package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import charlotte.tools.IArrays;
import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.SortableArray;
import charlotte.tools.Wrapper;

public class SortableArrayTest {
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
		//test01_a(() -> genAlreadySorted(10000));
		//test01_a(() -> genAlreadySorted(100000));
		test01_a(() -> genAlreadySorted(1000000));

		//test01_a(() -> genReversed(10000));
		//test01_a(() -> genReversed(100000));
		test01_a(() -> genReversed(1000000));

		System.out.println("gr.1");
		test01_a(() -> genRandom(10000, 100));
		test01_a(() -> genRandom(10000, 10000));
		test01_a(() -> genRandom(10000, 2100000000));

		System.out.println("gr.2");
		test01_a(() -> genRandom(100000, 100));
		test01_a(() -> genRandom(100000, 100000));
		test01_a(() -> genRandom(100000, 2100000000));

		System.out.println("gr.3");
		test01_a(() -> genRandom(1000000, 100));
		test01_a(() -> genRandom(1000000, 1000000));
		test01_a(() -> genRandom(1000000, 2100000000));
	}

	private static void test01_a(Supplier<List<Item>> gen) {
		double time_a = test01_b(gen, items -> items.sort(_compItem));
		double time_b = test01_b(gen, items -> new SortableArray<Item>(IArrays.wrap(items), _compItem).sort());
		double time_c = test01_b(gen, items -> new SortableArray<Item>(IArrays.wrap(items), _compItem).combSort());

		System.out.println(String.format("%f\t%f\t%f\t%f\t%f", time_a, time_b, time_c, time_b / time_a, time_c / time_a));
	}

	private static final int TEST_CNT = 10;

	private static double test01_b(Supplier<List<Item>> gen, Consumer<List<Item>> sort) {
		long totalTime = 0L;

		for(int test_cnt = 0; test_cnt < TEST_CNT; test_cnt++) {
			List<Item> items = gen.get();

			long startedTime = System.currentTimeMillis();
			sort.accept(items);
			totalTime += System.currentTimeMillis() - startedTime;

			for(int index = 1; index < items.size(); index++) {
				if(0 < _compItem.compare(items.get(index - 1), items.get(index))) {
					throw null; // bugged !!!
				}
			}
		}
		return totalTime * 1.0 / TEST_CNT;
	}

	private static class Item {
		public int value;

		public Item(int value) {
			this.value = value;
		}
	}

	private static Comparator<Item> _compItem = (a, b) -> a.value - b.value;

	private static List<Item> genAlreadySorted(int count) {
		return Wrapper.create(IntTools.sequence(count))
				.change(w -> IntTools.asList(w))
				.change(w -> w.iterator())
				.change(w -> ListTools.select(w, value -> new Item(value)))
				.get();
	}

	private static List<Item> genReversed(int count) {
		return Wrapper.create(genAlreadySorted(count))
				.accept(w -> ListTools.reverse(w))
				.get();
	}

	private static List<Item> genRandom(int count, int modulo) {
		List<Item> items = new ArrayList<Item>();

		for(int index = 0; index < count; index++) {
			items.add(new Item(SecurityTools.cRandom.getInt(modulo)));
		}
		return items;
	}
}
