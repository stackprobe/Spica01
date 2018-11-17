package tests.charlotte.tools;

import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.IArrayTools;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class ArrayToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			//test03();
			test04();
			//test04_2();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		String[] strs = ArrayTools.<Integer, String>select(
				ArrayTools.<String, Integer>select(
						new String[] { "123", "456", "789" },
						str -> Integer.parseInt(str)
						)
						.toArray(new Integer[0]),
				value -> value.toString()
				)
				.toArray(new String[0]);

		for(String str : strs) {
			System.out.println(str);
		}
	}

	private static void test02() {
		String[] strs = ArrayTools.distinct("A:B:C:A:A:A:B:B:C:A:B:B:C:C:C:A:B:C".split("[:]"), StringTools.comp).toArray(new String[0]);

		for(String str : strs) {
			System.out.println(str);
		}
	}

	private static void test03() {
		ArrayTools.merge(
				"A:A:A:B:B:C:A:B:B:C:C:C".split("[:]"),
				"C:C:C:B:B:A:C:B:B:A:A:A".split("[:]"),
				null,
				null,
				null,
				null,
				StringTools.comp
				);
	}

	private static void test04() {
		test04_a(100, 100, 100);
		test04_a(100, 100, 10000);
		test04_a(100, 100, 1000000);
		test04_a(100, 10000, 100);
		test04_a(100, 10000, 10000);
		test04_a(100, 10000, 1000000);
		test04_a(30, 300000, 100);
		test04_a(30, 300000, 10000);
		test04_a(30, 300000, 1000000);
		test04_a(10, 1000000, 100);
		test04_a(10, 1000000, 10000);
		test04_a(10, 1000000, 1000000);
		test04_a(10, 1000000, 2100000000);
	}

	private static void test04_2() {
		for(int length = 0; length < 100; length++) {
			for(int modulo = 1; modulo < 100; modulo++) {
				test04_a(100, length, modulo);
			}
		}
	}

	private static class Item {
		public int value;
	}

	private static void test04_a(int test_num, int length, int modulo) {
		System.out.println(test_num + ", " + length + ", " + modulo);

		long ta = 0L;
		long tb = 0L;

		for(int test_cnt = 0; test_cnt < test_num; test_cnt++) {
			Item[] items = new Item[length];

			for(int index = 0; index < length; index++) {
				Item item = new Item();
				item.value = SecurityTools.cRandom.getInt(modulo);
				items[index] = item;
			}
			//Item[] items_a = Arrays.copyOf(items, items.length);
			//Item[] items_b = Arrays.copyOf(items, items.length);

			List<Item> items_a = ArrayTools.toList(items);
			List<Item> items_b = IArrayTools.asList(items);

			if(ListTools.comp(items_a, items_b, (a, b) -> a.value - b.value) != 0) throw null; // test

			long t1 = System.currentTimeMillis();
			//Arrays.sort(items_a, (a, b) -> a.value - b.value);
			items_a.sort((a, b) -> a.value - b.value);
			long t2 = System.currentTimeMillis();
			//Arrays.sort(items_b, (a, b) -> a.value - b.value);
			items_b.sort((a, b) -> a.value - b.value);
			long t3 = System.currentTimeMillis();

			if(ListTools.comp(items_a, items_b, (a, b) -> a.value - b.value) != 0) throw null; // test

			//System.out.println("t_a: " + (t2 - t1));
			//System.out.println("t_b: " + (t3 - t2));

			ta += (t2 - t1);
			tb += (t3 - t2);
		}

		System.out.println("t_a: " + (ta * 1.0 / test_num));
		System.out.println("t_b: " + (tb * 1.0 / test_num));
	}
}
