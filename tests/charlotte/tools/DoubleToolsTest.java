package tests.charlotte.tools;

import charlotte.tools.DoubleTools;

public class DoubleToolsTest {
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
		for(int c = -30; c <= 30; c++) {
			double d = c / 10.0;

			System.out.println(d + " => " + DoubleTools.toInt(d));
		}
	}
}
