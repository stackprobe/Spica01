package violet.gbcTunnels.pumps;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import violet.gbcTunnels.GBCTunnelProps;
import violet.gbcTunnels.Ground;
import violet.gbcTunnels.utils.cbc16.CRC16;

public class BoomerangPump {
	public static final int CREDENTIAL_SIZE = 16;

	private static final int HEADER_SIZE = 20;

	private static final byte FLAG_BACKGROUND = (byte)0x42; // 'B'
	private static final byte FLAG_DISCONNECT = (byte)0x44; // 'D'
	private static final byte FLAG_ERROR_RECV = (byte)0x45; // 'E'
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

		byte[] data = new byte[HEADER_SIZE + size];

		System.arraycopy(Ground.connections.get().credential, 0, data, 0, CREDENTIAL_SIZE);

		int wIndex = CREDENTIAL_SIZE;

		data[wIndex++] = flag;
		data[wIndex++] = (byte)0x00; // reserved
		data[wIndex++] = (byte)((crc16 >> 0) & 0xff);
		data[wIndex++] = (byte)((crc16 >> 8) & 0xff);

		System.arraycopy(buff, offset, data, wIndex, size);

		byte[] resData = nextPump(data);

		{
			byte[] resHeader = BinTools.getSubBytes(resData, 0, HEADER_SIZE);
			byte[] resCredential = BinTools.getSubBytes(resHeader, 0, CREDENTIAL_SIZE);

			int rIndex = CREDENTIAL_SIZE;

			byte resFlag = resHeader[rIndex++];
			byte resReserved = resHeader[rIndex++];
			byte resCrc16_L = resHeader[rIndex++];
			byte resCrc16_H = resHeader[rIndex++];
			int resCrc16 = (resCrc16_L & 0xff) | ((resCrc16_H & 0xff) << 8);

			if(BinTools.comp_array.compare(Ground.connections.get().credential, resCredential) != 0) {
				throw new Exception("Bad resCredential");
			}
			if(resFlag != FLAG_DISCONNECT && resFlag != FLAG_ERROR_RECV && resFlag != 0x00) {
				throw new Exception("Bad resFlag");
			}
			if(resReserved != 0x00) {
				throw new Exception("Bad resReserved");
			}

			{
				int wCrc16 = _crc16.start();
				wCrc16 = _crc16.update(wCrc16, resCredential, 0, CREDENTIAL_SIZE);
				wCrc16 = _crc16.update(wCrc16, new byte[] { resFlag }, 0, 1);
				wCrc16 = _crc16.update(wCrc16, resData, HEADER_SIZE, resData.length - HEADER_SIZE);
				wCrc16 = _crc16.finish(wCrc16);

				if(resCrc16 != wCrc16) {
					throw new Exception("Bad resCrc16");
				}
			}

			// ----

			if(resFlag == FLAG_DISCONNECT) {
				throw new Exception("DISCONNECT");
			}
			if(resFlag == FLAG_ERROR_RECV) {
				throw new Exception("ERROR_ON_SERVER_SIDE");
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

		byte[] data = new byte[HEADER_SIZE];

		System.arraycopy(Ground.connections.get().credential, 0, data, 4, CREDENTIAL_SIZE);

		data[20] = FLAG_DISCONNECT;
		data[21] = (byte)0x00; // reserved
		data[22] = (byte)((crc16 >> 0) & 0xff);
		data[23] = (byte)((crc16 >> 8) & 0xff);

		nextPump(data);

		//throw new Exception("DISCONNECT"); // not needed
	}
}
