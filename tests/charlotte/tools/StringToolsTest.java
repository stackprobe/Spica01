package tests.charlotte.tools;

import charlotte.tools.StringTools;

public class StringToolsTest {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		System.out.println(StringTools.HALF);
	}
}
