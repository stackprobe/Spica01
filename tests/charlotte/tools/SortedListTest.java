package tests.charlotte.tools;

import charlotte.tools.ArrayTools;
import charlotte.tools.SortedList;
import charlotte.tools.StringTools;

public class SortedListTest {
	public static void main(String[] args) {
		try {
			test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		test01_a(new String[] { "1", "2", "3" }, "1", new String[] { "1" });
		test01_a(new String[] { "1", "2", "3" }, "2", new String[] { "2" });
		test01_a(new String[] { "1", "2", "3" }, "3", new String[] { "3" });
		test01_a(new String[] { "1", "2", "2", "3", "3", "3" }, "0", new String[] { });
		test01_a(new String[] { "1", "2", "2", "3", "3", "3" }, "1", new String[] { "1", });
		test01_a(new String[] { "1", "2", "2", "3", "3", "3" }, "2", new String[] { "2", "2" });
		test01_a(new String[] { "1", "2", "2", "3", "3", "3" }, "3", new String[] { "3", "3", "3" });
		test01_a(new String[] { "1", "2", "2", "3", "3", "3" }, "4", new String[] { });
		test01_a(new String[] { "1", "1", "1", "1", "3", "3", "3", "3" }, "2", new String[] { });
		test01_a(new String[] { }, "1", new String[] { });
	}

	private static void test01_a(String[] arr, String target, String[] expect) {
		SortedList<String> list = new SortedList<String>(StringTools.comp);
		list.addAll(arr);
		String[] ans = list.getMatch(list.getFerret(target)).toArray(new String[0]);

		if(ArrayTools.comp(ans, expect, StringTools.comp) != 0) {
			throw null; // bugged !!!
		}
	}

	private static void test02() {
		test02_a(new String[] { "1", "2", "3" }, "1", new String[] { null, "1", "2" });
		test02_a(new String[] { "1", "2", "3" }, "2", new String[] { "1", "2", "3" });
		test02_a(new String[] { "1", "2", "3" }, "3", new String[] { "2", "3", null });
		test02_a(new String[] { "1", "2", "2", "3", "3", "3" }, "0", new String[] { null, "1" });
		test02_a(new String[] { "1", "2", "2", "3", "3", "3" }, "1", new String[] { null, "1", "2" });
		test02_a(new String[] { "1", "2", "2", "3", "3", "3" }, "2", new String[] { "1", "2", "2", "3" });
		test02_a(new String[] { "1", "2", "2", "3", "3", "3" }, "3", new String[] { "2", "3", "3", "3", null });
		test02_a(new String[] { "1", "2", "2", "3", "3", "3" }, "4", new String[] { "3", null });
		test02_a(new String[] { "1", "1", "1", "1", "3", "3", "3", "3" }, "2", new String[] { "1", "3" });
		test02_a(new String[] { }, "1", new String[] { null, null });
	}

	private static void test02_a(String[] arr, String target, String[] expect) {
		SortedList<String> list = new SortedList<String>(StringTools.comp);
		list.addAll(arr);
		String[] ans = list.getMatchWithEdge(list.getFerret(target)).toArray(new String[0]);

		if(ArrayTools.comp(ans, expect, (a, b) -> StringTools.comp.compare("" + a, "" + b)) != 0) {
			throw null; // bugged !!!
		}
	}
}
