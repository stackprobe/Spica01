package tests.charlotte.tools;

import charlotte.tools.AttachString;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class AttachStringTest {
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
		AttachString attStr = new AttachString();

		for(int c = 0; c < 10000; c++) {
			String[] tkns = test01_mkTkns();
			String str = attStr.untokenize(tkns);
			String[] tkns2 = attStr.tokenize(str).toArray(new String[0]);

			System.out.println("tkns_: " + String.join(", ", tkns));
			System.out.println("str: " + str);
			System.out.println("tkns2: " + String.join(", ", tkns2));

			if(StringTools.comp_array.compare(tkns, tkns2) != 0) {
				throw null; // bugged !!!
			}
		}
	}

	private static String[] test01_mkTkns() {
		String[] tkns = new String[SecurityTools.cRandom.getInt(10)];

		for(int index = 0; index < tkns.length; index++) {
			tkns[index] = SecurityTools.makePassword(":$.ABC", SecurityTools.cRandom.getInt(10));
		}
		return tkns;
	}
}
