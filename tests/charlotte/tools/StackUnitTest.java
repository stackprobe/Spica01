package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IStack;
import charlotte.tools.SecurityTools;
import charlotte.tools.StackUnit;

public class StackUnitTest {
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
		IStack<Integer> k = new StackUnit<Integer>();
		List<Integer> list = new ArrayList<Integer>();

		if(k.hasElements()) {
			throw null; // bugged !!!
		}

		for(int c = 0; c < 1000000; c++) {
			//System.out.println(q.hasElements() + ", " + list.size()); // test

			if(1 <= list.size() && SecurityTools.cRandom.getReal() < 0.5) {
				int v1 = k.pop();
				int v2 = list.remove(list.size() - 1);

				//System.out.println("> " + v1 + ", " + v2); // test

				if(v1 != v2) {
					throw null; // bugged !!!
				}
			}
			else {
				int v = SecurityTools.cRandom.getInt();

				//System.out.println("< " + v); // test

				k.push(v);
				list.add(v);
			}

			if(k.hasElements() != !list.isEmpty()) {
				throw null; // bugged !!!
			}
		}
	}
}
