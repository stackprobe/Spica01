package violet.camellia;

import java.io.ByteArrayOutputStream;

import charlotte.tools.BinTools;
import charlotte.tools.RTError;
import charlotte.tools.SecurityTools;

public class CamelliaRingCipher {
	private CamelliaRingCBC _crcbc;

	public CamelliaRingCipher(String passphrase) throws Exception {
		this(CamelliaRingCipherUtils.generateRawKey(passphrase));
	}

	public CamelliaRingCipher(byte[] rawKey) throws Exception {
		_crcbc = new CamelliaRingCBC(rawKey);
	}

	public byte[] encrypt(byte[] src) throws Exception {
		return encrypt(src, 0);
	}

	public byte[] encrypt(byte[] src, int startIndex) throws Exception
	{
		return encrypt(src, startIndex, src.length);
	}

	public byte[] encrypt(byte[] src, int startIndex, int endIndex) throws Exception {
		if(
				startIndex < 0 ||
				src.length < endIndex ||
				endIndex < startIndex
				) {
			throw new IllegalArgumentException();
		}
		byte[] dest;

		try(ByteArrayOutputStream mem = new ByteArrayOutputStream()) {
			mem.write(src, startIndex, endIndex - startIndex);
			addPadding(mem);
			addRandPart(mem);
			addHash(mem);
			addRandPart(mem);
			dest = mem.toByteArray();
		}
		_crcbc.encrypt(dest);
		return dest;
	}

	public byte[] decrypt(byte[] src) throws Exception {
		return decrypt(src, 0);
	}

	public byte[] decrypt(byte[] src, int startIndex) throws Exception {
		return decrypt(src, startIndex, src.length);
	}

	public byte[] decrypt(byte[] src, int startIndex, int endIndex) throws Exception {
		if(
				startIndex < 0 ||
				src.length < endIndex ||
				endIndex < startIndex ||
				(endIndex - startIndex) < 16 + 64 + 64 + 64 ||
				//(endIndex - startIndex) < 256 + 64 + 64 + 64 || // Padding min size (old, new) : (16, 256)
				(endIndex - startIndex) % 16 != 0
				) {
			throw new IllegalArgumentException();
		}
		byte[] buff = BinTools.getSubBytes(src, startIndex, endIndex);

		_crcbc.decrypt(buff);

		int buffSize = buff.length;

		buffSize = unaddRandPart(buff, buffSize);
		buffSize = unaddHash(buff, buffSize);
		buffSize = unaddRandPart(buff, buffSize);
		buffSize = unaddPadding(buff, buffSize);

		return BinTools.getSubBytes(buff, 0, buffSize);
	}

	private static void addPadding(ByteArrayOutputStream mem) {
		int size = (int)mem.size();
		int padSzLow = ~size & 0x0f;
		int padSize;

		do {
			padSize = padSzLow | ((int)SecurityTools.cRandom.getByte() & 0xf0);
		}
		while(size + padSize < 0xff);

		for(int index = 0; index < padSize; index++) {
			mem.write(SecurityTools.cRandom.getByte());
		}
		mem.write(padSize);
	}

	private static int unaddPadding(byte[] data, int size) {
		if(size < 1) {
			throw new RTError("Bad data size");
		}
		size--;
		int padSize = (int)data[size];

		if(size < padSize) {
			throw new RTError("Bad data size");
		}
		size -= padSize;
		return size;
	}

	private static final int RAND_PART_SIZE = 64;

	private static void addRandPart(ByteArrayOutputStream mem) {
		mem.write(SecurityTools.cRandom.getBytes(RAND_PART_SIZE), 0, RAND_PART_SIZE);
	}

	private static int unaddRandPart(byte[] data, int size) {
		if(size < RAND_PART_SIZE) {
			throw new RTError("Bad data size");
		}
		size -= RAND_PART_SIZE;
		return size;
	}

	private static final int HASH_SIZE = 64;

	private static void addHash(ByteArrayOutputStream mem) throws Exception {
		mem.write(SecurityTools.getSHA512(mem.toByteArray()), 0, HASH_SIZE);
	}

	private static int unaddHash(byte[] data, int size) throws Exception {
		if(size < HASH_SIZE) {
			throw new RTError("Bad data size");
		}
		size -= HASH_SIZE;

		final int f_size = size;

		byte[] hash1 = BinTools.getSubBytes(data, size, HASH_SIZE);
		byte[] hash2 = SecurityTools.getSHA512(writer -> {
			try {
				writer.write(data, 0, f_size);
			}
			catch(Throwable e) {
				throw RTError.re(e);
			}
		});

		if(BinTools.comp_array.compare(hash1, hash2) != 0) {
			throw new RTError("Bad hash");
		}
		return size;
	}
}
