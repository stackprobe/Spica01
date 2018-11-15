package tests;

import java.util.Arrays;
import java.util.List;

public class Test0001 {
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
		String[] arr = new String[] { "AAA", "BBB", "CCC" };
		List<String> list = Arrays.asList(arr);

		//list.add("DDD"); //	例外 -- 追加は出来ない。
		//list.add("EEE");

		//list.remove(2); //	例外 -- 削除も出来ない。

		arr = list.toArray(new String[list.size()]);

		System.out.println(String.join(", ", arr));
	}
}
