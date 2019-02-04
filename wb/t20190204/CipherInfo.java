package wb.t20190204;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import charlotte.tools.BinTools;
import charlotte.tools.CipherTools;
import charlotte.tools.SecurityTools;

/**
 * deprecated ???
 *
 */
public class CipherInfo implements AutoCloseable {
	public CipherInfo() throws Exception {
		this(
				new CipherTools.IBlockCipher[] {
						new CipherTools.AES(SecurityTools.cRandom.getBytes(32)),
						new CipherTools.AES(SecurityTools.cRandom.getBytes(32)),
				},
				SecurityTools.cRandom.getBytes(16)
				);
	}

	private CipherTools.IBlockCipher[] _ciphers;
	private byte[] _credential; // 16 bytes

	public CipherInfo(CipherTools.IBlockCipher[] ciphers, byte[] credential) {
		_ciphers = ciphers;
		_credential = credential;
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
			mem.write(_credential);

			data = mem.toByteArray();
		}
		byte[] block = new byte[16];

		// 'Block Encryption + CBC-R Decryption' on encrypt()
		for(int cipher_index = _ciphers.length - 1; 0 <= cipher_index; cipher_index--) {
			CipherTools.IBlockCipher cipher = _ciphers[cipher_index];

			for(int index = data.length - 16; 0 <= index; index -= 16) {
				cipher.encryptBlock(data, index, block, 0);
				System.arraycopy(block, 0, data, index, 16);

				int prevIndex = (index + data.length - 16) % data.length;

				for(int c = 0; c < 16; c++) {
					data[index + c] ^= data[prevIndex + c];
				}
			}

			// extended
			cipher.encryptBlock(data, data.length - 16, block, 0);
			System.arraycopy(block, 0, data, data.length - 16, 16);
		}
		return data;
	}

	public byte[] decrypt(byte[] data) throws Exception {
		if(data.length < 48 || data.length % 16 != 0) {
			throw new IllegalArgumentException("Bad data length");
		}
		byte[] block = new byte[16];

		// 'Block Decryption + CBC-R Encryption' on decrypt()
		for(CipherTools.IBlockCipher cipher : _ciphers) {
			int lastIndex = data.length - 16;

			// extended
			cipher.decryptBlock(data, lastIndex, block, 0);
			System.arraycopy(block, 0, data, lastIndex, 16);

			for(int index = 0; index < data.length; index += 16) {
				for(int c = 0; c < 16; c++) {
					data[index + c] ^= data[lastIndex + c];
				}
				cipher.decryptBlock(data, index, block, 0);
				System.arraycopy(block, 0, data, index, 16);

				lastIndex = index;
			}
		}

		System.arraycopy(data, data.length - 16, block, 0, 16);
		if(BinTools.comp_array.compare(block, _credential) != 0) {
			throw new Exception("Bad credential");
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
			_ciphers = null;
		}
	}
}
