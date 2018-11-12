package tests.charlotte.tools;

import charlotte.tools.ArrayTools;
import charlotte.tools.ListTools;

public class ArrayToolsTest {
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
		Iterable<String> strs = ListTools.<Integer, String>select(
				ListTools.<String, Integer>select(ArrayTools.iterable(new String[] { "123", "456", "789" }), str -> Integer.parseInt(str)),
				value -> value.toString()
				);

		for(String str : strs) {
			System.out.println(str);
		}
	}
}
