package wb.t20200125_GBCTunnels.pumps;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import wb.t20200125_GBCTunnels.GBCTunnelProps;
import wb.t20200125_GBCTunnels.Ground;
import wb.t20200125_GBCTunnels.utils.camellia.CamelliaRingCipher;

public class CipherPump {
	private static CamelliaRingCipher _cipher;

	public static void init() throws Exception {
		_cipher = new CamelliaRingCipher(GBCTunnelProps.passphrase);
	}

	private static void nextPump(byte[] data) throws Exception {
		Ground.connections.get().cipherPumpRecvBuffer.write(BoomerangPump.pump(data));
	}

	private static byte[] recv(int size) throws Exception {
		while(Ground.connections.get().cipherPumpRecvBuffer.size() < size) {
			nextPump(BinTools.EMPTY);
		}
		return Ground.connections.get().cipherPumpRecvBuffer.read(size);
	}

	private static final int COUNTER_SIZE = 64;

	private static byte[] exchangeCounter(byte[] counter) throws Exception {
		byte[] eCounter = _cipher.encrypt(counter);
		int eCounterSize = eCounter.length;
		byte[] szECounter = BinTools.join(new byte[] { (byte)(eCounterSize / 16) }, eCounter);

		nextPump(szECounter);

		int eResCounterSize = (recv(1)[0] & 0xff) * 16;
		byte[] eResCounter = recv(eResCounterSize);
		byte[] resCounter = _cipher.decrypt(eResCounter);

		if(resCounter.length != COUNTER_SIZE) {
			throw new Exception("Bad resCounter");
		}
		return resCounter;
	}

	private static void increment(byte[] counter) {
		for(int index = 0; index < counter.length; index++) {
			int figure = counter[index] & 0xff;

			if(figure < 0xff) {
				figure++;
				counter[index] = (byte)figure;
				break;
			}
			counter[index] = 0x00;
		}
	}

	public static byte[] pump(byte[] data) throws Exception {
		if(Ground.connections.get().decCounter == null) {
			Ground.connections.get().decCounter = SecurityTools.cRandom.getBytes(COUNTER_SIZE);
			Ground.connections.get().encCounter = exchangeCounter(Ground.connections.get().decCounter);

			increment(Ground.connections.get().decCounter);
			increment(Ground.connections.get().encCounter);

			byte[] wc = exchangeCounter(Ground.connections.get().encCounter);

			if(BinTools.comp_array.compare(wc, Ground.connections.get().decCounter) != 0) {
				throw new Exception("Bad wc");
			}
			increment(Ground.connections.get().decCounter);
			increment(Ground.connections.get().encCounter);
		}

		// memo: don't encrypt empty block --> C:/Dev/wb/t20200122_GBCTunnelTest/memo.txt
		if(1 <= data.length) {
			byte[] dataCtr = BinTools.join(
					data,
					Ground.connections.get().encCounter
					);
			byte[] eDataCtr = _cipher.encrypt(dataCtr);
			byte[] szEDataCtr = BinTools.join(
					BinTools.toBytes(eDataCtr.length),
					eDataCtr
					);

			increment(Ground.connections.get().encCounter);

			nextPump(szEDataCtr);
		}
		else {
			nextPump(BinTools.EMPTY);
		}

		byte[] ret;

		if(1 <= Ground.connections.get().cipherPumpRecvBuffer.size()) {
			int eResDataSize = BinTools.toInt(recv(4));
			byte[] eResData = recv(eResDataSize);
			byte[] resData = _cipher.decrypt(eResData);

			if(resData.length < COUNTER_SIZE) {
				throw new Exception("Bad resData");
			}
			byte[] resDataReal = BinTools.getSubBytes(resData, 0, resData.length - COUNTER_SIZE);
			byte[] wc = BinTools.getSubBytes(resData, resData.length - COUNTER_SIZE);

			if(BinTools.comp_array.compare(wc, Ground.connections.get().decCounter) != 0) {
				throw new Exception("Bad resDataReal");
			}
			increment(Ground.connections.get().decCounter);

			ret = resDataReal;
		}
		else {
			ret = BinTools.EMPTY;
		}
		return ret;
	}
}
