package wb.t20200528;

import java.util.List;

import charlotte.tools.IntTools;
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
			if(dest[i] != i) {
				throw null; // souteigai !!!
			}
		}
	}
}
