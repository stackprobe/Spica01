package tests.charlotte.tools;

import java.util.Arrays;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;

public class StringToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			//test03();
			//test04();
			//test05();
			//test06();
			test07();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		System.out.println(StringTools.HALF);
	}

	private static void test02() {
		test02a("ABC", false);
		test02a("AAA", true);
		test02a("ABCA", true);
		test02a("ABCDD123", true);
		test02a("ABCDE123", false);
	}

	private static void test02a(String str, boolean correctRet) {
		if(StringTools.hasSameChar(str) != correctRet) {
			throw null; // bugged !!!
		}
	}

	private static void test03() {
		{
			StringTools.Enclosed encl = StringTools.getEnclosedIgnoreCase(
				"<html>" +
				"<head>" +
				"<title>123</title>" +
				"</head>" +
				"<body>" +
				"<div>EnclosedByDiv_01</div>" +
				"<div>EnclosedByDiv_02</div>" +
				"<div>EnclosedByDiv_03</div>" +
				"</body>" +
				"</html>",
				"<DIV>",
				"</DIV>"
				);

			if(encl.inner().equals("EnclosedByDiv_01") == false) throw null;

			encl = StringTools.getEnclosedIgnoreCase(encl.str(), "<DIV>", "</DIV>", encl.endPtn.end);

			if(encl.inner().equals("EnclosedByDiv_02") == false) throw null;

			encl = StringTools.getEnclosedIgnoreCase(encl.endPtn.right(), "<DIV>", "</DIV>");

			if(encl.inner().equals("EnclosedByDiv_03") == false) throw null;
		}

		{
			List<StringTools.Enclosed> encls = StringTools.getAllEnclosed("<<<a>>><<<b>>><<<c>>>", "<<<", ">>>");

			if(encls.size() != 3) throw null;
			if(encls.get(0).inner().equals("a") == false) throw null;
			if(encls.get(1).inner().equals("b") == false) throw null;
			if(encls.get(2).inner().equals("c") == false) throw null;
		}
	}

	private static void test04() throws Exception {
		String charset = StringTools.CHARSET_SJIS;
		//String charset = StringTools.CHARSET_UTF8;

		FileTools.writeAllText("C:/temp/MBC_DECIMAL.txt", StringTools.MBC_DECIMAL, charset);
		FileTools.writeAllText("C:/temp/MBC_ALPHA_U.txt", StringTools.MBC_ALPHA, charset);
		FileTools.writeAllText("C:/temp/mbc_alpha_L.txt", StringTools.mbc_alpha, charset);
		FileTools.writeAllText("C:/temp/MBC_SPACE.txt", StringTools.MBC_SPACE, charset);
		FileTools.writeAllText("C:/temp/MBC_PUNCT.txt", StringTools.MBC_PUNCT, charset);
		FileTools.writeAllText("C:/temp/MBC_HIRA.txt", StringTools.MBC_HIRA, charset);
		FileTools.writeAllText("C:/temp/MBC_KANA.txt", StringTools.MBC_KANA, charset);
	}

	private static void test05() throws Exception {
		String value = "777aaa";
		String allowChars = "123456789";

		System.out.println("*1");
		if(ListTools.any(StringTools.asList(value), chr -> StringTools.contains(allowChars, chr.charValue()) == false)) {
			System.out.println("*2");
		}
		System.out.println("*3");

		// ----

		System.out.println(StringTools.setCharAt("ABCDE", 0, '$'));
		System.out.println(StringTools.setCharAt("ABCDE", 1, '$'));
		System.out.println(StringTools.setCharAt("ABCDE", 2, '$'));
		System.out.println(StringTools.setCharAt("ABCDE", 3, '$'));
		System.out.println(StringTools.setCharAt("ABCDE", 4, '$'));

		// ----

		{
			String a = StringTools.ASCII;
			String b = StringTools.getString_SJISHalfCodeRange(0x21, 0x7e);

			System.out.println("a: " + a);
			System.out.println("b: " + b);

			a = sort(a);
			b = sort(b);

			System.out.println("a: " + a);
			System.out.println("b: " + b);

			if(a.equals(b) == false) {
				throw null; // bugged !!!
			}
		}
	}

	private static String sort(String str) {
		char[] chrs = str.toCharArray();
		Arrays.sort(chrs);
		return new String(chrs);
	}

	private static void test06() {
		System.out.println(StringTools.multiReplace("ABC", "A", "ABC", "B", "BAC", "C", "CAB"));
		System.out.println(StringTools.multiReplace("[A][B][C]", "A", "(ABC)", "B", "(BAC)", "C", "(CAB)"));
	}

	private static void test07() {
		System.out.println(String.join(", ", StringTools.tokenize("A=B=C", "=", false, false, 2)));
	}
}
