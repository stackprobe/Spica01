package charlotte.tools;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class SecurityTools {
	public static RandomUnit cRandom = new RandomUnit(new CryptographicallySecurePseudoRandomNumberGenerator());

	public static String makePassword(String allowChars, int length) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < length; index++) {
			buff.append(allowChars.charAt(cRandom.getInt(allowChars.length())));
		}
		return buff.toString();
	}

	public static String makePassword() {
		return makePassword(StringTools.DECIMAL + StringTools.ALPHA + StringTools.alpha, 22);
	}

	public static String makePassword_9A() {
		return makePassword(StringTools.DECIMAL + StringTools.ALPHA, 25);
	}

	public static String makePassword_9a() {
		return makePassword(StringTools.DECIMAL + StringTools.alpha, 25);
	}

	public static String makePassword_9() {
		return makePassword(StringTools.DECIMAL, 39);
	}

	public static String makePassword_A() {
		return makePassword(StringTools.ALPHA, 28);
	}

	public static String makePassword_a() {
		return makePassword(StringTools.alpha, 28);
	}

	public static class AESRandomNumberGenerator implements RandomUnit.IRandomNumberGenerator {
		private CipherTools.AES _aes;
		private byte[] _counter = new byte[16];
		private byte[] _block = new byte[16];

		public AESRandomNumberGenerator(int seed) throws Exception {
			this("" + seed);
		}

		public AESRandomNumberGenerator(String seed) throws Exception {
			this(seed.getBytes(StringTools.CHARSET_UTF8));
		}

		public AESRandomNumberGenerator(byte[] seed) throws Exception {
			byte[] hash = getSHA512(seed);
			//byte[] rawKey = new byte[16];
			//byte[] rawKey = new byte[24];
			byte[] rawKey = new byte[32];

			//System.arraycopy(hash, 0, rawKey, 0, 16);
			//System.arraycopy(hash, 0, rawKey, 0, 24);
			System.arraycopy(hash, 0, rawKey, 0, 32);

			_aes = new CipherTools.AES(rawKey);
		}

		@Override
		public byte[] getBlock() {
			return RTError.get(() -> getBlock_e());
		}

		public byte[] getBlock_e() throws Exception {
			_aes.encryptBlock(_counter, _block);

			for(int index = 0; ; index++) {
				if(_counter[index] < 0xff) {
					_counter[index]++;
					break;
				}
				_counter[index] = 0x00;
			}
			return _block;
		}

		@Override
		public void close() throws Exception {
			if(_aes != null) {
				_aes.close();
				_aes = null;
			}
		}
	}

	public static class CryptographicallySecurePseudoRandomNumberGenerator implements RandomUnit.IRandomNumberGenerator {
		private SecureRandom _random = new SecureRandom();
		private byte[] _cache = new byte[4096];

		@Override
		public byte[] getBlock() {
			_random.nextBytes(_cache);
			return _cache;
		}

		@Override
		public void close() throws Exception {
			// noop
		}
	}

	private static final String ALGORITHM_SHA512 = "SHA-512";
	private static final String ALGORITHM_MD5 = "MD5";

	private static MessageDigest getMessageDigest(String algorithm) throws Exception {
		return MessageDigest.getInstance(algorithm);
	}

	private static byte[] getDigest(MessageDigest md, byte[] src) {
		return md.digest(src);
	}

	private static byte[] getDigestFile(MessageDigest md, String file) throws Exception {
		md.reset();

		try(FileInputStream reader = new FileInputStream(file)) {
			FileTools.readToEnd(reader, (data, offset, length) -> md.update(data, offset, length));
		}
		return md.digest();
	}

	public static byte[] getSHA512(byte[] src) throws Exception {
		return getDigest(getMessageDigest(ALGORITHM_SHA512), src);
	}

	public static byte[] getSHA512File(String file) throws Exception {
		return getDigestFile(getMessageDigest(ALGORITHM_SHA512), file);
	}

	public static byte[] getMD5(byte[] src) throws Exception {
		return getDigest(getMessageDigest(ALGORITHM_MD5), src);
	}

	public static byte[] getMD5File(String file) throws Exception {
		return getDigestFile(getMessageDigest(ALGORITHM_MD5), file);
	}

	public static String toFairIdent(String ident) throws Exception {
		if(isFairIdent(ident) == false) {
			ident = BinTools.Hex.toString(BinTools.getSubBytes(SecurityTools.getSHA512(ident.getBytes(StringTools.CHARSET_UTF8)), 0, 16));
		}
		return ident;
	}

	private static boolean isFairIdent(String ident) {
		String fmt = ident;

		fmt = StringTools.replaceChars(fmt, StringTools.DECIMAL + StringTools.alpha + "-{}", '9');
		fmt = StringTools.replaceLoop(fmt, "99", "9");

		return fmt.equals("9") && ident.length() <= 38;
	}
}
