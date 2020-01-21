package violet.gbcTunnels.pumps;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import violet.gbcTunnels.GBCTunnelProps;
import violet.gbcTunnels.Ground;
import violet.gbcTunnels.pumps.utils.camellia.CamelliaRingCipher;

public class CipherPump {
	private static final int COUNTER_SIZE = 64;

	private static CamelliaRingCipher _cipher = null;

	private static CamelliaRingCipher getCipher() throws Exception {
		if(_cipher == null) {
			_cipher = new CamelliaRingCipher(GBCTunnelProps.passphrase);
		}
		return _cipher;
	}

	private static byte[] nextPump(byte[] data) {
		return BoomerangPump.pump(data);
	}

	private static byte[] recv(int size) {
		while(Ground.connections.get().cipherRecvBuffer.size() < size) {
			Ground.connections.get().cipherRecvBuffer.write(nextPump(BinTools.EMPTY));
		}
		return Ground.connections.get().cipherRecvBuffer.read(size);
	}

	private static byte[] exchangeCounter(byte[] counter) throws Exception {
		byte[] eCounter = getCipher().encrypt(counter);
		int eCounterSize = eCounter.length;
		byte[] szECounter = BinTools.join(new byte[][] { new byte[] { (byte)(eCounterSize / 16) }, eCounter });

		nextPump(szECounter);

		int eResCounterSize = (recv(1)[0] & 0xff) * 16;
		byte[] eResCounter = recv(eResCounterSize);
		byte[] resCounter = getCipher().decrypt(eResCounter);

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

		{
			byte[] eData = getCipher().encrypt(data);
			byte[] szEData = BinTools.join(new byte[][] {
				BinTools.toBytes(eData.length),
				eData,
				Ground.connections.get().encCounter,
			});

			increment(Ground.connections.get().encCounter);

			nextPump(szEData);
		}

		byte[] ret;

		if(1 <= Ground.connections.get().cipherRecvBuffer.size()) {
			int eResDataSize = BinTools.toInt(recv(4));
			byte[] eResData = recv(eResDataSize);
			byte[] resData = getCipher().decrypt(eResData);

			if(resData.length < COUNTER_SIZE) {
				throw new Exception("Bad resData");
			}
			byte[] resDataReal = BinTools.getSubBytes(resData, 0, resData.length - COUNTER_SIZE);
			byte[] wc = BinTools.getSubBytes(resData, resData.length - COUNTER_SIZE);

			if(BinTools.comp_array.compare(Ground.connections.get().decCounter, wc) != 0) {
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
