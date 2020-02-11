package wb.t20191201_tests;

import java.io.File;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.IArrays;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import charlotte.tools.ThreadEx;
import charlotte.tools.WorkingDir;

public class Test0001 {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			//test03();
			//test04();
			//test05();
			//test06_0();
			//test06();
			//test06_2();
			//test07();
			//test08();
			//test09();
			test10();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		String[] arr = new String[] { "AAA", "BBB", "CCC" };
		List<String> list = IArrays.asList(arr);

		//list.add("DDD"); //	throw exception -- add dekinai
		//list.add("EEE");

		//list.remove(2); //	throw exception -- remove mo dekinai

		arr = list.toArray(new String[list.size()]);

		System.out.println(String.join(", ", arr));
	}

	private static void test02() {
		System.out.println(new Test0001().getClass().getName());
		System.out.println(new Test0001().getClass().getSimpleName());
	}

	private static void test03() {
		/*
0, 0 -> true
1, 1 -> true
2, 2 -> true
...
125, 125 -> false
126, 126 -> false
127, 127 -> false
-128, 128 -> true
-127, 129 -> true
-126, 130 -> true
...
-3, 253 -> true
-2, 254 -> true
-1, 255 -> true
		*/
		for(int c = 0x00; c <= 0xff; c++) {
			byte chr = (byte)c;
			System.out.println((int)chr + ", " + (chr & 0xff) + " -> " + (chr < 0x20));
		}
	}

	private static void test04() {
		List<String> list = ArrayTools.copy("1:2:3:4:5:6:7:8:9".split("[:]"));

		list.subList(3, 6).clear();

		System.out.println(String.join(":", list)); // 1:2:3:7:8:9
	}

	private static void test05() throws Exception {
		for(int c = 0; c < 10; c++) {
			String str = "[" + c + "]";

			new ThreadEx(() -> {
				Thread.sleep(500);

				System.out.println(str);
			});

			Thread.sleep(100);
		}
		Thread.sleep(500);
	}

	private static void test06_0() {
		char c = '\u9023';

		System.out.println("" + (int)c);
	}

	private static void test06() {
		for(int c = -1; c < 10; c++) {
			System.out.println("c: " + c);
			test06_a("".split("[:]", c));
			test06_a("aaa".split("[:]", c));
			test06_a("aaa:".split("[:]", c));
			test06_a("aaa:bbb".split("[:]", c));
			test06_a("aaa:bbb:".split("[:]", c));
			test06_a("aaa:bbb:ccc".split("[:]", c));
			test06_a(":aaa".split("[:]", c));
			test06_a(":aaa:".split("[:]", c));
			test06_a(":aaa:bbb".split("[:]", c));
			test06_a(":aaa:bbb:".split("[:]", c));
		}
	}

	private static void test06_a(String[] tokens) {
		for(int index = 0; index < tokens.length; index++) {
			if(1 <= index) {
				System.out.print(", ");
			}
			if(tokens[index] == null) {
				System.out.print("<null>");
			}
			else {
				System.out.print("\"" + tokens[index] + "\"");
			}
		}
		System.out.println("");
	}

	private static void test06_2() {
		for(int c = 0; c < 1000000; c++) {
			test06_2a(SecurityTools.makePassword(": ab", SecurityTools.cRandom.getInt(100)), ':', SecurityTools.cRandom.getRangeInt(-1, 100));
		}
	}

	private static void test06_2a(String str, char delimiter, int limit) {
		System.out.println("[" + str + "], " + limit); // test

		List<String> list = StringTools.tokenize(str, "" + delimiter);

		if(str.isEmpty()) {
			list.clear();
			list.add("");
		}
		else if(limit == -1) {
			// noop
		}
		else if(limit == 0) {
			while(limit < list.size() && list.get(list.size() - 1).isEmpty()) {
				list.remove(list.size() - 1);
			}
		}
		else {
			while(limit < list.size()) {
				String b = list.remove(list.size() - 1);
				String a = list.remove(list.size() - 1);

				list.add(a + delimiter + b);
			}
		}
		String[] ans = list.toArray(new String[0]);
		String[] ans2 = str.split("[" + delimiter + "]", limit);

		//System.out.println("ans_: [" + String.join(",", ans) + "] " + ans.length);
		//System.out.println("ans2: [" + String.join(",", ans2) + "] " + ans2.length);

		if(ArrayTools.comp(ans, ans2, StringTools.comp) != 0) {
			throw null; // souteigai !!!
		}
	}

	private static Thread _test07_th2;

	private static void test07() throws Exception {
		Thread th = Thread.currentThread();

		test07_a(th, true);

		try(ThreadEx subTh = new ThreadEx(() -> test07_a(th, false))) {
			subTh.relayThrow();
		}

		_test07_th2 = new Thread(() -> {
			try {
				test07_a(_test07_th2, true);

				try(ThreadEx subTh = new ThreadEx(() -> test07_a(_test07_th2, false))) {
					subTh.relayThrow();
				}
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
		});

		_test07_th2.start();
		_test07_th2.join();
		_test07_th2 = null;
	}

	private static void test07_a(Thread mainTh, boolean callByMainTh) {
		Thread currTh = Thread.currentThread();

		System.out.println("mainTh: " + mainTh);
		System.out.println("currTh: " + currTh);
		System.out.println("callByMainTh: " + callByMainTh);

		if(mainTh == currTh) {
			if(callByMainTh == false) {
				throw null; // souteigai !!!
			}
		}
		else {
			if(callByMainTh) {
				throw null; // souteigai !!!
			}
		}
	}

	private static void test08() throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			// dir -> file
			{
				String dir = wd.makePath();
				String file = wd.makePath();

				FileTools.createDir(dir);
				Thread.sleep(2000);
				FileTools.writeAllBytes(file, BinTools.EMPTY);

				System.out.println("d: " + new File(dir).lastModified());
				System.out.println("f: " + new File(file).lastModified());

				if(new File(dir).lastModified() < new File(file).lastModified()) {
					// noop
				}
				else {
					throw null; // bugged !!!
				}
			}

			// file -> dir
			{
				String dir = wd.makePath();
				String file = wd.makePath();

				FileTools.writeAllBytes(file, BinTools.EMPTY);
				Thread.sleep(2000);
				FileTools.createDir(dir);

				System.out.println("f: " + new File(file).lastModified());
				System.out.println("d: " + new File(dir).lastModified());

				if(new File(file).lastModified() < new File(dir).lastModified()) {
					// noop
				}
				else {
					throw null; // bugged !!!
				}

				// ----

				Thread.sleep(2000);

				FileTools.writeAllBytes(FileTools.combine(dir, "aaa"), BinTools.EMPTY);

				System.out.println("d: " + new File(dir).lastModified()); // \u30d5\u30a9\u30eb\u30c0\u306e\u4e2d\u8eab\u3092\u5909\u3048\u308b\u3068 lastModified \u3082\u5909\u66f4\u3055\u308c\u308b\u6a21\u69d8\u3002
			}
		}
	}

	private static void test09() {
		test09_b(0);
		test09_b(1);
		test09_b(0x7ffffffe);
		test09_b(0x7fffffff);
		test09_b(0x80000000);
		test09_b(0x80000001);
		test09_b(0xfffffffe);
		test09_b(0xffffffff);
	}

	private static void test09_b(int value) {
		System.out.println(value + " ----> " + String.format("%08x", value));
	}

	private static void test10() {
		System.out.println(Integer.toHexString(0x7fffffff));
		System.out.println(Integer.toHexString(0x80000000));
		System.out.println(Integer.toHexString(0xffffffff));
	}
}
