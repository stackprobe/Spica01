package tests.charlotte.tools;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;

public class BinToolsTest {
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
		for(int c = 0; c < 3000; c++) {
			byte[] src = SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(1000));
			String mid = BinTools.Base64B.toString(src);
			byte[] ans = BinTools.Base64B.toBytes(mid);

			System.out.println(src.length + ", " + mid.length() + ", " + ans.length); // test

			if(BinTools.comp_array.compare(src, ans) != 0) {
				throw null; // bugged !!!
			}
		}
	}

	private static void test02() {
		byte[] src = SecurityTools.cRandom.getBytes(100000000); // 100 MB

		System.out.println("go");

		long t1 = System.currentTimeMillis();
		String mid = BinTools.Base64B.toString(src);
		long t2 = System.currentTimeMillis();
		byte[] ans = BinTools.Base64B.toBytes(mid);
		long t3 = System.currentTimeMillis();

		System.out.println(src.length + ", " + mid.length() + ", " + ans.length); // test

		System.out.println("t1: " + (t2 - t1));
		System.out.println("t2: " + (t3 - t2));
	}
}
