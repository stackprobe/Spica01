package wb.t20190108;

import charlotte.tools.DateTimeToSec;

public class Test0001 {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			test03();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		System.out.println("\t" + DateTimeInfo.now());
		System.out.println("\t" + DateTimeInfo.fromDateTime(DateTimeToSec.Now.getDateTime()));

		for(long i = 1; i <= 7; i++) {
			System.out.println("\t" + DateTimeInfo.fromDateTime(DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() + i * 86400L)));
		}
	}

	private static void test02() {
		System.out.println("\t" + DateTimeInfo.fromDateTime(1L));
		System.out.println("\t" + DateTimeInfo.fromDateTime(0L));
		System.out.println("\t" + DateTimeInfo.fromDateTime(-1L));
		System.out.println("\t" + DateTimeInfo.fromDateTime(Long.MAX_VALUE));
		System.out.println("\t" + DateTimeInfo.fromDateTime(Long.MIN_VALUE));
		System.out.println("\t" + DateTimeInfo.fromDateTime(1234567890123456789L));
		System.out.println("\t" + DateTimeInfo.fromDateTime(-1234567890123456789L));
	}

	private static void test03() {
		System.out.println(DateTimeInfo.fromString("2019/1/9 (\u6c34) 13:18:30"));
	}
}
