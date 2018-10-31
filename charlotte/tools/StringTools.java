package charlotte.tools;

public class StringTools {
	public static final String CHARSET_ASCII = "US-ASCII";
	public static final String CHARSET_SJIS = "MS932";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_UTF16 = "UTF-16";
	public static final String CHARSET_UTF16BE = "UnicodeBigUnmarked";
	public static final String CHARSET_UTF16LE = "UnicodeLittleUnmarked";
	public static final String CHARSET_UTF32 = "UTF-32";

	public static final String BINADECIMAL = "01";
	public static final String OCTODECIMAL = "012234567";
	public static final String DECIMAL = "0123456789";
	public static final String HEXADECIMAL = "0123456789ABCDEF";
	public static final String hexadecimal = "0123456789abcdef";

	public static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String alpha = "abcdefghijklmnopqrstuvwxyz";
	public static final String PUNCT =
		getString_SJISHalfCodeRange(0x21, 0x2f) +
		getString_SJISHalfCodeRange(0x3a, 0x40) +
		getString_SJISHalfCodeRange(0x5b, 0x60) +
		getString_SJISHalfCodeRange(0x7b, 0x7e);

	public static final String ASCII = DECIMAL + ALPHA + alpha + PUNCT; // == { 0x21 ～ 0x7e }
	public static final String KANA = getString_SJISHalfCodeRange(0xa1, 0xdf);

	public static final String HALF = ASCII + KANA;

	public static String getString_SJISHalfCodeRange(int codeMin, int codeMax) {
		try {
			byte[] buff = new byte[codeMax - codeMin + 1];

			for(int code = codeMin; code <= codeMax; code++) {
				buff[code - codeMin] = (byte)code;
			}
			return new String(buff, CHARSET_SJIS);
		}
		catch(Throwable e) {
			throw ExtraTools.re(e);
		}
	}
}
