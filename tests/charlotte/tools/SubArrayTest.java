package tests.charlotte.tools;

import charlotte.tools.IArray;
import charlotte.tools.IArrays;
import charlotte.tools.SubArray;

public class SubArrayTest {
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
		IArray<String> a = IArrays.wrap("0:1:2:3:4:5:6:7:8:9".split("[:]"));
		IArray<String> b = new SubArray<String>(a, 1, 9);
		IArray<String> c = new SubArray<String>(b, 1, 7);
		IArray<String> d = new SubArray<String>(c, 1, 5);
		IArray<String> e = new SubArray<String>(d, 1, 3);

		String ee = String.join(":", IArrays.asList(e));

		System.out.println("ee: " + ee);

		if(ee.equals("4:5") == false) {
			throw null; // bugged !!!
		}
	}
}
