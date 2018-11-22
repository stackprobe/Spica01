package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.HugeQueue;
import charlotte.tools.SecurityTools;

public class HugeQueueTest {
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
		try(HugeQueue hq = new HugeQueue()) {
			List<byte[]> list = new ArrayList<byte[]>();

			hq.FILE_SIZE_LIMIT = 1000L;

			if(hq.hasElements()) {
				throw null; // bugged !!!
			}

			for(int c = 0; c < 30000; c++) {
				System.out.println(hq.hasElements() + ", " + list.size()); // test

				if(1 <= list.size() && SecurityTools.cRandom.getReal() < 0.5) {
					byte[] v1 = hq.dequeue();
					byte[] v2 = list.remove(0);

					//System.out.println("> " + v1 + ", " + v2); // test

					if(BinTools.comp_array.compare(v1, v2) != 0) {
						throw null; // bugged !!!
					}
				}
				else {
					byte[] v = SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(100));

					//System.out.println("< " + v); // test

					hq.enqueue(v);
					list.add(v);
				}

				if(hq.hasElements() != !list.isEmpty()) {
					throw null; // bugged !!!
				}
			}
		}
	}
}
