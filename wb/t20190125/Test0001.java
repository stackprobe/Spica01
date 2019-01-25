package wb.t20190125;

import charlotte.tools.IntTools;
import charlotte.tools.LongTools;
import charlotte.tools.SecurityTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();
			test02();
			test03();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		for(int c = 0; c < 1000; c++) {
			test01_a(SecurityTools.cRandom.getLong(LongTools.IMAX_64), SecurityTools.cRandom.getLong(LongTools.IMAX_64));
		}
	}

	private static void test01_a(long a, long b) {
		System.out.println("" + a);
		System.out.println("" + b);

		IUInt xa = new XDec(5);
		IUInt xb = new XDec(5);

		xa.set("" + a);
		xb.set("" + b);

		IUInt xc = xa.add(xb)[0];

		String ans1 = xc.get();
		String ans2 = "" + (a + b);

		System.out.println(ans1);
		System.out.println(ans2);

		if(ans1.equals(ans2) == false) {
			throw null; // bugged !!!
		}
	}

	private static void test02() {
		for(int c = 0; c < 1000; c++) {
			test02_a(SecurityTools.cRandom.getLong(LongTools.IMAX_64), SecurityTools.cRandom.getLong(LongTools.IMAX_64));
		}
	}

	private static void test02_a(long a, long b) {
		System.out.println("" + a);
		System.out.println("" + b);

		IUInt xa = new XDec(5);
		IUInt xb = new XDec(5);

		xa.set("" + a);
		xb.set("" + b);

		IUInt xc = xa.sub(xb)[0];

		String ans1 = xc.get();
		String ans2 = "" + (a - b);

		System.out.println(ans1);
		System.out.println(ans2);

		if(a < b) {
			// todo ???
		}
		else {
			if(ans1.equals(ans2) == false) {
				throw null; // bugged !!!
			}
		}
	}

	private static void test03() {
		for(int c = 0; c < 1000; c++) {
			test03_a(SecurityTools.cRandom.getLong((long)IntTools.IMAX), SecurityTools.cRandom.getLong((long)IntTools.IMAX));
		}
	}

	private static void test03_a(long a, long b) {
		System.out.println("" + a);
		System.out.println("" + b);

		IUInt xa = new XDec(5);
		IUInt xb = new XDec(5);

		xa.set("" + a);
		xb.set("" + b);

		IUInt xc = xa.mul(xb)[0];

		String ans1 = xc.get();
		String ans2 = "" + (a * b);

		System.out.println(ans1);
		System.out.println(ans2);

		if(ans1.equals(ans2) == false) {
			throw null; // bugged !!!
		}
	}
}
