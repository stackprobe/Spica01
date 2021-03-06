package wb.t20190208;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static List<String> _test01_dest = new ArrayList<String>();

	private static void test01() throws Exception {
		for(int c = 0; c < 100; c++) {
			test01_a(test01_mkStr());
		}

		for(int c = 0; c < 100; c++) {
			test01_a(test01_mkStr2());
		}

		for(int c = 0; c < 100; c++) {
			test01_a(test01_mkStr3());
		}

		test01_a("");
		test01_a("abc");
		test01_a("いろはにほへと"); // orig: test01_a("\u3044\u308d\u306f\u306b\u307b\u3078\u3068");
		test01_a("123456");
		test01_a("漢字"); // orig: test01_a("\u6f22\u5b57");

		FileTools.writeAllLines("C:/temp/1.txt", _test01_dest, StringTools.CHARSET_UTF8);
	}

	private static void test01_a(String s) throws Exception {
		s = JString2.filter(s);
		s = "\"" + s + "\"";

		_test01_dest.add(s);
	}

	private static String test01_mkStr() {
		char[] c = new char[SecurityTools.cRandom.getInt(100)];

		for(int i = 0; i < c.length; i++) {
			c[i] = (char)SecurityTools.cRandom.getInt(0x10000);
		}
		return new String(c);
	}

	private static String test01_mkStr2() {
		char[] c = new char[SecurityTools.cRandom.getInt(100)];

		for(int i = 0; i < c.length; i++) {
			c[i] = (char)SecurityTools.cRandom.getInt(0x100);
		}
		return new String(c);
	}

	private static String test01_mkStr3() {
		char[] c = new char[SecurityTools.cRandom.getInt(100)];

		for(int i = 0; i < c.length; i++) {
			c[i] = (char)SecurityTools.cRandom.getRangeInt(0x09, 0x7e);
		}
		return new String(c);
	}
}
