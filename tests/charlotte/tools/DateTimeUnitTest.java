package tests.charlotte.tools;

import charlotte.tools.DateTimeToSec;
import charlotte.tools.DateTimeUnit;

public class DateTimeUnitTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		System.out.println("\t" + DateTimeUnit.now());
		System.out.println("\t" + DateTimeUnit.fromDateTime(DateTimeToSec.Now.getDateTime()));

		for(long i = 1; i <= 7; i++) {
			System.out.println("\t" + DateTimeUnit.fromDateTime(DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() + i * 86400L)));
		}
	}

	private static void test02() {
		System.out.println("\t" + DateTimeUnit.fromDateTime(1L));
		System.out.println("\t" + DateTimeUnit.fromDateTime(0L));
		System.out.println("\t" + DateTimeUnit.fromDateTime(-1L));
		System.out.println("\t" + DateTimeUnit.fromDateTime(Long.MAX_VALUE));
		System.out.println("\t" + DateTimeUnit.fromDateTime(Long.MIN_VALUE));
		System.out.println("\t" + DateTimeUnit.fromDateTime(1234567890123456789L));
		System.out.println("\t" + DateTimeUnit.fromDateTime(-1234567890123456789L));
	}
}
