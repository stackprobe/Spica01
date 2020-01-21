package violet.gbcTunnels.pumps;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import violet.gbcTunnels.GBCTunnelProps;
import violet.gbcTunnels.Ground;
import violet.gbcTunnels.pumps.utils.cbc16.CRC16;

public class BoomerangPump {
	public static final int CREDENTIAL_SIZE = 16;

	private static final int HEADER_SIZE = 20;

	private static final byte FLAG_BACKGROUND = (byte)0x42; // 'B'
	private static final byte FLAG_DISCONNECT = (byte)0x44; // 'D'
	private static final byte FLAG_FOREGROUND = (byte)0x46; // 'F'

	private static int _sendDataSizeMax;

	public static void init() {
		_sendDataSizeMax = GBCTunnelProps.boomerangSendDataSizeMax - HEADER_SIZE;
		_sendDataSizeMax = Math.max(1, _sendDataSizeMax); // 2bs
	}

	public static byte[] pump(byte[] data) throws Exception {
		if(Ground.connections.get().credential == null) {
			Ground.connections.get().credential = SecurityTools.cRandom.getBytes(CREDENTIAL_SIZE);
		}
		if(Ground.connections.get().disconnect) {
			pumpDisconnect();
			return BinTools.EMPTY;
		}
		if(data.length <= _sendDataSizeMax) {
			return pump2(data, 0, data.length);
		}

		{
			List<byte[]> buff = new ArrayList<byte[]>();

			for(int offset = 0; offset < data.length; offset += _sendDataSizeMax) {
				buff.add(pump2(data, offset, Math.min(data.length - offset, _sendDataSizeMax)));
			}
			return BinTools.join(buff);
		}
	}

	private static byte[] nextPump(byte[] data) throws Exception {
		return HTTPPump.pump(data);
	}

	private static CRC16 _crc16 = new CRC16();

	private static byte[] pump2(byte[] buff, int offset, int size) throws Exception {
		byte flag = Ground.connections.get().foregroundFlag ? FLAG_FOREGROUND : FLAG_BACKGROUND;

		Ground.connections.get().foregroundFlag = !Ground.connections.get().foregroundFlag;

		int crc16 = _crc16.start();
		crc16 = _crc16.update(crc16, Ground.connections.get().credential, 0, CREDENTIAL_SIZE);
		crc16 = _crc16.update(crc16, new byte[] { flag }, 0, 1);
		crc16 = _crc16.update(crc16, buff, offset, size);
		crc16 = _crc16.finish(crc16);

		byte[] data = new byte[4 + HEADER_SIZE + size];

		BinTools.toBytes(HEADER_SIZE + size, data);

		System.arraycopy(Ground.connections.get().credential, 0, data, 4, CREDENTIAL_SIZE);

		data[20] = flag;
		data[21] = (byte)0x00; // reserved
		data[22] = (byte)((crc16 >> 0) & 0xff);
		data[23] = (byte)((crc16 >> 8) & 0xff);

		System.arraycopy(buff, offset, data, 24, size);

		byte[] resData = nextPump(data);

		{
			byte[] resHeader = BinTools.getSubBytes(resData, 0, HEADER_SIZE);

			// FIXME todo check resHeader

			if(resHeader[20] == FLAG_DISCONNECT) {
				throw new Exception("DISCONNECT");
			}
		}

		byte[] resDataReal = BinTools.getSubBytes(resData, HEADER_SIZE);

		return resDataReal;
	}

	private static void pumpDisconnect() throws Exception {
		int crc16 = _crc16.start();
		crc16 = _crc16.update(crc16, Ground.connections.get().credential, 0, CREDENTIAL_SIZE);
		crc16 = _crc16.update(crc16, new byte[] { 0x44 }, 0, 1);
		crc16 = _crc16.finish(crc16);

		byte[] data = new byte[4 + HEADER_SIZE];

		BinTools.toBytes(HEADER_SIZE, data);

		System.arraycopy(Ground.connections.get().credential, 0, data, 4, CREDENTIAL_SIZE);

		data[20] = FLAG_DISCONNECT;
		data[21] = (byte)0x00; // reserved
		data[22] = (byte)((crc16 >> 0) & 0xff);
		data[23] = (byte)((crc16 >> 8) & 0xff);

		nextPump(data);

		//throw new Exception("DISCONNECT"); // not needed
	}
}
