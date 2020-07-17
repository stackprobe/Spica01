package tests.charlotte.tools;

import charlotte.tools.I4Color;

public class I4ColorTest {
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
		System.out.println("80ff407f ==> " + new I4Color(128, 255, 64, 127));
	}
}
