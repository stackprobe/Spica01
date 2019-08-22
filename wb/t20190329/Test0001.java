package wb.t20190329;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import charlotte.tools.IQueue;
import charlotte.tools.IQueues;
import charlotte.tools.IntTools;
import charlotte.tools.IterableTools;
import charlotte.tools.ListTools;
import charlotte.tools.QueueUnit;
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
		for(int c = 0; c < 1000000; c++) {
			List<Integer> data = test01_makeTestData();
			List<Integer> data2 = ListTools.toList(data.iterator());

			data = ListTools.toList(IQueues.iterator(sort(IQueues.wrap(data.iterator()), IntTools.comp)));
			data2.sort(IntTools.comp);

			if(ListTools.comp(data, data2, IntTools.comp) != 0) {
				throw null; // bugged !!!
			}
		}
	}

	private static List<Integer> test01_makeTestData() {
		int count = SecurityTools.cRandom.getInt(100);
		int scale = SecurityTools.cRandom.getInt(1, 3000);

		System.out.println(count + ", " + scale); // test

		List<Integer> dest = new ArrayList<Integer>();

		for(int index = 0; index < count; index++) {
			dest.add(SecurityTools.cRandom.getInt(scale));
		}
		return dest;
	}

	/**
	 * kimoi...
	 *
	 */
	private static <T> IQueue<T> sort(IQueue<T> src, Comparator<T> comp) {
		IQueue<IQueue<T>> table = new QueueUnit<IQueue<T>>();

		List<Iterator<IQueue<T>>> ites = new ArrayList<Iterator<IQueue<T>>>();

		ites.add(IterableTools.select(IQueues.iterator(src), element -> IQueues.wrap(ListTools.one((T)element).iterator())));
		ites.add(IQueues.iterator(table));

		IQueue<T> ret = IQueues.wrap(() -> (T)null);

		for(IQueue<T> next : IterableTools.once(IterableTools.linearize(ites.iterator()))) {
			if(ret == null) {
				ret = next;
			}
			else {
				IQueue<T> dest = new QueueUnit<T>();
				IQueues.merge(ret, next, dest, dest, dest, dest, comp);
				table.enqueue(dest);
				ret = null;
			}
		}
		return ret;
	}
}
