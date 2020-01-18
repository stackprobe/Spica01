package violet.gbcTunnels.pumps;

import violet.gbcTunnels.Consts;
import violet.gbcTunnels.GBCTunnelProps;
import violet.gbcTunnels.Ground;
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

		if(Ground.currThConnections.get().disconnect) {
			pumpDisconnect(packet, nextPump);
		}
		else if(packet.data.length <= _sendDataSizeMax) {
			pump2(packet, 0, packet.data.length, nextPump);
		}
		else {
			for(int offset = 0; offset < packet.data.length; offset += _sendDataSizeMax) {
				pump2(packet, offset, Math.min(packet.data.length - offset, _sendDataSizeMax), nextPump);
			}
		}
	}

	private static CRC16 _crc16 = new CRC16();

	private void pump2(PumpPacket packet, int offset, int size, IPump nextPump) throws Exception {
		byte flag = Ground.currThConnections.get().foregroundFlag ? FLAG_FOREGROUND : FLAG_BACKGROUND;

		Ground.currThConnections.get().foregroundFlag = !Ground.currThConnections.get().foregroundFlag;

		int crc16 = _crc16.start();
		crc16 = _crc16.update(crc16, Ground.currThConnections.get().credential, 0, Consts.CREDENTIAL_SIZE);
		crc16 = _crc16.update(crc16, new byte[] { flag }, 0, 1);
		crc16 = _crc16.update(crc16, packet.data, 0, packet.data.length);
		crc16 = _crc16.finish(crc16);

		byte[] data = new byte[4 + HEADER_SIZE + size];

		data[0] = (byte)((data.length >>  0) & 0xff);
		data[1] = (byte)((data.length >>  8) & 0xff);
		data[2] = (byte)((data.length >> 16) & 0xff);
		data[3] = (byte)((data.length >> 24) & 0xff);

		System.arraycopy(Ground.currThConnections.get().credential, 0, data, 4, Consts.CREDENTIAL_SIZE);

		data[20] = flag;
		data[21] = (byte)0x00; // reserved
		data[22] = (byte)((crc16 >> 0) & 0xff);
		data[23] = (byte)((crc16 >> 8) & 0xff);

		System.arraycopy(packet.data, offset, data, 24, size);

		PumpPacket pp = new PumpPacket(data);

		nextPump.pump(pp);

		{
			byte[] resHeader = pp.readFromResData(HEADER_SIZE);

			// FIXME todo check resHeader

			if(resHeader[20] == FLAG_DISCONNECT) {
				throw new Exception("DISCONNECT");
			}
		}

		packet.resDataParts.addAll(pp.resDataParts);
	}

	private void pumpDisconnect(PumpPacket packet, IPump nextPump) throws Exception {
		int crc16 = _crc16.start();
		crc16 = _crc16.update(crc16, Ground.currThConnections.get().credential, 0, Consts.CREDENTIAL_SIZE);
		crc16 = _crc16.update(crc16, new byte[] { 0x44 }, 0, 1);
		crc16 = _crc16.finish(crc16);

		byte[] data = new byte[4 + HEADER_SIZE];

		data[0] = (byte)((data.length >>  0) & 0xff);
		data[1] = (byte)((data.length >>  8) & 0xff);
		data[2] = (byte)((data.length >> 16) & 0xff);
		data[3] = (byte)((data.length >> 24) & 0xff);

		System.arraycopy(Ground.currThConnections.get().credential, 0, data, 4, Consts.CREDENTIAL_SIZE);

		data[20] = FLAG_DISCONNECT;
		data[21] = (byte)0x00; // reserved
		data[22] = (byte)((crc16 >> 0) & 0xff);
		data[23] = (byte)((crc16 >> 8) & 0xff);

		PumpPacket pp = new PumpPacket(data);

		nextPump.pump(pp);

		//throw new Exception("DISCONNECT"); // not needed
	}
}
