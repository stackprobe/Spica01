package wb.t20191206_httpserverfwdemo.module.fatcalc_v1.tests;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;
import wb.t20191206_httpserverfwdemo.module.fatcalc_v1.FatCalc;

public class FatCalcTest {
	public static void main(String[] args) {
		try {
			test01();
			//test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		FatCalc calc = new FatCalc(10);

		long t = System.currentTimeMillis();
		//String ans = calc.power("2", 100000);
		String ans = calc.power("2", 300000);
		//String ans = calc.power("2", 1000000);
		System.out.println("t: " + (System.currentTimeMillis() - t));
		System.out.println("ans: " + ans.length());
	}

	private static void test02() throws Exception {
		FatCalc calc = new FatCalc(10);

		//String n = StringTools.repeat("1234567890", 100);
		//String n = StringTools.repeat("1234567890", 300);
		//String n = StringTools.repeat("1234567890", 1000);
		//String n = StringTools.repeat("1234567890", 3000);
		//String n = StringTools.repeat("1234567890", 10000);
		//String n = StringTools.repeat("1234567890", 30000);
		String n = StringTools.repeat("1234567890", 100000);
		String d = "1234567890";

		long t = System.currentTimeMillis();
		String ans = calc.calc(n, "/", d);
		System.out.println("t: " + (System.currentTimeMillis() - t));
		System.out.println("ans: " + ans.length());
		FileTools.writeAllText("C:/temp/1.txt", ans, StringTools.CHARSET_ASCII);
	}
}
