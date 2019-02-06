package wb.t20190204.b;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import charlotte.tools.BinTools;
import charlotte.tools.CipherTools;
import charlotte.tools.SecurityTools;

public class CipherInfo implements AutoCloseable {
	public CipherInfo() throws Exception {
		this(
				new CipherTools.IBlockCipher[] {
						new CipherTools.AES(SecurityTools.cRandom.getBytes(32)),
						new CipherTools.AES(SecurityTools.cRandom.getBytes(32)),
				},
				new CipherTools.AES(SecurityTools.cRandom.getBytes(32))
				);
	}

	private CipherTools.IBlockCipher[] _ciphers;
	private CipherTools.IBlockCipher _hashCipher;

	public CipherInfo(CipherTools.IBlockCipher[] ciphers, CipherTools.IBlockCipher hashCipher) {
		_ciphers = ciphers;
		_hashCipher = hashCipher;
	}

	public byte[] encrypt(byte[] data) throws Exception {
		try(ByteArrayOutputStream mem = new ByteArrayOutputStream((data.length / 16) * 16 + 48)) {
			mem.write(data);
			mem.write(0xff);

			while(mem.size() % 16 != 0) {
				mem.write(0x00);
			}
			for(int c = 0; c < 16; c++) {
				mem.write(SecurityTools.cRandom.getByte() & 0xff);
			}
			mem.write(new byte[16]); // zero clear hash

			data = mem.toByteArray();
		}
		final int FINAL_INDEX = data.length - 16;
		byte[] block = new byte[16];

		for(int index = 0; index < FINAL_INDEX; index += 16) {
			for(int c = 0; c < 16; c++) {
				data[FINAL_INDEX + c] ^= data[index + c];
			}
			_hashCipher.encryptBlock(data, FINAL_INDEX, block, 0);
			System.arraycopy(block, 0, data, FINAL_INDEX, 16);
		}
		for(CipherTools.IBlockCipher cipher : _ciphers) {
			int lastIndex = FINAL_INDEX;

			for(int index = 0; index < data.length; index += 16) {
				for(int c = 0; c < 16; c++) {
					data[index + c] ^= data[lastIndex + c];
				}
				cipher.encryptBlock(data, index, block, 0);
				System.arraycopy(block, 0, data, index, 16);

				lastIndex = index;
			}
		}
		return data;
	}

	public byte[] decrypt(byte[] data) throws Exception {
		if(data.length < 48 || data.length % 16 != 0) {
			throw new IllegalArgumentException("Bad data length");
		}
		final int FINAL_INDEX = data.length - 16;
		byte[] block = new byte[16];

		for(int cipher_index = _ciphers.length - 1; 0 <= cipher_index; cipher_index--) {
			CipherTools.IBlockCipher cipher = _ciphers[cipher_index];

			for(int index = FINAL_INDEX; 0 <= index; index -= 16) {
				cipher.decryptBlock(data, index, block, 0);
				System.arraycopy(block, 0, data, index, 16);

				int prevIndex = (index + FINAL_INDEX) % data.length;

				for(int c = 0; c < 16; c++) {
					data[index + c] ^= data[prevIndex + c];
				}
			}
		}

		byte[] hash = new byte[16];
		for(int index = 0; index < FINAL_INDEX; index += 16) {
			for(int c = 0; c < 16; c++) {
				hash[c] ^= data[index + c];
			}
			_hashCipher.encryptBlock(hash, block);
			System.arraycopy(block, 0, hash, 0, 16);
		}
		System.arraycopy(data, FINAL_INDEX, block, 0, 16);
		if(BinTools.comp_array.compare(hash, block) != 0) {
			throw new Exception("Bad hash");
		}

		int count;
		for(count = data.length - 33; data[count] == 0x00; count--) {
			// noop
		}
		data = Arrays.copyOf(data, count);

		return data;
	}

	@Override
	public void close() throws Exception {
		if(_ciphers != null) {
			for(CipherTools.IBlockCipher cipher : _ciphers) {
				cipher.close();
			}
			_hashCipher.close();
			_ciphers = null;
		}
	}
}
