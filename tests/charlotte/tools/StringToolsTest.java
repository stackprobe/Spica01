package tests.charlotte.tools;

import charlotte.tools.StringTools;

public class StringToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			test03();

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
			StringTools.Enclosed[] encls = StringTools.getAllEnclosed("<<<a>>><<<b>>><<<c>>>", "<<<", ">>>");

			if (encls.length != 3) throw null;
			if (encls[0].inner().equals("a") == false) throw null;
			if (encls[1].inner().equals("b") == false) throw null;
			if (encls[2].inner().equals("c") == false) throw null;
		}
	}
}
