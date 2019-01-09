package tests.charlotte.tools;

import charlotte.tools.DateTimeUnit;

public class DateTimeUnitTest {
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
		test01_a("20190109");
		test01_a("20190109132209");

		test01_a("2019/1/9");
		test01_a("2019/01/09");
		test01_a("2019/01/09 13:22:09");
		test01_a("2019/01/09 (水) 13:22:09");

		test01_a("2019年1月9日");
		test01_a("2019年01月09日");
		test01_a("2019年01月09日 13時22分09秒");
		test01_a("2019年01月09日 (水) 13時22分09秒");
	}

	private static void test01_a(String str) {
		System.out.println(str + " -> " + DateTimeUnit.fromString(str));
	}
}
