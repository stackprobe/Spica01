package tests.charlotte.tools;

import charlotte.tools.EscapeString;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class EscapeStringTest {
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

	private static void test01() {
		EscapeString es = new EscapeString();

		for(int c = 0; c < 10000; c++) {
			String str = test01_mkStr();
			String enc = es.encode(str);
			String dec = es.decode(enc);

			System.out.println("str: " + test01_forPrint(str));
			System.out.println("enc: " + test01_forPrint(enc));
			System.out.println("dec: " + test01_forPrint(dec));

			if(StringTools.comp.compare(str, dec) != 0) {
				throw null; // bugged !!!
			}
		}
	}

	private static String test01_mkStr() {
		return SecurityTools.makePassword("\t\r\n $trnsABCDEF", SecurityTools.cRandom.getInt(30));
	}

	private static String test01_forPrint(String str) {
		str = str.replace("\t", "[TAB]");
		str = str.replace("\r", "[CR]");
		str = str.replace("\n", "[LF]");

		return "'" + str + "'";
	}
}
