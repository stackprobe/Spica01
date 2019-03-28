package violet.labo.module.fatcalc.v1.tests;

import charlotte.tools.StringTools;
import violet.labo.module.fatcalc.v1.FatConverter;
import violet.labo.module.fatcalc.v1.FatFloat;

public class FatConverterTest {
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
		test01_a("0");
		test01_a("1");
		test01_a("2");
		test01_a("3");
		test01_a("0.0001");
		test01_a("10000");
		test01_a("123");
		test01_a("123456");
		test01_a("123456789");
		test01_a("123456789123456789");
		test01_a("123456789123456789123456789");
		test01_a("0.123456789123456789123456789");
		test01_a("0.000123456789123456789123456789");
		test01_a("0.000000123456789123456789123456789");
		test01_a("0.000000000123456789123456789123456789");
		test01_a("0.000000000000123456789123456789123456789");
		test01_a("123456789.123456789123456789");
		test01_a("123456789123456789.123456789");
		test01_a("123456789123456789123456789000");
		test01_a("123456789123456789123456789000000");
		test01_a("123456789123456789123456789000000000");
		test01_a("123456789123456789123456789000000000000");

		test01_a("-1");
		test01_a("-2");
		test01_a("-3");
		test01_a("-0.0001");
		test01_a("-10000");
		test01_a("-123");
		test01_a("-123456");
		test01_a("-123456789");
		test01_a("-123456789123456789");
		test01_a("-123456789123456789123456789");
		test01_a("-0.123456789123456789123456789");
		test01_a("-0.000123456789123456789123456789");
		test01_a("-0.000000123456789123456789123456789");
		test01_a("-0.000000000123456789123456789123456789");
		test01_a("-0.000000000000123456789123456789123456789");
		test01_a("-123456789.123456789123456789");
		test01_a("-123456789123456789.123456789");
		test01_a("-123456789123456789123456789000");
		test01_a("-123456789123456789123456789000000");
		test01_a("-123456789123456789123456789000000000");
		test01_a("-123456789123456789123456789000000000000");

		for(int c = 0; c < 100; c++) {
			test01_a("0." + StringTools.repeat("0", c) + "58946");
			test01_a("58946" + StringTools.repeat("0", c));
			test01_a("-0." + StringTools.repeat("0", c) + "58946");
			test01_a("-58946" + StringTools.repeat("0", c));
		}

		for(int c = 1; c < 100; c++) {
			test01_a("0." + StringTools.repeat("9", c));
			test01_a(StringTools.repeat("9", c));
			test01_a(StringTools.repeat("9", c) + "." + StringTools.repeat("9", c));
			test01_a("-0." + StringTools.repeat("9", c));
			test01_a("-" + StringTools.repeat("9", c));
			test01_a("-" + StringTools.repeat("9", c) + "." + StringTools.repeat("9", c));
		}
	}

	private static void test01_a(String str) {
		FatConverter conv = new FatConverter(10);

		conv.setString(str);
		String ret = conv.getString(-1);

		System.out.println("str: " + str);
		System.out.println("ret: " + ret);

		if(str.equals(ret) == false) {
			throw null; // bugged !!!
		}

		conv.setString(str);
		FatFloat operand = conv.getFloat();
		conv.setFloat(operand);
		ret = conv.getString(-1);

		System.out.println("ret: " + ret);

		if(str.equals(ret) == false) {
			throw null; // bugged !!!
		}
	}
}
