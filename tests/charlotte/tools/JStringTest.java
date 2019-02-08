package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.JString;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class JStringTest {
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

		test01_a("");
		test01_a("abc");
		test01_a("いろはにほへと");
		test01_a("123456");
		test01_a("漢字");

		FileTools.writeAllLines("C:/temp/1.txt", _test01_dest, StringTools.CHARSET_UTF8);
		_test01_dest.clear();

		for(int c = 0; c < 100; c++) {
			test01_b(SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(100)));
		}

		FileTools.writeAllLines("C:/temp/2.txt", _test01_dest, StringTools.CHARSET_UTF8);
	}

	private static void test01_a(String s) throws Exception {
		s = JString.toJString(s, true, true, true, true);
		s = "\"" + s + "\"";

		_test01_dest.add(s);
	}

	private static void test01_b(byte[] b) throws Exception {
		String s = JString.toJString(b, true, true, true, true);
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
}
