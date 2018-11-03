package charlotte.tools;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class SecurityTools {
	public static RandomUnit cRandom = new RandomUnit(new RNGRandomNumberGenerator());

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

	public static class AES implements AutoCloseable {

		// TODO
		// TODO
		// TODO

		@Override
		public void close() throws Exception {
			throw null; // TODO
		}
	}

	public static class AESRandomNumberGenerator implements RandomUnit.IRandomNumberGenerator {
		@Override
		public byte[] getBlock() {
			throw null; // TODO
		}

		@Override
		public void close() throws Exception {
			throw null; // TODO
		}
	}

	public static class RNGRandomNumberGenerator implements RandomUnit.IRandomNumberGenerator {
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
		byte[] buff = new byte[4 * 1024 * 1024];

		md.reset();

		try(FileInputStream reader = new FileInputStream(file)) {
			for(; ; ) {
				int readSize = reader.read(buff);

				if(readSize <= 0) {
					break;
				}
				md.update(buff, 0, readSize);
			}
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
