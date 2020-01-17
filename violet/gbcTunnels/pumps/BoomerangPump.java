package violet.gbcTunnels.pumps;

import violet.gbcTunnels.Consts;
import violet.gbcTunnels.GBCTunnelProps;
import violet.gbcTunnels.IPump;
import violet.gbcTunnels.PumpPacket;
import violet.gbcTunnels.pumps.utils.cbc16.CRC16;

public class BoomerangPump implements IPump {
	private static final int HEADER_SIZE = 20;

	private static final byte FLAG_BACKGROUND = (byte)0x42; // 'B'
	private static final byte FLAG_DISCONNECT = (byte)0x44; // 'D'
	private static final byte FLAG_FOREGROUND = (byte)0x46; // 'F'

	private static int _sendDataSizeMax = -1;

	@Override
	public void pump(PumpPacket packet, IPump nextPump) throws Exception {
		if(_sendDataSizeMax == -1) {
			_sendDataSizeMax = GBCTunnelProps.boomerangSendDataSizeMax - HEADER_SIZE;
			_sendDataSizeMax = Math.max(1, _sendDataSizeMax); // 2bs
		}

		if(packet.disconnect) {
			pumpDisconnect(packet, nextPump);
		}
		else {
			for(int offset = 0; offset < packet.data.length; offset += _sendDataSizeMax) {
				pump2(packet, offset, Math.min(packet.data.length - offset, _sendDataSizeMax), nextPump);
			}
		}
	}

	private static CRC16 _crc16 = new CRC16();

	private void pump2(PumpPacket packet, int offset, int size, IPump nextPump) throws Exception {
		PumpPacket pp = packet.getTemp();

		byte flag = packet.connection.foregroundFlag ? FLAG_FOREGROUND : FLAG_BACKGROUND;

		packet.connection.foregroundFlag = !packet.connection.foregroundFlag;

		int crc16 = _crc16.start();
		crc16 = _crc16.update(crc16, packet.connection.credential, 0, Consts.CREDENTIAL_SIZE);
		crc16 = _crc16.update(crc16, new byte[] { flag }, 0, 1);
		crc16 = _crc16.update(crc16, packet.data, 0, packet.data.length);
		crc16 = _crc16.finish(crc16);

		pp.data = new byte[4 + HEADER_SIZE + size];

		pp.data[0] = (byte)((pp.data.length >>  0) & 0xff);
		pp.data[1] = (byte)((pp.data.length >>  8) & 0xff);
		pp.data[2] = (byte)((pp.data.length >> 16) & 0xff);
		pp.data[3] = (byte)((pp.data.length >> 24) & 0xff);

		System.arraycopy(packet.connection.credential, 0, pp.data, 4, Consts.CREDENTIAL_SIZE);

		pp.data[20] = flag;
		pp.data[21] = (byte)0x00; // reserved
		pp.data[22] = (byte)((crc16 >> 0) & 0xff);
		pp.data[23] = (byte)((crc16 >> 8) & 0xff);

		System.arraycopy(packet.data, offset, pp.data, 24, size);

		nextPump.pump(pp);

		{
			byte[] resHeader = pp.readFromResData(HEADER_SIZE);

			// TODO check resHeader

			if(resHeader[20] == FLAG_DISCONNECT) {
				throw new Exception("DISCONNECT");
			}
		}

		packet.resDataParts.add(pp.getResData());
	}

	private void pumpDisconnect(PumpPacket packet, IPump nextPump) throws Exception {
		PumpPacket pp = packet.getTemp();

		int crc16 = _crc16.start();
		crc16 = _crc16.update(crc16, packet.connection.credential, 0, Consts.CREDENTIAL_SIZE);
		crc16 = _crc16.update(crc16, new byte[] { 0x44 }, 0, 1);
		crc16 = _crc16.finish(crc16);

		pp.data = new byte[4 + HEADER_SIZE];

		pp.data[0] = (byte)((pp.data.length >>  0) & 0xff);
		pp.data[1] = (byte)((pp.data.length >>  8) & 0xff);
		pp.data[2] = (byte)((pp.data.length >> 16) & 0xff);
		pp.data[3] = (byte)((pp.data.length >> 24) & 0xff);

		System.arraycopy(packet.connection.credential, 0, pp.data, 4, Consts.CREDENTIAL_SIZE);

		pp.data[20] = FLAG_DISCONNECT;
		pp.data[21] = (byte)0x00; // reserved
		pp.data[22] = (byte)((crc16 >> 0) & 0xff);
		pp.data[23] = (byte)((crc16 >> 8) & 0xff);

		nextPump.pump(pp);

		//throw new Exception("DISCONNECT"); // not needed
	}
}
