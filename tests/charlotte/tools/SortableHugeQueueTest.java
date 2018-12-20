package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.HugeQueue;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.SortableHugeQueue;

public class SortableHugeQueueTest {
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

	private static void test01() throws Exception {
		for(int testCount = 0; testCount < 10; testCount++) {
			System.out.println("1." + testCount); // test

			test01_a(20, 0);
		}
		for(int testCount = 0; testCount < 100; testCount++) {
			System.out.println("2." + testCount); // test

			test01_a(10, 10);
		}
	}

	private static void test01_a(int prm_m, int prm_c) throws Exception {
		try(HugeQueue hq = new HugeQueue()) {
			List<byte[]> testList = new ArrayList<byte[]>();

			for(int count = SecurityTools.cRandom.getInt(1000); 0 <= count; count--) {
				byte[] value = SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(prm_m) + prm_c);
				//byte[] value = SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(10) + 10);
				//byte[] value = SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(20));

				hq.enqueue(value);
				testList.add(value);
			}

			// ---- sort 1 ----

			{
				SortableHugeQueue shq = new SortableHugeQueue(hq, BinTools.comp_array);

				shq.LOAD_SIZE_LIMIT = 100;
				shq.LOAD_COUNT_LIMIT = 10;
				shq.FILE_SIZE_LIMIT = 300;

				shq.sort();
			}

			// ---- sort 2 ----

			testList.sort(BinTools.comp_array);

			// ----

			List<byte[]> testList2 = new ArrayList<byte[]>();

			while(hq.hasElements()) {
				testList2.add(hq.dequeue());
			}

			if(ListTools.comp(testList, testList2, BinTools.comp_array) != 0) {
				throw null; // bugged !!!
			}
		}
	}
}
