package violet.gbcTunnels.pumps.utils.camellia;

import charlotte.tools.BinTools;
import charlotte.tools.CipherTools.Camellia;

public class CamelliaRingCBC {
	private Camellia[] _camellias;

	public CamelliaRingCBC(byte[] rawKey) throws Exception {
		_camellias = createCamellias(rawKey);
	}

	private static Camellia[] createCamellias(byte[] rawKey) throws Exception {
		//FileTools.writeAllBytes("C:/temp/1.bin", rawKey); // test
		if(rawKey.length < 16) {
			throw new IllegalArgumentException("Too short keys");
		}
		if(rawKey.length == 32) {
			return createCamellias(rawKey, 16);
		}
		if(rawKey.length % 32 == 0) {
			return createCamellias(rawKey, 32);
		}
		if(rawKey.length == 24) {
			throw new IllegalArgumentException("Too few keys");
		}
		if(rawKey.length % 24 == 0) {
			return createCamellias(rawKey, 24);
		}
		if(rawKey.length == 16) {
			throw new IllegalArgumentException("Too few keys");
		}
		if(rawKey.length % 16 == 0) {
			return createCamellias(rawKey, 16);
		}
		throw new IllegalArgumentException("Bad key length");
	}

	private static Camellia[] createCamellias(byte[] rawKey, int keyWidth) throws Exception {
		int deep = rawKey.length / keyWidth;
		Camellia[] camellias = new Camellia[deep];

		for(int index = 0; index < deep; index++) {
			camellias[index] = new Camellia(BinTools.getSubBytes(rawKey, index * keyWidth, (index + 1) * keyWidth));
		}
		return camellias;
	}

	public void encrypt(byte[] data) throws Exception {
		encrypt(data, 0);
	}

	public void encrypt(byte[] data, int offset) throws Exception {
		encrypt(data, offset, data.length - offset);
	}

	public void encrypt(byte[] data, int offset, int size) throws Exception {
		if(
				offset < 0 ||
				data.length < offset ||
				data.length - offset < size ||
				size < 32 ||
				size % 16 != 0
				) {
			throw new IllegalArgumentException();
		}

		for(Camellia camellia : _camellias) {
			encryptRingCBC(camellia, data, offset, size / 16);
		}
	}

	public void decrypt(byte[] data) throws Exception {
		decrypt(data, 0);
	}

	public void decrypt(byte[] data, int offset) throws Exception {
		decrypt(data, offset, data.length - offset);
	}

	public void decrypt(byte[] data, int offset, int size) throws Exception {
		if(
				offset < 0 ||
				data.length < offset ||
				data.length - offset < size ||
				size < 32 ||
				size % 16 != 0
				) {
			throw new IllegalArgumentException();
		}

		for(int index = _camellias.length - 1; 0 <= index; index--) {
			decryptRingCBC(_camellias[index], data, offset, size / 16);
		}
	}

	private static void encryptRingCBC(Camellia camellia, byte[] data, int offset, int blockCount) throws Exception {
		xorBlock(data, offset, data, offset + (blockCount - 1) * 16);
		camellia.encryptBlock(data, offset, data, offset);

		for(int index = 1; index < blockCount; index++) {
			xorBlock(data, offset + index * 16, data, offset + (index - 1) * 16);
			camellia.encryptBlock(data, offset + index * 16, data, offset + index * 16);
		}
	}

	private static void decryptRingCBC(Camellia camellia, byte[] data, int offset, int blockCount) throws Exception {
		for(int index = blockCount - 1; 1 <= index; index--) {
			camellia.decryptBlock(data, offset + index * 16, data, offset + index * 16);
			xorBlock(data, offset + index * 16, data, offset + (index - 1) * 16);
		}
		camellia.decryptBlock(data, offset, data, offset);
		xorBlock(data, offset, data, offset + (blockCount - 1) * 16);
	}

	private static void xorBlock(byte[] target, int targetOffset, byte[] mask, int maskOffset) {
		for(int index = 0; index < 16; index++) {
			target[targetOffset + index] ^= mask[maskOffset + index];
		}
	}
}
