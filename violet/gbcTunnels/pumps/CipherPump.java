package violet.gbcTunnels.pumps;

import java.util.Scanner;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import violet.gbcTunnels.IPump;
import violet.gbcTunnels.PumpPacket;
import violet.gbcTunnels.pumps.utils.camellia.CamelliaRingCipher;

public class CipherPump implements IPump {
	private static final int COUNTER_SIZE = 64;

	private CamelliaRingCipher _cipher = null;

	private CamelliaRingCipher getCipher() throws Exception {
		if(_cipher == null) {
			_cipher = getCipher_noCache();
		}
		return _cipher;
	}

	private CamelliaRingCipher getCipher_noCache() throws Exception {
		try(Scanner sc = new Scanner(System.in)) {
			System.out.println("Input passphrase:");
			String passphrase = sc.nextLine();
			System.out.println("passphrase: " + passphrase);

			return new CamelliaRingCipher(passphrase);
		}
	}

	private byte[] exchangeCounter(PumpPacket packet, IPump nextPump, byte[] data) throws Exception {
		PumpPacket pp = packet.getTemp();

		data = getCipher().encrypt(data);
		data = BinTools.join(new byte[][] {
			new byte[] { (byte)(data.length / 16) },
			data
			});

		pp.data = data;

		nextPump.pump(pp);
		nextPump.recvWhile(pp, 1);

		int resDataSize = (pp.readFromResData(1)[0] & 0xff) * 16;

		nextPump.recvWhile(pp, resDataSize);

		byte[] resData = pp.readFromResData(resDataSize);

		resData = getCipher().decrypt(resData);

		if(resData.length != COUNTER_SIZE) {
			throw new Exception("Bad resData");
		}
		if(pp.getResData().length != 0) {
			throw new Exception("Bad pp");
		}
		return resData;
	}

	private void increment(byte[] counter) {
		for(int index = 0; index < counter.length; index++) {
			int bCount = counter[index] & 0xff;

			if(bCount < 0xff) {
				bCount++;
				counter[index] = (byte)bCount;
				break;
			}
			counter[index] = 0x00;
		}
	}

	@Override
	public void pump(PumpPacket packet, IPump nextPump) throws Exception {
		if(packet.connection.decCounter == null) {
			packet.connection.decCounter = SecurityTools.cRandom.getBytes(COUNTER_SIZE);
			packet.connection.encCounter = exchangeCounter(packet, nextPump, packet.connection.decCounter);

			increment(packet.connection.decCounter);
			increment(packet.connection.encCounter);

			byte[] wc = exchangeCounter(packet, nextPump, packet.connection.encCounter);

			if(BinTools.comp_array.compare(wc, packet.connection.decCounter) != 0) {
				throw new Exception("Bad wc");
			}
			increment(packet.connection.decCounter);
			increment(packet.connection.encCounter);
		}

		throw null; // TODO
	}
}
