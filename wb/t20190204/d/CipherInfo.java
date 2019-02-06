package wb.t20190204.d;

import java.util.Arrays;

import charlotte.tools.BinTools;
import charlotte.tools.CipherTools;
import charlotte.tools.CipherTools.IBlockCipher;
import charlotte.tools.SecurityTools;

public class CipherInfo implements AutoCloseable {
	public CipherInfo() throws Exception {
		this(new CipherTools.IBlockCipher[] {
				new CipherTools.AES(SecurityTools.cRandom.getBytes(32)),
				new CipherTools.AES(SecurityTools.cRandom.getBytes(32)),
		});
	}

	private CipherTools.IBlockCipher[] _ciphers;

	public CipherInfo(CipherTools.IBlockCipher[] ciphers) {
		_ciphers = ciphers;
	}

	public byte[] addCryptoHash(byte[] data) throws Exception {
		byte[] ret = new byte[data.length + 32];

		System.arraycopy(data, 0, ret, 0, data.length);
		System.arraycopy(SecurityTools.getSHA512(data), 0, ret, data.length, 16); // *** necessary SHA-2 after
		System.arraycopy(SecurityTools.cRandom.getBytes(16), 0, ret, data.length + 16, 16);

		for(CipherTools.IBlockCipher cipher : _ciphers) {
			enc(cipher, ret, data.length, data.length + 16);
			enc(cipher, ret, data.length + 16, data.length);
		}
		return ret;
	}

	public byte[] unaddCryptoHash(byte[] data) throws Exception {
		if(data.length < 32) {
			throw new IllegalArgumentException("Bad data length");
		}
		byte[] ret = Arrays.copyOf(data, data.length - 32);
		byte[] trailer = Arrays.copyOfRange(data, data.length - 32, data.length);

		for(int index = _ciphers.length - 1; 0 <= index; index--) {
			dec(_ciphers[index], trailer, 16, 0);
			dec(_ciphers[index], trailer, 0, 16);
		}
		byte[] hash = Arrays.copyOf(trailer, 16);
		byte[] reHash = Arrays.copyOf(SecurityTools.getSHA512(ret), 16);

		if(BinTools.comp_array.compare(hash, reHash) != 0) {
			throw new IllegalArgumentException("Bad hash");
		}
		return ret;
	}

	private void enc(CipherTools.IBlockCipher cipher, byte[] data, int index, int maskIndex) throws Exception {
		byte[] block = new byte[16];

		for(int c = 0; c < 16; c++) {
			block[c] = (byte)((data[index + c] & 0xff) ^ (data[maskIndex + c] & 0xff));
		}
		cipher.encryptBlock(block, 0, data, index);
	}

	private void dec(IBlockCipher cipher, byte[] data, int index, int maskIndex) throws Exception {
		byte[] block = new byte[16];

		cipher.decryptBlock(data, index, block, 0);

		for(int c = 0; c < 16; c++) {
			data[index + c] = (byte)((block[c] & 0xff) ^ (data[maskIndex + c] & 0xff));
		}
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
