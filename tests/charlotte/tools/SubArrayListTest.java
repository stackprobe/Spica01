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
		IArray<String> oneToFive = IArrays.wrap("1:2:3:4:5".split("[:]"));

		SubArrayList<String> oneToFive_x3 = new SubArrayList<String>()
				.addOne("{")
				.add(new SubArray<String>(oneToFive, 0, 3))
				.add(new SubArray<String>(oneToFive, 1, 4))
				.add(new SubArray<String>(oneToFive, 2, 5))
				.addOne("}");

		for(String s : new SubArrayList<String>()
				.addOne("A")
				.add(oneToFive)
				.addOne("B")
				.add(oneToFive_x3.toArray())
				.addOne("C")
				) {
			System.out.print(" " + s);
		}
		System.out.println("");
	}
}
