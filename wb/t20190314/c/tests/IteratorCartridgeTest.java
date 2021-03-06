package wb.t20190314.c.tests;

import charlotte.tools.IArrays;
import wb.t20190314.c.IteratorCartridge;

public class IteratorCartridgeTest {
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
		IteratorCartridge<String> ic = new IteratorCartridge<String>(IArrays.asList("A:B:C".split("[:]")).iterator());

		for(String element : ic) {
			System.out.println(element);
		}
	}
}
