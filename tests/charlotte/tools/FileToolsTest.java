package tests.charlotte.tools;

import java.util.Arrays;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;

public class FileToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		test01_a(new String[] { });
		test01_a(new String[] { "" });
		test01_a(new String[] { "AAA" });
		test01_a(StringTools.tokenize("AAA:BBB", ":"));
		test01_a(StringTools.tokenize("AAA:BBB:CCC", ":"));
		test01_a(StringTools.tokenize("AAA:BBB:CCC:DDD", ":"));
		test01_a(StringTools.tokenize("AAA:BBB:CCC:DDD:EEE", ":"));
		test01_a(StringTools.tokenize(":", ":"));
		test01_a(StringTools.tokenize("::", ":"));
		test01_a(StringTools.tokenize(":::", ":"));
		test01_a(StringTools.tokenize(":::a", ":"));
		test01_a(StringTools.tokenize("::a:", ":"));
		test01_a(StringTools.tokenize(":a::", ":"));
		test01_a(StringTools.tokenize("a:::", ":"));
	}

	private static void test01_a(String[] lines) throws Exception {
		test01_a(Arrays.asList(lines));
	}

	private static void test01_a(List<String> lines) throws Exception {
		// test >
		/*
		System.out.println("{");
		for(String line : lines) {
			System.out.println(line);
		}
		System.out.println("}");
		*/
		// < test


		FileTools.writeAllLines(
				"C:/temp/FileToolsTest_test01_a.tmp", lines, StringTools.CHARSET_UTF8);
		List<String> rdLines = FileTools.readAllLines(
				"C:/temp/FileToolsTest_test01_a.tmp", StringTools.CHARSET_UTF8);

		if(ListTools.comp(lines, rdLines, StringTools.comp) != 0) {
			throw null; // bugged !!!
		}
	}

	private static void test02() {
		System.out.println(FileTools.getFileNameWithoutExtension("C:/temp/AAA.dat"));
	}
}
