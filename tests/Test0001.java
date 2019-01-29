package tests;

import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.IArrays;
import charlotte.tools.ThreadEx;

public class Test0001 {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			//test03();
			//test04();
			//test05();
			//test06_0();
			test06();

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

		//list.add("DDD"); //	例外 -- 追加は出来ない。
		//list.add("EEE");

		//list.remove(2); //	例外 -- 削除も出来ない。

		arr = list.toArray(new String[list.size()]);

		System.out.println(String.join(", ", arr));
	}

	private static void test02() {
		System.out.println(new Test0001().getClass().getName());
		System.out.println(new Test0001().getClass().getSimpleName());
	}

	private static void test03() {
		for(int c = 0x00; c <= 0xff; c++) {
			byte chr = (byte)c;
			System.out.println((int)chr + ", " + (chr & 0xff) + " -> " + (chr < 0x20));
		}
	}

	private static void test04() {
		List<String> list = ArrayTools.copy("1:2:3:4:5:6:7:8:9".split("[:]"));

		list.subList(3, 6).clear();

		System.out.println(String.join(":", list));
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
		char c = '連';

		System.out.println("" + (int)c);
	}

	private static void test06() {
		for(int c = 0; c < 5; c++) {
			test06_a("aaa".split("[:]", c));
			test06_a("aaa:".split("[:]", c));
			test06_a("aaa:bbb".split("[:]", c));
			test06_a("aaa:bbb:".split("[:]", c));
			test06_a("aaa:bbb:ccc".split("[:]", c));
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
}
