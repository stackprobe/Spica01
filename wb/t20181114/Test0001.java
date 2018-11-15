package wb.t20181114;

import java.math.BigDecimal;

public class Test0001 {
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
