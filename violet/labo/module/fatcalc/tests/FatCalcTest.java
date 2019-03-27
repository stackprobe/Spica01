package violet.labo.module.fatcalc.tests;

import violet.labo.module.fatcalc.FatCalc;

public class FatCalcTest {
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
		FatCalc calc = new FatCalc(10);

		System.out.println(calc.power("2", 64));
		System.out.println(calc.power("2.1", 100));
		System.out.println(calc.power("0.3", 100));
	}
}
