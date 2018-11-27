package tests;

import java.util.List;

import charlotte.tools.IArrays;

public class Test0001 {
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
}
