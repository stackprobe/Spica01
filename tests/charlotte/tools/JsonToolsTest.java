package tests.charlotte.tools;

import charlotte.tools.FileTools;
import charlotte.tools.JsonTools;
import charlotte.tools.ObjectTree;
import charlotte.tools.StringTools;

public class JsonToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			test03();

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

	private static void test02() {
		System.out.println(JsonTools.encode(ObjectTree.conv(new Object[] {
				"String",
				true,
				false,
				123,
				(short)456,
				789L,
				123.456,
				789.012F,
		}
		)));
	}

	private static void test03() throws Exception {
		test03_a(
				"C:/temp/a.json",
				"C:/temp/b.json"
				);
	}

	private static void test03_a(String rFile, String wFile) throws Exception {
		byte[] bText = FileTools.readAllBytes(rFile);
		Object json = JsonTools.decode(bText);
		String text = JsonTools.encode(json);
		FileTools.writeAllText(wFile, text, StringTools.CHARSET_UTF8);
	}
}
