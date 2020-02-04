package wb.t20191206_httpserverfwdemo.module.csvtable.utils.extra.tests;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import wb.t20191206_httpserverfwdemo.module.csvtable.utils.extra.TextFileSorter;

public class TextFileSorterTest {
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

	private static void test01() throws Exception {
		test01_a(0, 3);
		test01_a(1, 3);
		test01_a(0, 10);
		test01_a(0, 100);
		test01_a(30, 30);
	}

	private static void test01_a(int minlen, int maxlen) throws Exception {
		test01_b(minlen, maxlen, 1, 1);
		test01_b(minlen, maxlen, 10, 10);
		test01_b(minlen, maxlen, 100, 100);
		test01_b(minlen, maxlen, 1000, 30);
		test01_b(minlen, maxlen, 10000, 10);
	}

	private static void test01_b(int minlen, int maxlen, int linecntlmt, int testcnt) throws Exception {
		System.out.println(minlen + ", " + maxlen + ", " + linecntlmt + ", " + testcnt); // test

		while(1 <= testcnt) {
			test01_c(minlen, maxlen, linecntlmt);
			testcnt--;
		}
	}

	private static String MID_FILE = "C:/temp/1.tmp";

	private static void test01_c(int minlen, int maxlen, int linecntlmt) throws Exception {
		//System.out.println(minlen + ", " + maxlen + ", " + linecntlmt); // test

		List<String> lines = makeTestVector(minlen, maxlen, linecntlmt);

		FileTools.writeAllLines(MID_FILE, lines, StringTools.CHARSET_SJIS);

		lines.sort(StringTools.comp);

		try(TextFileSorter sorter = new TextFileSorter(MID_FILE) {
			@Override
			public int capacity() {
				return 1000;
			}
		}
		) {
			sorter.sort(StringTools.comp);
		}

		List<String> lines2 = FileTools.readAllLines(MID_FILE, StringTools.CHARSET_SJIS);

		if(ListTools.comp(lines, lines2, StringTools.comp) != 0) {
			throw null; // bugged !!!
		}
	}

	private static List<String> makeTestVector(int minlen, int maxlen, int linecntlmt) {
		int linecnt = SecurityTools.cRandom.getInt(linecntlmt);
		List<String> lines = new ArrayList<String>();

		while(lines.size() < linecnt) {
			lines.add(SecurityTools.makePassword(
					StringTools.HALF,
					SecurityTools.cRandom.getRangeInt(minlen, maxlen)
					));
		}
		return lines;
	}
}
