package wb.t20190204.c;

import java.util.Arrays;

import charlotte.tools.BinTools;
import charlotte.tools.CipherTools;
import charlotte.tools.SecurityTools;

public class CipherInfo implements AutoCloseable {
	public CipherInfo() throws Exception {
		this(new CipherTools.AES(SecurityTools.cRandom.getBytes(32)));
	}

	private CipherTools.IBlockCipher _cipher;

	public CipherInfo(CipherTools.IBlockCipher cipher) {
		_cipher = cipher;
	}

	private byte[] _nonceCounter = new byte[8];

	public byte[] encrypt(byte[] data) throws Exception {
		byte[] ret = new byte[data.length + 24];

		/*
		byte[] nonce = SecurityTools.cRandom.getBytes(8);
		/*/
		byte[] nonce = Arrays.copyOf(_nonceCounter, 8);
		inc(_nonceCounter, 7);
		//*/

		System.arraycopy(data, 0, ret, 0, data.length);
		System.arraycopy(SecurityTools.getMD5(data), 0, ret, data.length, 16); // 任意のビットを反転できるのでMD5じゃマズいような... // orig: System.arraycopy(SecurityTools.getMD5(data), 0, ret, data.length, 16); // \u4efb\u610f\u306e\u30d3\u30c3\u30c8\u3092\u53cd\u8ee2\u3067\u304d\u308b\u306e\u3067MD5\u3058\u3083\u30de\u30ba\u3044\u3088\u3046\u306a...
		System.arraycopy(nonce, 0, ret, data.length + 16, 8);

		ctr(ret, data.length + 16, nonce);

		return ret;
	}

	public byte[] decrypt(byte[] data) throws Exception {
		if(data.length < 24) {
			throw new IllegalArgumentException("Bad data length");
		}
		byte[] ret = Arrays.copyOf(data, data.length - 8);
		byte[] nonce = Arrays.copyOfRange(data, data.length - 8, data.length);

		ctr(ret, data.length - 8, nonce);

		byte[] hash = Arrays.copyOfRange(ret, data.length - 24, data.length - 8);
		ret = Arrays.copyOf(ret, data.length - 24);
		byte[] reHash = SecurityTools.getMD5(ret);

		if(BinTools.comp_array.compare(hash, reHash) != 0) {
			throw new IllegalArgumentException("Bad hash");
		}
		return ret;
	}

	private void ctr(byte[] data, int length, byte[] nonce) throws Exception {
		byte[] counter = Arrays.copyOf(nonce, 16);
		byte[] mask = new byte[16];
		int index;

		_cipher.encryptBlock(counter, mask);

		for(index = 0; index + 16 <= length; index += 16) {
			for(int c = 0; c < 16; c++) {
				data[index + c] ^= mask[c];
			}
			inc(counter, 15);
			_cipher.encryptBlock(counter, mask);
		}
		for(int c = 0; index + c < length; index++) {
			data[index + c] ^= mask[c];
		}
	}

	private void inc(byte[] counter, int index) {
		for(; ; ) {
			if((counter[index] & 0xff) < 0xff) {
				counter[index]++;
				break;
			}
			counter[index--] = 0x00;
		}
	}

	@Override
	public void close() throws Exception {
		if(_cipher != null) {
			_cipher.close();
			_cipher = null;
		}
	}
}
