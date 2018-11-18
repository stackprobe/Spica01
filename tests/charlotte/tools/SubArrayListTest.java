package tests.charlotte.tools;

import charlotte.tools.IArray;
import charlotte.tools.IArrays;
import charlotte.tools.SubArray;
import charlotte.tools.SubArrayList;

public class SubArrayListTest {
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
		IArray<String> src = IArrays.wrap("{:A:B:C:D:E:}".split("[:]"));

		for(String s : new SubArrayList<String>()
				.add(new SubArray<String>(src, 0, 3))
				.add(new SubArray<String>(src, 1, 4))
				.add(new SubArray<String>(src, 2, 5))
				.add(new SubArray<String>(src, 3, 6))
				.add(new SubArray<String>(src, 4, 7))
				) {
			System.out.print(" " + s);
		}
		System.out.println("");
	}
}
