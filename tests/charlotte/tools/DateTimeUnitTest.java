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
		test01_a("2019/01/09 (\u6c34) 13:22:09");

		test01_a("2019\u5e741\u67089\u65e5");
		test01_a("2019\u5e7401\u670809\u65e5");
		test01_a("2019\u5e7401\u670809\u65e5 13\u664222\u520609\u79d2");
		test01_a("2019\u5e7401\u670809\u65e5 (\u6c34) 13\u664222\u520609\u79d2");
	}

	private static void test01_a(String str) {
		System.out.println(str + " -> " + DateTimeUnit.fromString(str));
	}
}
