package tests.charlotte.tools;

import charlotte.tools.IntTools;
import charlotte.tools.SecurityTools;

public class RandomUnitTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		int[] ns = IntTools.sequence(10);

		for(int n : ns) {
			System.out.print(" " + n);
		}
		System.out.println("");

		SecurityTools.cRandom.shuffle(IntTools.asList(ns));

		for(int n : ns) {
			System.out.print(" " + n);
		}
		System.out.println("");
	}

	private static void test02() {
		for(int c = 0; c < 1000; c++) {
			System.out.println(String.join(", ",
					"" + SecurityTools.cRandom.getShort(),
					"" + SecurityTools.cRandom.getInt24(),
					"" + SecurityTools.cRandom.getInt(),
					"" + SecurityTools.cRandom.getLong()
					));
		}
	}
}
