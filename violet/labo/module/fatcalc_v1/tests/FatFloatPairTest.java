package violet.labo.module.fatcalc_v1.tests;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import violet.labo.module.fatcalc_v1.FatFloat;
import violet.labo.module.fatcalc_v1.FatFloatPair;
import violet.labo.module.fatcalc_v1.FatUFloat;

public class FatFloatPairTest {
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
		//return SecurityTools.cRandom.getInt(-IntTools.IMAX + 1, IntTools.IMAX - 1); // -999,999,999 to 999,999,999

		String format;

		switch(SecurityTools.cRandom.getInt(1, 17)) {
		case 1: format = "9"; break;
		case 2: format = "99"; break;
		case 3: format = "999"; break;
		case 4: format = "9999"; break;
		case 5: format = "99999"; break;
		case 6: format = "999999"; break;
		case 7: format = "9999999"; break;
		case 8: format = "99999999"; break;
		case 9: format = "999999999"; break;

		case 10: format = "900000000"; break;
		case 11: format = "990000000"; break;
		case 12: format = "999000000"; break;
		case 13: format = "999900000"; break;
		case 14: format = "999990000"; break;
		case 15: format = "999999000"; break;
		case 16: format = "999999900"; break;
		case 17: format = "999999990"; break;

		default:
			throw null; // bugged !!!
		}

		StringBuffer buff = new StringBuffer();

		for(char chr : format.toCharArray()) {
			if(chr == '9') {
				chr = StringTools.DECIMAL.charAt(SecurityTools.cRandom.getInt(10));
			}
			buff.append(chr);
		}
		int value = Integer.parseInt(buff.toString());

		if(SecurityTools.cRandom.getInt(2) == 1) {
			value *= -1;
		}
		return value;
	}

	private static FatFloat getFloat(int value) {
		return new FatFloat(new FatUFloat(10, getFigures(Math.abs(value))), value < 0 ? -1 : 1);
	}

	private static int[] getFigures(int value) {
		List<Integer> dest = new ArrayList<Integer>();

		while(1 <= value) {
			dest.add(value % 10);
			value /= 10;
		}
		return IntTools.toArray(dest);
	}

	private static int getInt(FatFloat value) {
		FatUFloat figures = value.figures();

		if(figures.start() < 0) {
			throw null; // bugged !!!
		}
		if(10 < figures.end()) { // max: 999,999,999 + 999,999,999 = 1,999,999,998
			throw null; // bugged !!!
		}
		int ret = 0;
		int scale = 1;

		for(int index = 0; index < figures.end(); index++) {
			ret += figures.get(index) * scale;
			scale *= 10;
		}
		ret *= value.sign();
		return ret;
	}

	private static long getLong(FatFloat value) {
		FatUFloat figures = value.figures();

		if(figures.start() < 0) {
			throw null; // bugged !!!
		}
		if(18 < figures.end()) { // max: 999,999,999 * 999,999,999 = 999,999,998,000,000,001
			throw null; // bugged !!!
		}
		long ret = 0L;
		long scale = 1L;

		for(int index = 0; index < figures.end(); index++) {
			ret += figures.get(index) * scale;
			scale *= 10L;
		}
		ret *= value.sign();
		return ret;
	}
}
