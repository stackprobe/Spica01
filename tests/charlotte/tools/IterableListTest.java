package tests.charlotte.tools;

import java.util.List;

import charlotte.tools.IterableList;
import charlotte.tools.StringTools;

public class IterableListTest {
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
		List<String> oneToThree = StringTools.tokenize("1:2:3", ":");

		IterableList<String> oneToThree_x3 = new IterableList<String>()
				.addOne("{")
				.add(oneToThree)
				.add(oneToThree)
				.add(oneToThree)
				.addOne("}");

		for(String s : new IterableList<String>()
				.addOne("A")
				.add(oneToThree)
				.addOne("B")
				.add(oneToThree_x3)
				.addOne("C")
				) {
			System.out.print(" " + s);
		}
		System.out.println("");
	}
}
