package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.QueueUnit;
import charlotte.tools.SecurityTools;

public class QueueUnitTest {
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
		QueueUnit<Integer> q = new QueueUnit<Integer>();
		List<Integer> list = new ArrayList<Integer>();

		if(q.hasElements()) {
			throw null; // bugged !!!
		}

		for(int c = 0; c < 1000000; c++) {
			//System.out.println(q.hasElements() + ", " + list.size()); // test

			if(1 <= list.size() && SecurityTools.cRandom.getReal() < 0.5) {
				int v1 = q.dequeue();
				int v2 = list.remove(0);

				//System.out.println("> " + v1 + ", " + v2); // test

				if(v1 != v2) {
					throw null; // bugged !!!
				}
			}
			else {
				int v = SecurityTools.cRandom.getInt();

				//System.out.println("< " + v); // test

				q.enqueue(v);
				list.add(v);
			}

			if(q.hasElements() != !list.isEmpty()) {
				throw null; // bugged !!!
			}
		}
	}
}
