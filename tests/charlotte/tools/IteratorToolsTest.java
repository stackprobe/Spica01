package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IArrays;
import charlotte.tools.IteratorTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;

public class IteratorToolsTest {
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
		Iterable<String> oneToThree = StringTools.tokenize("1:2:3", ":");
		Iterable<String> oneToThree_x3;
		Iterable<String> complex;

		{
			List<Iterable<String>> list = new ArrayList<Iterable<String>>();

			list.add(IArrays.asList(new String[] { "{" }));
			list.add(oneToThree);
			list.add(oneToThree);
			list.add(oneToThree);
			list.add(IArrays.asList(new String[] { "}" }));

			oneToThree_x3 = IteratorTools.linearize(list);
		}

		{
			List<Iterable<String>> list = new ArrayList<Iterable<String>>();

			list.add(IArrays.asList(new String[] { "A" }));
			list.add(oneToThree);
			list.add(IArrays.asList(new String[] { "B" }));
			list.add(oneToThree_x3);
			list.add(IArrays.asList(new String[] { "C" }));

			complex = IteratorTools.linearize(list);
		}

		for(int c = 0; c < 3; c++) {
			for(String s : complex) {
				System.out.print(" " + s);
			}
			System.out.println("");
		}
	}

	private static void test02() {
		List<String> strs = new ArrayList<String>();

		ListTools.insertRange(strs, 0, IteratorTools.repeat("b", 7));
		ListTools.insertRange(strs, 7, IteratorTools.repeat("c", 7));
		ListTools.insertRange(strs, 0, IteratorTools.repeat("a", 7));

		System.out.println(String.join(":", strs)); // a:a:a:a:a:a:a:b:b:b:b:b:b:b:c:c:c:c:c:c:c

		ListTools.insertRange(strs, 7, IteratorTools.repeat("w", 7));
		ListTools.insertRange(strs, 21, IteratorTools.repeat("x", 7));
		ListTools.removeRange(strs, 14, 21); // -= b...
		ListTools.removeRange(strs, 21, 28); // -= c...
		ListTools.removeRange(strs, 0, 7); // -= a...

		System.out.println(String.join(":", strs)); // w:w:w:w:w:w:w:x:x:x:x:x:x:x
	}
}
