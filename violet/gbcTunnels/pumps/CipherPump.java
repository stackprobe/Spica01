package violet.gbcTunnels.pumps;

import java.util.Scanner;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import violet.gbcTunnels.Ground;
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
		data = getCipher().encrypt(data);
		data = BinTools.join(new byte[][] {
			new byte[] { (byte)(data.length / 16) },
			data
			});

		PumpPacket pp = new PumpPacket(data);

		pp.resDataParts.addAll(packet.resDataParts);

		nextPump.pump(pp);
		nextPump.recvWhile(pp, nextPump, 1);

		int resDataSize = (pp.readFromResData(1)[0] & 0xff) * 16;

		nextPump.recvWhile(pp, nextPump, resDataSize);

		byte[] resData = pp.readFromResData(resDataSize);

		resData = getCipher().decrypt(resData);

		if(resData.length != COUNTER_SIZE) {
			throw new Exception("Bad resData");
		}
		packet.resDataParts.addAll(pp.resDataParts);

		return resData;
	}

	private void increment(byte[] counter) {
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

	@Override
	public void pump(PumpPacket packet, IPump nextPump) throws Exception {
		if(Ground.currThConnections.get().counterExchanged == false) {
			Ground.currThConnections.get().decCounter = SecurityTools.cRandom.getBytes(COUNTER_SIZE);
			Ground.currThConnections.get().encCounter = exchangeCounter(packet, nextPump, Ground.currThConnections.get().decCounter);

			increment(Ground.currThConnections.get().decCounter);
			increment(Ground.currThConnections.get().encCounter);

			byte[] wc = exchangeCounter(packet, nextPump, Ground.currThConnections.get().encCounter);

			if(BinTools.comp_array.compare(wc, Ground.currThConnections.get().decCounter) != 0) {
				throw new Exception("Bad wc");
			}
			increment(Ground.currThConnections.get().decCounter);
			increment(Ground.currThConnections.get().encCounter);

			Ground.currThConnections.get().counterExchanged = true;
		}

		throw null; // TODO
	}
}
