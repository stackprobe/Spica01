package violet.labo.module.fatcalc.tests;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import violet.labo.module.fatcalc.FatUInt;
import violet.labo.module.fatcalc.FatUIntCalc;

/**
 *	2進数ver
 *
 */
public class FatUIntCalcTestB {
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
		for(int c = 0; c < 1000000; c++) {
			int a = test01_randInt();
			int b = test01_randInt();

			{
				int ans = a + b;

				System.out.println(a + " + " + b);
				System.out.println("= " + ans);

				FatUInt aa = getUInt(a);
				new FatUIntCalc(2).add(aa, getUInt(b));
				int ans2 = getInt(aa);

				System.out.println("= " + ans2);

				if(ans != ans2) {
					throw null; // bugged !!!
				}
			}

			{
				int ans = a - b;

				System.out.println(a + " - " + b);
				System.out.println("= " + ans);

				FatUInt aa = getUInt(a);
				int sign = new FatUIntCalc(2).sub(aa, getUInt(b));
				int ans2 = getInt(aa) * sign;

				System.out.println("= " + ans2);

				if(ans != ans2) {
					throw null; // bugged !!!
				}
			}

			{
				long ans = (long)a * b;

				System.out.println(a + " * " + b);
				System.out.println("= " + ans);

				long ans2 = getLong(new FatUIntCalc(2).mul(getUInt(a), getUInt(b)));

				System.out.println("= " + ans2);

				if(ans != ans2) {
					throw null; // bugged !!!
				}
			}

			if(b == 0) {
				b = 1;
			}

			{
				int ans = a / b;
				boolean rem = a % b != 0;

				System.out.println(a + " / " + b);
				System.out.println("= " + ans + ", " + rem);

				FatUInt answer = new FatUIntCalc(2).div(getUInt(a), getUInt(b));
				long ans2 = getLong(answer);
				boolean rem2 = answer.remained;

				System.out.println("= " + ans2 + ", " + rem2);

				if(ans != ans2) {
					throw null; // bugged !!!
				}
				if(rem != rem2) {
					throw null; // bugged !!!
				}
			}
		}
	}

	private static int test01_randInt() {
		//return SecurityTools.cRandom.getInt(0, 0x3fffffff); // 0 to 111,111,111,111,111,111,111,111,111,111

		String format;

		switch(SecurityTools.cRandom.getInt(1, 59)) {
		case 1: format = "B"; break;
		case 2: format = "BB"; break;
		case 3: format = "BBB"; break;
		case 4: format = "BBBB"; break;
		case 5: format = "BBBBB"; break;
		case 6: format = "BBBBBB"; break;
		case 7: format = "BBBBBBB"; break;
		case 8: format = "BBBBBBBB"; break;
		case 9: format = "BBBBBBBBB"; break;
		case 10: format = "BBBBBBBBBB"; break;
		case 11: format = "BBBBBBBBBBB"; break;
		case 12: format = "BBBBBBBBBBBB"; break;
		case 13: format = "BBBBBBBBBBBBB"; break;
		case 14: format = "BBBBBBBBBBBBBB"; break;
		case 15: format = "BBBBBBBBBBBBBBB"; break;
		case 16: format = "BBBBBBBBBBBBBBBB"; break;
		case 17: format = "BBBBBBBBBBBBBBBBB"; break;
		case 18: format = "BBBBBBBBBBBBBBBBBB"; break;
		case 19: format = "BBBBBBBBBBBBBBBBBBB"; break;
		case 20: format = "BBBBBBBBBBBBBBBBBBBB"; break;
		case 21: format = "BBBBBBBBBBBBBBBBBBBBB"; break;
		case 22: format = "BBBBBBBBBBBBBBBBBBBBBB"; break;
		case 23: format = "BBBBBBBBBBBBBBBBBBBBBBB"; break;
		case 24: format = "BBBBBBBBBBBBBBBBBBBBBBBB"; break;
		case 25: format = "BBBBBBBBBBBBBBBBBBBBBBBBB"; break;
		case 26: format = "BBBBBBBBBBBBBBBBBBBBBBBBBB"; break;
		case 27: format = "BBBBBBBBBBBBBBBBBBBBBBBBBBB"; break;
		case 28: format = "BBBBBBBBBBBBBBBBBBBBBBBBBBBB"; break;
		case 29: format = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBB"; break;
		case 30: format = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"; break;

		case 31: format = "B00000000000000000000000000000"; break;
		case 32: format = "BB0000000000000000000000000000"; break;
		case 33: format = "BBB000000000000000000000000000"; break;
		case 34: format = "BBBB00000000000000000000000000"; break;
		case 35: format = "BBBBB0000000000000000000000000"; break;
		case 36: format = "BBBBBB000000000000000000000000"; break;
		case 37: format = "BBBBBBB00000000000000000000000"; break;
		case 38: format = "BBBBBBBB0000000000000000000000"; break;
		case 39: format = "BBBBBBBBB000000000000000000000"; break;
		case 40: format = "BBBBBBBBBB00000000000000000000"; break;
		case 41: format = "BBBBBBBBBBB0000000000000000000"; break;
		case 42: format = "BBBBBBBBBBBB000000000000000000"; break;
		case 43: format = "BBBBBBBBBBBBB00000000000000000"; break;
		case 44: format = "BBBBBBBBBBBBBB0000000000000000"; break;
		case 45: format = "BBBBBBBBBBBBBBB000000000000000"; break;
		case 46: format = "BBBBBBBBBBBBBBBB00000000000000"; break;
		case 47: format = "BBBBBBBBBBBBBBBBB0000000000000"; break;
		case 48: format = "BBBBBBBBBBBBBBBBBB000000000000"; break;
		case 49: format = "BBBBBBBBBBBBBBBBBBB00000000000"; break;
		case 50: format = "BBBBBBBBBBBBBBBBBBBB0000000000"; break;
		case 51: format = "BBBBBBBBBBBBBBBBBBBBB000000000"; break;
		case 52: format = "BBBBBBBBBBBBBBBBBBBBBB00000000"; break;
		case 53: format = "BBBBBBBBBBBBBBBBBBBBBBB0000000"; break;
		case 54: format = "BBBBBBBBBBBBBBBBBBBBBBBB000000"; break;
		case 55: format = "BBBBBBBBBBBBBBBBBBBBBBBBB00000"; break;
		case 56: format = "BBBBBBBBBBBBBBBBBBBBBBBBBB0000"; break;
		case 57: format = "BBBBBBBBBBBBBBBBBBBBBBBBBBB000"; break;
		case 58: format = "BBBBBBBBBBBBBBBBBBBBBBBBBBBB00"; break;
		case 59: format = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBB0"; break;

		default:
			throw null; // bugged !!!
		}

		StringBuffer buff = new StringBuffer();

		for(char chr : format.toCharArray()) {
			if(chr == 'B') {
				chr = StringTools.BINADECIMAL.charAt(SecurityTools.cRandom.getInt(2));
			}
			buff.append(chr);
		}
		int value = Integer.parseInt(buff.toString(), 2);
		System.out.println(buff.toString() + " --> " + value); // test
		return value;
	}

	private static FatUInt getUInt(int value) {
		return new FatUInt(getFigures(value));
	}

	private static int[] getFigures(int value) {
		List<Integer> dest = new ArrayList<Integer>();

		int origVal = value; // test
		while(1 <= value) {
			dest.add(value % 2);
			value /= 2;
		}
		System.out.println(origVal + " --> [" + String.join(", ", ListTools.select(dest.iterator(), v -> "" + v)) + "]"); // test
		return IntTools.toArray(dest);
	}

	private static int getInt(FatUInt value) {
		if(31 < value.figures.length) { // max: 111,111,111,111,111,111,111,111,111,111 + 111,111,111,111,111,111,111,111,111,111 = 1,111,111,111,111,111,111,111,111,111,110
			throw null; // bugged !!!
		}
		int ret = 0;
		int scale = 1;

		StringBuffer bits = new StringBuffer(); // test
		for(int index = 0; index < value.figures.length; index++) {
			ret += value.figures[index] * scale;
			scale *= 2;

			bits.append("" + value.figures[index]); // test
		}
		System.out.println("[" + bits + "] --> " + ret); // test
		return ret;
	}

	private static long getLong(FatUInt value) {
		if(60 < value.figures.length) { // max: 111,111,111,111,111,111,111,111,111,111 * 111,111,111,111,111,111,111,111,111,111 = 111,111,111,111,111,111,111,111,111,110,000,000,000,000,000,000,000,000,000,001
			throw null; // bugged !!!
		}
		long ret = 0L;
		long scale = 1L;

		StringBuffer bits = new StringBuffer(); // test
		for(int index = 0; index < value.figures.length; index++) {
			ret += value.figures[index] * scale;
			scale *= 2L;

			bits.append("" + value.figures[index]); // test
		}
		System.out.println("[" + bits + "] --> " + ret); // test
		return ret;
	}
}
