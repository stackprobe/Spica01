package tests.charlotte.tools;

import charlotte.tools.DateTimeToSec;

public class DateTimeToSecTest {
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
		System.out.println("" + DateTimeToSec.Now.getDateTime());
	}
}
