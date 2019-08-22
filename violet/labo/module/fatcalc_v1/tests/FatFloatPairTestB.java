package violet.labo.module.fatcalc_v1.tests;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import violet.labo.module.fatcalc_v1.FatFloat;
import violet.labo.module.fatcalc_v1.FatFloatPair;
import violet.labo.module.fatcalc_v1.FatUFloat;

/**
 *	2進数ver
 *
 */
public class FatFloatPairTestB {
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

				FatFloatPair pair = new FatFloatPair(getFloat(a), getFloat(b));
				pair.add();
				int ans2 = getInt(pair.answer());

				System.out.println("= " + ans2);

				if(ans != ans2) {
					throw null; // bugged !!!
				}
			}

			{
				int ans = a - b;

				System.out.println(a + " - " + b);
				System.out.println("= " + ans);

				FatFloatPair pair = new FatFloatPair(getFloat(a), getFloat(b));
				pair.sub();
				int ans2 = getInt(pair.answer());

				System.out.println("= " + ans2);

				if(ans != ans2) {
					throw null; // bugged !!!
				}
			}

			{
				long ans = (long)a * b;

				System.out.println(a + " * " + b);
				System.out.println("= " + ans);

				FatFloatPair pair = new FatFloatPair(getFloat(a), getFloat(b));
				pair.mul();
				long ans2 = getLong(pair.answer());

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

				FatFloatPair pair = new FatFloatPair(getFloat(a), getFloat(b));
				pair.div(0);
				int ans2 = getInt(pair.answer());
				boolean rem2 = pair.answer().figures().remained;

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

		if(SecurityTools.cRandom.getInt(2) == 1) {
			value *= -1;
		}
		return value;
	}

	private static FatFloat getFloat(int value) {
		return new FatFloat(new FatUFloat(2, getFigures(Math.abs(value))), value < 0 ? -1 : 1);
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

	private static int getInt(FatFloat value) {
		FatUFloat figures = value.figures();

		if(figures.start() < 0) {
			throw null; // bugged !!!
		}
		if(31 < figures.end()) { // max: 111,111,111,111,111,111,111,111,111,111 + 111,111,111,111,111,111,111,111,111,111 = 1,111,111,111,111,111,111,111,111,111,110
			throw null; // bugged !!!
		}
		int ret = 0;
		int scale = 1;

		StringBuffer bits = new StringBuffer(); // test
		for(int index = 0; index < figures.end(); index++) {
			ret += figures.get(index) * scale;
			scale *= 2;

			bits.append("" + figures.get(index)); // test
		}
		System.out.println("[" + bits + "] --> " + ret); // test
		ret *= value.sign();
		return ret;
	}

	private static long getLong(FatFloat value) {
		FatUFloat figures = value.figures();

		if(figures.start() < 0) {
			throw null; // bugged !!!
		}
		if(60 < figures.end()) { // max: 111,111,111,111,111,111,111,111,111,111 * 111,111,111,111,111,111,111,111,111,111 = 111,111,111,111,111,111,111,111,111,110,000,000,000,000,000,000,000,000,000,001
			throw null; // bugged !!!
		}
		long ret = 0L;
		long scale = 1L;

		StringBuffer bits = new StringBuffer(); // test
		for(int index = 0; index < figures.end(); index++) {
			ret += figures.get(index) * scale;
			scale *= 2L;

			bits.append("" + figures.get(index)); // test
		}
		System.out.println("[" + bits + "] --> " + ret); // test
		ret *= value.sign();
		return ret;
	}
}
