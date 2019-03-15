package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class StringTools {
	public static final String EMPTY = "";
	public static final String[] EMPTY_ARRAY = new String[0];
	public static final List<String> EMPTY_LIST = IArrays.asList(EMPTY_ARRAY);

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
		getString_SJISHalfCodeRange_re(0x21, 0x2f) +
		getString_SJISHalfCodeRange_re(0x3a, 0x40) +
		getString_SJISHalfCodeRange_re(0x5b, 0x60) +
		getString_SJISHalfCodeRange_re(0x7b, 0x7e);

	public static final String ASCII = DECIMAL + ALPHA + alpha + PUNCT; // == getString_SJISHalfCodeRange_re(0x21, 0x7e)
	public static final String KANA = getString_SJISHalfCodeRange_re(0xa1, 0xdf);

	public static final String HALF = ASCII + KANA;

	public static String getString_SJISHalfCodeRange_re(int codeMin, int codeMax) {
		return RTError.get(() -> getString_SJISHalfCodeRange(codeMin, codeMax));
	}

	public static String getString_SJISHalfCodeRange(int codeMin, int codeMax) throws Exception {
		byte[] buff = new byte[codeMax - codeMin + 1];

		for(int code = codeMin; code <= codeMax; code++) {
			buff[code - codeMin] = (byte)code;
		}
		return new String(buff, CHARSET_SJIS);
	}

	public static final String MBC_DECIMAL = getString_SJISCodeRange_re(0x82, 0x4f, 0x58);
	public static final String MBC_ALPHA = getString_SJISCodeRange_re(0x82, 0x60, 0x79);
	public static final String mbc_alpha = getString_SJISCodeRange_re(0x82, 0x81, 0x9a);
	public static final String MBC_SPACE = getString_SJISCodeRange_re(0x81, 0x40, 0x40);
	public static final String MBC_PUNCT =
			getString_SJISCodeRange_re(0x81, 0x41, 0x7e) +
			getString_SJISCodeRange_re(0x81, 0x80, 0xac) +
			getString_SJISCodeRange_re(0x81, 0xb8, 0xbf) + //	集合
			getString_SJISCodeRange_re(0x81, 0xc8, 0xce) + //	論理
			getString_SJISCodeRange_re(0x81, 0xda, 0xe8) + //	数学
			getString_SJISCodeRange_re(0x81, 0xf0, 0xf7) +
			getString_SJISCodeRange_re(0x81, 0xfc, 0xfc) +
			getString_SJISCodeRange_re(0x83, 0x9f, 0xb6) + //	ギリシャ語大文字
			getString_SJISCodeRange_re(0x83, 0xbf, 0xd6) + //	ギリシャ語小文字
			getString_SJISCodeRange_re(0x84, 0x40, 0x60) + //	キリル文字大文字
			getString_SJISCodeRange_re(0x84, 0x70, 0x7e) + //	キリル文字小文字(1)
			getString_SJISCodeRange_re(0x84, 0x80, 0x91) + //	キリル文字小文字(2)
			getString_SJISCodeRange_re(0x84, 0x9f, 0xbe) + //	枠線
			getString_SJISCodeRange_re(0x87, 0x40, 0x5d) + //	機種依存文字(1)
			getString_SJISCodeRange_re(0x87, 0x5f, 0x75) + //	機種依存文字(2)
			getString_SJISCodeRange_re(0x87, 0x7e, 0x7e) + //	機種依存文字(3)
			getString_SJISCodeRange_re(0x87, 0x80, 0x9c) + //	機種依存文字(4)
			getString_SJISCodeRange_re(0xee, 0xef, 0xfc); //	機種依存文字(5)

	public static final String MBC_HIRA = getString_SJISCodeRange_re(0x82, 0x9f, 0xf1);
	public static final String MBC_KANA =
			getString_SJISCodeRange_re(0x83, 0x40, 0x7e) +
			getString_SJISCodeRange_re(0x83, 0x80, 0x96);

	private static String getString_SJISCodeRange_re(int lead, int trailMin, int trailMax) {
		return RTError.get(() -> getString_SJISCodeRange(lead, trailMin, trailMax));
	}

	private static String getString_SJISCodeRange(int lead, int trailMin, int trailMax) throws Exception {
		byte[] buff = new byte[(trailMax - trailMin + 1) * 2];

		for(int trail = trailMin; trail <= trailMax; trail++) {
			buff[(trail - trailMin) * 2 + 0] = (byte)lead;
			buff[(trail - trailMin) * 2 + 1] = (byte)trail;
		}
		return new String(buff, CHARSET_SJIS);
	}

	public static Comparator<String> comp = new Comparator<String>() {
		@Override
		public int compare(String a, String b) {
			return a.compareTo(b);
		}
	};

	public static Comparator<String> compIgnoreCase = new Comparator<String>() {
		@Override
		public int compare(String a, String b) {
			return a.compareToIgnoreCase(b);
		}
	};

	public static Comparator<String[]> comp_array = new Comparator<String[]>() {
		@Override
		public int compare(String[] a, String[] b) {
			return ArrayTools.comp(a, b, comp);
		}
	};

	public static Comparator<String[]> compIgnoreCase_array = new Comparator<String[]>() {
		@Override
		public int compare(String[] a, String[] b) {
			return ArrayTools.comp(a, b, compIgnoreCase);
		}
	};

	public static List<Character> asList(String inner) {
		return asList(inner.toCharArray());
	}

	public static List<Character> asList(char[] inner) {
		return IArrays.asList(wrap(inner));
	}

	public static IArray<Character> wrap(String inner) {
		return wrap(inner.toCharArray());
	}

	public static IArray<Character> wrap(char[] inner) {
		return new IArray<Character>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Character get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Character element) {
				inner[index] = element;
			}
		};
	}

	public static boolean startsWithIgnoreCase(String str, String ptn) {
		return str.toLowerCase().startsWith(ptn.toLowerCase());
	}

	public static boolean endsWithIgnoreCase(String str, String ptn) {
		return str.toLowerCase().endsWith(ptn.toLowerCase());
	}

	public static boolean contains(String str, char chr) {
		return str.indexOf(chr) != -1;
	}

	public static boolean containsIgnoreCase(String str, char chr) {
		return contains(str.toLowerCase(), Character.toLowerCase(chr));
	}

	public static boolean containsIgnoreCase(String str, String ptn) {
		return str.toLowerCase().contains(ptn.toLowerCase());
	}

	public static int indexOfIgnoreCase(String str, char chr) {
		return str.toLowerCase().indexOf(Character.toLowerCase(chr));
	}

	public static int indexOfIgnoreCase(String str, String ptn) {
		return str.toLowerCase().indexOf(ptn.toLowerCase());
	}

	public static boolean contains(String str, Predicate<Character> match) {
		return indexOf(str, match) != -1;
	}

	public static int indexOf(String str, Predicate<Character> match) {
		for(int index = 0; index < str.length(); index++) {
			if(match.test(str.charAt(index))) {
				return index;
			}
		}
		return -1;
	}

	public static class Island {
		public String str;
		public int start;
		public int end;

		public String left() {
			return str.substring(0, start);
		}

		public String inner() {
			return str.substring(start, end);
		}

		public int innerLength() {
			return end - start;
		}

		public String right() {
			return str.substring(end);
		}
	}

	public static Island getIsland(String str, String mid) {
		return getIsland(str, mid, 0);
	}

	public static Island getIsland(String str, String mid, int fromIndex) {
		int index = str.indexOf(mid, fromIndex);

		if(index == -1) {
			return null;
		}
		Island ret = new Island();
		ret.str = str;
		ret.start = index;
		ret.end = index + mid.length();
		return ret;
	}

	public static class Enclosed {
		public Island startPtn;
		public Island endPtn;

		public String str() {
			return startPtn.str;
		}

		public void set_str(String str) {
			startPtn.str = str;
			endPtn.str = str;
		}

		public String left() {
			return str().substring(0, startPtn.end);
		}

		public String inner() {
			return str().substring(startPtn.end, endPtn.start);
		}

		public int innerLength() {
			return endPtn.start - startPtn.end;
		}

		public String right() {
			return str().substring(endPtn.start);
		}
	}

	public static Enclosed getEnclosed(String str, String startPtn, String endPtn) {
		return getEnclosed(str, startPtn, endPtn, 0);
	}

	public static Enclosed getEnclosed(String str, String startPtn, String endPtn, int fromIndex) {
		Enclosed ret = new Enclosed();

		ret.startPtn = getIsland(str, startPtn, fromIndex);

		if(ret.startPtn == null) {
			return null;
		}
		ret.endPtn = getIsland(str, endPtn, ret.startPtn.end);

		if(ret.endPtn == null) {
			return null;
		}
		return ret;
	}

	public static Enclosed getEnclosedIgnoreCase(String str, String startPtn, String endPtn) {
		return getEnclosedIgnoreCase(str, startPtn, endPtn, 0);
	}

	public static Enclosed getEnclosedIgnoreCase(String str, String startPtn, String endPtn, int fromIndex) {
		Enclosed ret = getEnclosed(
				str.toLowerCase(),
				startPtn.toLowerCase(),
				endPtn.toLowerCase(),
				fromIndex
				);

		if(ret != null) {
			ret.set_str(str);
		}
		return ret;
	}

	public static List<Enclosed> getAllEnclosed(String str, String startPtn, String endPtn) {
		return getAllEnclosed(str, startPtn, endPtn, 0);
	}

	public static List<Enclosed> getAllEnclosed(String str, String startPtn, String endPtn, int fromIndex) {
		List<Enclosed> dest = new ArrayList<Enclosed>();

		for(; ; ) {
			Enclosed encl = getEnclosed(str, startPtn, endPtn, fromIndex);

			if(encl == null) {
				break;
			}
			dest.add(encl);
			fromIndex = encl.endPtn.end;
		}
		return dest;
	}

	public static List<Enclosed> getAllEnclosedIgnoreCase(String str, String startPtn, String endPtn) {
		return getAllEnclosedIgnoreCase(str, startPtn, endPtn, 0);
	}

	public static List<Enclosed> getAllEnclosedIgnoreCase(String str, String startPtn, String endPtn, int fromIndex) {
		List<Enclosed> ret = getAllEnclosed(
				str.toLowerCase(),
				startPtn.toLowerCase(),
				endPtn.toLowerCase(),
				fromIndex
				);

		for(Enclosed encl : ret) {
			encl.set_str(str);
		}
		return ret;
	}

	public static List<String> tokenize(String str, String delimiters) {
		return tokenize(str, delimiters, false);
	}

	public static List<String> tokenize(String str, String delimiters, boolean meaningFlag) {
		return tokenize(str, delimiters, meaningFlag, false);
	}

	public static List<String> tokenize(String str, String delimiters, boolean meaningFlag, boolean ignoreEmpty) {
		StringBuffer buff = new StringBuffer();
		List<String> tokens = new ArrayList<String>();

		for(char chr : str.toCharArray()) {
			if(contains(delimiters, chr) == meaningFlag) {
				buff.append(chr);
			}
			else {
				if(ignoreEmpty == false || buff.length() != 0) {
					tokens.add(buff.toString());
				}
				buff = new StringBuffer();
			}
		}
		if(ignoreEmpty == false || buff.length() != 0) {
			tokens.add(buff.toString());
		}
		return tokens;
	}

	public static boolean hasSameChar(String str) {
		for(int r = 1; r < str.length(); r++) {
			for(int l = 0; l < r; l++) {
				if(str.charAt(l) == str.charAt(r)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String replaceChars(String str, String rChrs, char wChr) {
		for(char rChr : rChrs.toCharArray()) {
			str = str.replace(rChr, wChr);
		}
		return str;
	}

	public static String replaceChars(String str, String rChrs, String wChrs) {
		if(rChrs.length() != wChrs.length()) {
			throw new IllegalArgumentException(rChrs + ", " + wChrs);
		}

		for(int index = 0; index < rChrs.length(); index++) {
			str = str.replace(rChrs.charAt(index), wChrs.charAt(index));
		}
		return str;
	}

	public static String replaceLoop(String str, String rPtn, String wPtn) {
		return replaceLoop(str, rPtn, wPtn, 30);
	}

	public static String replaceLoop(String str, String rPtn, String wPtn, int count) {
		while(0 < count) {
			str = str.replace(rPtn, wPtn);
			count--;
		}
		return str;
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static String antiNullOrEmpty(String str, String defval) {
		return isNullOrEmpty(str) ? defval : str;
	}

	public static String antiNullOrEmpty(String str) {
		return antiNullOrEmpty(str, "_");
	}

	public static String antiNull(String str, String defval) {
		return str == null ? defval : str;
	}

	public static String antiNull(String str) {
		return antiNull(str, "");
	}

	public static String setCharAt(String str, int index, char chr) {
		return str.substring(0, index) + chr + str.substring(index + 1);
	}

	public static String repeat(String ptn, int count) {
		StringBuffer buff = new StringBuffer(ptn.length() * count);

		while(0 < count) {
			buff.append(ptn);
			count--;
		}
		return buff.toString();
	}

	public static String escape(String str) {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			if(chr <= ' ' || chr == '$' || (0x7f <= chr && chr <= 0xff)) {
				buff.append(String.format("$%02x", chr & 0xff));
			}
			else {
				buff.append(chr);
			}
		}
		return buff.toString();
	}

	public static String unescape(String str) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < str.length(); index++) {
			char chr = str.charAt(index);

			if(chr == '$') {
				chr = (char)Integer.parseInt(str.substring(index + 1, index + 3), 16);
				index += 2;
			}
			buff.append(chr);
		}
		return buff.toString();
	}
}
