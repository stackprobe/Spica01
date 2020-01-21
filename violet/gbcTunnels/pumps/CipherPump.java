package violet.gbcTunnels.pumps;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import violet.gbcTunnels.GBCTunnelProps;
import violet.gbcTunnels.Ground;
import violet.gbcTunnels.PumpPacket;
import violet.gbcTunnels.pumps.utils.PumpBinBuffer;
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

	private static byte[] exchangeCounter(PumpBinBuffer recvBuff, byte[] counter) throws Exception {
		counter = getCipher().encrypt(counter);

		recvBuff = BinTools.join(new byte[][] {
			recvBuff,
			nextPump(BinTools.join(new byte[][] {
				new byte[] { (byte)(counter.length / 16) },
				counter
			}
			)),
		});

		recvBuff = recvWhileToSize(recvBuff, 1);

		int resDataSize = (recvBuff[0] & 0xff) * 16;

		recvBuff =

		pp.recvWhileToSize(resDataSize, nextPump);

		byte[] resCounter = pp.readFromResData(resDataSize);

		resCounter = getCipher().decrypt(resCounter);

		if(resCounter.length != COUNTER_SIZE) {
			throw new Exception("Bad resData");
		}
		packet.resDataParts.addAll(pp.resDataParts);

		return resCounter;
	}

	public static byte[] pump(byte[] data) throws Exception {
		PumpBinBuffer recvBuff = new PumpBinBuffer();

		if(Ground.connections.get().decCounter == null) {
			Ground.connections.get().decCounter = SecurityTools.cRandom.getBytes(COUNTER_SIZE);
			Ground.connections.get().encCounter = exchangeCounter(recvBuff, Ground.connections.get().decCounter);

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

		{
			byte[] data = getCipher().encrypt(packet.data);

			data = BinTools.join(new byte[][] {
				BinTools.toBytes(data.length),
				data
				});

			PumpPacket pp = new PumpPacket(data);

			nextPump.pump(pp);

			packet.resDataParts.addAll(pp.resDataParts);
		}

		List<byte[]> resBuff = new ArrayList<byte[]>();

		while(1 <= packet.getResData().length) {
			packet.recvWhileToSize(4, nextPump);

			int size = BinTools.toInt(packet.readFromResData(4));

			packet.recvWhileToSize(size, nextPump);

			byte[] data = packet.readFromResData(size);

			data = getCipher().decrypt(data);
			resBuff.add(data);
		}
		packet.resDataParts.addAll(resBuff);
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
}
