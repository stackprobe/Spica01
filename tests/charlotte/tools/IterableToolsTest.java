package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IArrays;
import charlotte.tools.IterableTools;
import charlotte.tools.StringTools;

public class IterableToolsTest {
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

			oneToThree_x3 = IterableTools.linearize(list);
		}

		{
			List<Iterable<String>> list = new ArrayList<Iterable<String>>();

			list.add(IArrays.asList(new String[] { "A" }));
			list.add(oneToThree);
			list.add(IArrays.asList(new String[] { "B" }));
			list.add(oneToThree_x3);
			list.add(IArrays.asList(new String[] { "C" }));

			complex = IterableTools.linearize(list);
		}

		for(String s : complex) {
			System.out.print(" " + s);
		}
		System.out.println("");
	}
}
