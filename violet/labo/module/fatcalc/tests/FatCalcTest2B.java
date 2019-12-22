package violet.labo.module.fatcalc.tests;

import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import violet.labo.module.fatcalc.FatCalc;

/**
 *	2進数ver
 *
 */
public class FatCalcTest2B {
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
		for(int c = 0; c < 1000000; c++) {
			String a = test01_randOperand();
			String b = test01_randOperand();

			System.out.println("a: " + a);
			System.out.println("b: " + b);

			{
				String ans = keisan(a, "+", b);
				String ans2 = new FatCalc(2).calc(a, "+", b);

				System.out.println("= " + ans);
				System.out.println("= " + ans2);

				if(ans.equals(ans2) == false) {
					throw null; // bugged !!!
				}
			}

			{
				String ans = keisan(a, "-", b);
				String ans2 = new FatCalc(2).calc(a, "-", b);

				System.out.println("= " + ans);
				System.out.println("= " + ans2);

				if(ans.equals(ans2) == false) {
					throw null; // bugged !!!
				}
			}

			{
				String ans = keisan(a, "*", b);
				String ans2 = new FatCalc(2).calc(a, "*", b);

				System.out.println("= " + ans);
				System.out.println("= " + ans2);

				if(ans.equals(ans2) == false) {
					throw null; // bugged !!!
				}
			}

			if(isZero(b)) {
				b = "1";
			}

			{
				String ans = keisan(a, "/", b);
				String ans2 = new FatCalc(2).calc(a, "/", b);

				System.out.println("= " + ans);
				System.out.println("= " + ans2);

				if(ans.equals(ans2) == false) {
					throw null; // bugged !!!
				}
			}
		}
	}

	private static String test01_randOperand() {
		StringBuffer buff = new StringBuffer();

		if(SecurityTools.cRandom.getInt(2) == 1) {
			buff.append("-");
		}
		buff.append(test01_randIntOperand());

		if(SecurityTools.cRandom.getInt(2) == 1) {
			buff.append(".");
			buff.append(test01_randIntOperand());
		}
		return buff.toString();
	}

	private static String test01_randIntOperand() {
		switch(SecurityTools.cRandom.getInt(2)) {
		case 0:
			return
					test01_randSingleIntOperand();
		case 1:
			return
					test01_randSingleIntOperand() +
					test01_randSingleIntOperand();
		}
		throw null; // never
	}

	private static String test01_randSingleIntOperand() {
		switch(SecurityTools.cRandom.getInt(4)) {
		case 0:
			return
					"0";
		case 1:
			return
					SecurityTools.makePassword(StringTools.BINADECIMAL, SecurityTools.cRandom.getRangeInt(1, 100)) +
					StringTools.repeat("0", SecurityTools.cRandom.getRangeInt(1, 100));
		case 2:
			return
					SecurityTools.makePassword(StringTools.BINADECIMAL, SecurityTools.cRandom.getRangeInt(1, 100));
		case 3:
			return
					StringTools.repeat("0", SecurityTools.cRandom.getRangeInt(1, 100)) +
					SecurityTools.makePassword(StringTools.BINADECIMAL, SecurityTools.cRandom.getRangeInt(1, 100));
		}
		throw null; // never
	}

	private static String keisan(String a, String operator, String b) throws Exception {
		String command = "C:/Factory/Tools/Keisan.exe //O C:/temp/1.tmp /B 30 /X 2 " + a + " " + operator + " " + b;

		System.out.println("command: " + command);

		Runtime.getRuntime().exec(command).waitFor();

		return FileTools.readAllText("C:/temp/1.tmp", StringTools.CHARSET_ASCII).trim();
	}

	private static boolean isZero(String operand) {
		return StringTools.liteValidate(operand, "-.0");
		/*
		String format = operand;

		format = StringTools.replaceChars(format, "-.0", '-');
		format = StringTools.replaceLoop(format, "--", "-");

		return format.equals("-");
		*/
	}
}
