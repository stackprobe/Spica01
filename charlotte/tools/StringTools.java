package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
		getString_SJISHalfCodeRange_rt(0x21, 0x2f) +
		getString_SJISHalfCodeRange_rt(0x3a, 0x40) +
		getString_SJISHalfCodeRange_rt(0x5b, 0x60) +
		getString_SJISHalfCodeRange_rt(0x7b, 0x7e);

	public static final String ASCII = DECIMAL + ALPHA + alpha + PUNCT; // == { 0x21 ～ 0x7e }
	public static final String KANA = getString_SJISHalfCodeRange_rt(0xa1, 0xdf);

	public static final String HALF = ASCII + KANA;

	public static String getString_SJISHalfCodeRange_rt(int codeMin, int codeMax) {
		try {
			return getString_SJISHalfCodeRange(codeMin, codeMax);
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}

	public static String getString_SJISHalfCodeRange(int codeMin, int codeMax) throws Exception {
		byte[] buff = new byte[codeMax - codeMin + 1];

		for(int code = codeMin; code <= codeMax; code++) {
			buff[code - codeMin] = (byte)code;
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

	public static int indexOfIgnoreCase(String str, String ptn) {
		return str.toLowerCase().indexOf(ptn.toLowerCase());
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

	public static Enclosed[] getAllEnclosed(String str, String startPtn, String endPtn) {
		return getAllEnclosed(str, startPtn, endPtn, 0);
	}

	public static Enclosed[] getAllEnclosed(String str, String startPtn, String endPtn, int fromIndex) {
		List<Enclosed> dest = new ArrayList<Enclosed>();

		for(; ; ) {
			Enclosed encl = getEnclosed(str, startPtn, endPtn, fromIndex);

			if(encl == null) {
				break;
			}
			dest.add(encl);
			fromIndex = encl.endPtn.end;
		}
		return dest.toArray(new Enclosed[dest.size()]);
	}

	public static Enclosed[] getAllEnclosedIgnoreCase(String str, String startPtn, String endPtn) {
		return getAllEnclosedIgnoreCase(str, startPtn, endPtn, 0);
	}

	public static Enclosed[] getAllEnclosedIgnoreCase(String str, String startPtn, String endPtn, int fromIndex) {
		Enclosed[] ret = getAllEnclosed(
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

	public static String[] tokenize(String str, String delimiters) {
		return tokenize(str, delimiters, false);
	}

	public static String[] tokenize(String str, String delimiters, boolean meaningFlag) {
		return tokenize(str, delimiters, meaningFlag, false);
	}

	public static String[] tokenize(String str, String delimiters, boolean meaningFlag, boolean ignoreEmpty) {
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
		return tokens.toArray(new String[tokens.size()]);
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

	public static String replaceLoop(String str, String rPtn, String wPtn) {
		return replaceLoop(str, rPtn, wPtn, 30);
	}

	public static String replaceLoop(String str, String rPtn, String wPtn, int count) {
		while(1 <= count) {
			str = str.replace(rPtn, wPtn);
			count--;
		}
		return str;
	}

	public static boolean isLine(String line) throws Exception {
		return line.equals(asLine(line));
	}

	public static String asLine(String line) throws Exception {
		return JString.toJString(line, true, false, true, true);
	}
}
