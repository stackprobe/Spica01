package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IArrays;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;

public class ListToolsTest {
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
		List<String> list = StringTools.tokenize("A:B:C:1:2:3:D:E:F", ":");
		System.out.println(String.join(":", list)); // ---> A:B:C:1:2:3:D:E:F
		ListTools.removeRange(list, 3, 6);
		System.out.println(String.join(":", list)); // ---> A:B:C:D:E:F
		ListTools.insertRange(list, 3, IArrays.asList(IArrays.repeat("999", 3)));
		System.out.println(String.join(":", list)); // ---> A:B:C:999:999:999:D:E:F

		// ----

		list = new ArrayList<String>();
		System.out.println(String.join(":", list)); // ---> ""
		ListTools.insertRange(list, 0, IArrays.asList(IArrays.repeat("a", 2)));
		System.out.println(String.join(":", list)); // ---> a:a
		ListTools.insertRange(list, 2, IArrays.asList(IArrays.repeat("b", 2)));
		System.out.println(String.join(":", list)); // ---> a:a:b:b
		ListTools.insertRange(list, 0, IArrays.asList(IArrays.repeat("c", 2)));
		System.out.println(String.join(":", list)); // ---> c:c:a:a:b:b
		ListTools.removeRange(list, 0, 2);
		System.out.println(String.join(":", list)); // ---> a:a:b:b
		ListTools.removeRange(list, 2, 4);
		System.out.println(String.join(":", list)); // ---> a:a
		ListTools.removeRange(list, 0, 2);
		System.out.println(String.join(":", list)); // ---> ""

		// ----

		list = new ArrayList<String>();
		System.out.println(String.join(":", list)); // ---> ""
		ListTools.removeRange(list, 0, 0);
		System.out.println(String.join(":", list)); // ---> ""
		ListTools.insertRange(list, 0, new ArrayList<String>(0));
		System.out.println(String.join(":", list)); // ---> ""

		// ----

		list = StringTools.tokenize("A:Z", ":");
		System.out.println(String.join(":", list)); // ---> A:Z
		ListTools.removeRange(list, 0, 0);
		System.out.println(String.join(":", list)); // ---> A:Z
		ListTools.removeRange(list, 1, 1);
		System.out.println(String.join(":", list)); // ---> A:Z
		ListTools.removeRange(list, 2, 2);
		System.out.println(String.join(":", list)); // ---> A:Z
		ListTools.insertRange(list, 0, new ArrayList<String>(0));
		System.out.println(String.join(":", list)); // ---> A:Z
		ListTools.insertRange(list, 1, new ArrayList<String>(0));
		System.out.println(String.join(":", list)); // ---> A:Z
		ListTools.insertRange(list, 2, new ArrayList<String>(0));
		System.out.println(String.join(":", list)); // ---> A:Z

		// ----
	}
}
