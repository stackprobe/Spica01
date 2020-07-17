package tests.charlotte.tools;

import charlotte.tools.I3Color;

public class I3ColorTest {
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
		System.out.println("80ff40 ==> " + new I3Color(128, 255, 64));
	}
}
