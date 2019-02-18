package tests.charlotte.tools;

import charlotte.tools.JsonTools;

public class JsonToolsTest {
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
		test01_a("[ 1, 2, 3]");
		test01_a("[ 1, 2, 3,]"); // warning x1
		test01_a("{ \"a\": 1, \"b\": 2, \"c\": 3 }");
		test01_a("{ \"a\": 1, \"b\": 2, \"c\": 3, }"); // warning x1
		test01_a("{ 1: 2, 3: 4, 5: 6 }");  // warning x3
		test01_a("{ 1: 2, 3: 4, 5: 6, }"); // warning x4

		test01_a("1");
		test01_a("\"a\"");
		test01_a("[]");
		test01_a("{}");
		test01_a("[   ]");
		test01_a("{   }");

		test01_a(","); // warning x1
		test01_a(",,"); // warning x1
		test01_a(",,,"); // warning x1
		test01_a("tea"); // warning x1
		test01_a("tea time"); // warning x1
		test01_a("tea time 123"); // warning x1

		test01_a("true");
		test01_a("false");
		test01_a("null");
		test01_a("-1.234e+99");
	}

	private static void test01_a(String json) {
		System.out.println("< " + json);

		Object ret = JsonTools.decode(json);

		System.out.println("> " + ret);

		String ret2 = JsonTools.encode(ret);
		String ret3 = JsonTools.encode(ret, true);

		//System.out.println(">2 " + ret2);
		//System.out.println(">3 " + ret3);
	}
}