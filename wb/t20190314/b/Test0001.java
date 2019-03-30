package wb.t20190314.b;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IArray;
import charlotte.tools.IArrays;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();
			//test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		IArray<String> oneToFive = IArrays.wrap("1:2:3:4:5".split("[:]"));

		SubArrayTrain<String> oneToFive_x3 = new SubArrayTrain<String>()
				.addOne("{")
				.add(new SubArray<String>(oneToFive, 0, 3))
				.add(new SubArray<String>(oneToFive, 1, 4))
				.add(new SubArray<String>(oneToFive, 2, 5))
				.addOne("}");

		for(String s : new SubArrayTrain<String>()
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

	private static void test02() {
		for(int testCnt = 0; testCnt < 100; testCnt++) {
			System.out.println("" + testCnt);

			test02_once();
		}
	}

	private static void test02_once() {
		List<String> aa = new ArrayList<String>();
		SubArrayTrain<String> sat = new SubArrayTrain<String>();

		for(int c = SecurityTools.cRandom.getInt(1000); 0 < c; c--) {
			List<String> ss = new ArrayList<String>();

			for(int d = SecurityTools.cRandom.getInt(1000); 0 < d; d--) {
				String token = SecurityTools.makePassword();

				aa.add(token);
				ss.add(token);
			}
			sat.add(IArrays.wrap(ss));

			System.out.print("\t" + ss.size()); // test
		}
		System.out.println(""); // test

		List<String> bb = IArrays.asList(sat.toArray());

		if(ListTools.comp(aa, bb, StringTools.comp) != 0) {
			throw null; // bugged !!!
		}
	}
}
