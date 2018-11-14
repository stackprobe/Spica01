package tests;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Test0001 {
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
		String[] arr = new String[] { "AAA", "BBB", "CCC" };
		List<String> list = Arrays.asList(arr);

		//list.add("DDD"); //	例外 -- 追加は出来ない。
		//list.add("EEE");

		//list.remove(2); //	例外 -- 削除も出来ない。

		arr = list.toArray(new String[list.size()]);

		System.out.println(String.join(", ", arr));
	}

	private static void test02() {
		BigDecimal a = new BigDecimal("256");
		BigDecimal b = new BigDecimal("62");

		for(int aLen = 1; aLen <= 100; aLen++) {
			int bLen = getBLen(a, b);

			System.out.println(bLen + " / " + aLen + " = " + ((double)bLen / aLen));

			a = a.multiply(new BigDecimal("256"));
		}
	}

	private static int getBLen(BigDecimal a, BigDecimal b) {
		for(int bLen = 1; ; bLen++) {
			if(a.compareTo(b) <= 0) {
				return bLen;
			}
			b = b.multiply(new BigDecimal("62"));
		}
	}
}
