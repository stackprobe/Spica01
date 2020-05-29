package wb.t20200528;

import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;

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
		test01_a(1);
		test01_a(2);
		test01_a(3);
		test01_a(4);
		test01_a(5);
		test01_a(6);
		test01_a(7);
		test01_a(8);
		test01_a(9);
		test01_a(10);
		test01_a(30);
		test01_a(100);
		test01_a(300);
		test01_a(1000);
		test01_a(3000);
		test01_a(10000);
	}

	private static void test01_a(int valueNum) {
		for(int c = 0; c < 100; c++) {
			test01_b(valueNum);
			test01_c(valueNum);
			test01_d(valueNum);
		}
	}

	private static void test01_b(int valueNum) {
		List<Integer> values = IntTools.asList(IntTools.sequence(valueNum));
		SecurityTools.cRandom.shuffle(values);

		List<Integer> indexes = IntTools.asList(IntTools.sequence(valueNum));
		indexes.sort((a, b) -> values.get(a) - values.get(b));

		int[] dest = new int[valueNum];
		for(int i = 0; i < valueNum; i++) {
			dest[i] = values.get(indexes.get(i));
		}

		for(int i = 0; i < valueNum; i++) {
			//System.out.println(values.get(i)); // test
			if(dest[i] != i) {
				throw null; // souteigai !!!
			}
		}
		//System.out.println(""); // test
	}

	private static void test01_c(int valueNum) {
		List<Integer> values = IntTools.asList(IntTools.sequence(valueNum));
		SecurityTools.cRandom.shuffle(values);

		List<Integer> indexes = IntTools.asList(IntTools.sequence(valueNum));
		indexes.sort((a, b) -> values.get(a) - values.get(b));

		for(int i = 0; i < valueNum; i++) {
			if(indexes.get(i) != -1) {
				int xi = i;

				for(; ; ) {
					int yi = indexes.get(xi);

					indexes.set(xi, -1);

					if(yi == i) {
						break;
					}
					ListTools.swap(values, xi, yi);
					xi = yi;
				}
			}
		}

		for(int i = 0; i < valueNum; i++) {
			//System.out.println(values.get(i)); // test
			if(values.get(i) != i) {
				throw null; // souteigai !!!
			}
		}
		//System.out.println(""); // test
	}

	private static void test01_d(int valueNum) {
		List<Integer> values = IntTools.asList(IntTools.sequence(valueNum));
		SecurityTools.cRandom.shuffle(values);

		List<Integer> indexes = IntTools.asList(IntTools.sequence(valueNum));
		indexes.sort((a, b) -> values.get(a) - values.get(b));

		for(int i = 0; i < valueNum; i++) {
			if(indexes.get(i) != -1) {
				int escVal = values.get(i);
				int xi = i;

				for(; ; ) {
					int yi = indexes.get(xi);

					indexes.set(xi, -1);

					if(yi == i) {
						break;
					}
					values.set(xi, values.get(yi));
					xi = yi;
				}
				values.set(xi, escVal);
			}
		}

		for(int i = 0; i < valueNum; i++) {
			//System.out.println(values.get(i)); // test
			if(values.get(i) != i) {
				throw null; // souteigai !!!
			}
		}
		//System.out.println(""); // test
	}
}
