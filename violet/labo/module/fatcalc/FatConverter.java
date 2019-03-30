package violet.labo.module.fatcalc;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.IterableTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;

public class FatConverter {
	public int radix;
	public int rdx;
	public int rdxw;

	public FatConverter(int radix) {
		this.radix = radix;

		rdx = radix;
		rdxw = 1;

		while(rdx <= IntTools.IMAX / radix) {
			rdx *= radix;
			rdxw++;
		}
	}

	public static final String DIGIT_36 = StringTools.DECIMAL + StringTools.alpha;

	public List<Integer> figures = new ArrayList<Integer>();
	public int exponent = 0;
	public int sign = 1;
	public boolean remained = false;

	public void setString(String operand) {
		if(operand == null) {
			throw new IllegalArgumentException("Bad operand: " + operand);
		}

		// init
		{
			figures.clear();
			exponent = 0;
			sign = 1;
			remained = false;
		}

		boolean decimalPointPassed = false;

		for(int index = 0; index < operand.length(); index++) {
			char chr = operand.charAt(index);

			if(chr == '-') {
				sign = -1;
			}
			else if(chr == '.') {
				decimalPointPassed = true;
			}
			else if(chr == '[') {
				int value = 0;

				for(; ; ) {
					chr = operand.charAt(++index);

					if(chr == ']') {
						break;
					}
					int val = StringTools.DECIMAL.indexOf(chr);

					if(val == -1) {
						throw new IllegalArgumentException("Bad character: " + chr);
					}
					if((long)radix <= value * 10L + val) {
						throw new IllegalArgumentException("Overflow: " + value + ", " + val + ", " + radix);
					}
					value *= 10;
					value += val;
				}
				addToFigures(value, decimalPointPassed);
			}
			else {
				chr = Character.toLowerCase(chr);
				int val = DIGIT_36.indexOf(chr);

				if(val == -1) {
					throw new IllegalArgumentException("Bad character: " + chr);
				}
				addToFigures(val, decimalPointPassed);
			}
		}
		ListTools.reverse(figures);
		normalize();
	}

	private void addToFigures(int value, boolean decimalPointPassed) {
		if(radix <= value) {
			throw new IllegalArgumentException("Overflow: " + value + ", " + radix);
		}
		figures.add(value);

		if(decimalPointPassed) {
			exponent--;
		}
	}

	private void normalize() {
		int start;

		for(start = 0; start < figures.size() && figures.get(start) == 0; start++) {
			// noop
		}

		if(start < figures.size()) {
			int end;

			for(end = figures.size(); figures.get(end - 1) == 0; end--) {
				// noop
			}

			if(0 < start || end < figures.size()) {
				figures = ListTools.copyOfRange(figures, start, end);
				exponent += start;
			}
		}
		else {
			figures.clear();
			exponent = 0;

			if(remained == false) {
				sign = 1;
			}
		}
	}

	public String getString(int basement) {
		normalize();

		StringBuffer buff = new StringBuffer();

		int start = Math.min(0, -exponent);
		int end = Math.max(figures.size(), -exponent + 1);

		if(remained) {
			buff.append('*');

			if(basement == -1) {
				throw null; // never
			}
			start = -exponent - basement;
		}
		for(int index = start; index < end; index++) {
			if(index == -exponent) {
				buff.append('.');
			}
			int value;

			if(0 <= index && index < figures.size()) {
				value = figures.get(index);
			}
			else {
				value = 0;
			}

			if(value < 36) {
				buff.append(DIGIT_36.charAt(value));
			}
			else {
				buff.append(StringTools.reverse("[" + value + "]"));
			}
		}
		if(sign == -1) {
			buff.append('-');
		}
		String ret = buff.toString();
		ret = StringTools.reverse(ret);

		// normalize
		{
			if(ret.startsWith("0")) {
				int index;

				for(index = 0; ret.charAt(index) == '0' && ret.charAt(index + 1) != '.'; index++) {
					// noop
				}
				ret = ret.substring(index);
			}
			else if(ret.startsWith("-0")) {
				int index;

				for(index = 1; ret.charAt(index) == '0' && ret.charAt(index + 1) != '.'; index++) {
					// noop
				}
				ret = "-" + ret.substring(index);
			}

			if(ret.endsWith("0") || ret.endsWith(".")) {
				int index;

				for(index = ret.length(); ret.charAt(index - 1) == '0'; index--) {
					// noop
				}
				if(ret.charAt(index - 1) == '.') {
					index--;
				}
				ret = ret.substring(0, index);
			}
		}

		return ret;
	}

	public void setFloat(FatFloat a) {
		figures.clear();

		for(int index = 0; index < a.inner.inner.figures.length; index++) {
			int value = a.inner.inner.figures[index];

			for(int ndx = 0; ndx < rdxw; ndx++) {
				figures.add(value % radix);
				value /= radix;
			}
		}
		exponent = a.inner.exponent * rdxw;
		sign = a.sign;
		remained = a.inner.inner.remained;

		normalize();
	}

	public FatFloat getFloat() {
		normalize();

		// zantei
		{
			int a;
			int z;

			a = exponent % rdxw;
			a += rdxw;
			a %= rdxw;

			ListTools.insertRange(figures, 0, IterableTools.repeat(0, a));
			exponent -= a;

			z = figures.size() % rdxw;
			z = rdxw - z;
			z %= rdxw;

			ListTools.insertRange(figures, figures.size(), IterableTools.repeat(0, z));
		}

		List<Integer> bundles = new ArrayList<Integer>();

		for(int index = 0; index < figures.size(); index += rdxw) {
			int value = 0;
			int scale = 1;

			for(int ndx = 0; ndx < rdxw; ndx++) {
				value += figures.get(index + ndx) * scale;
				scale *= radix;
			}
			bundles.add(value);
		}
		return new FatFloat(new FatUFloat(new FatUInt(IntTools.toArray(bundles)), exponent / rdxw), sign);
	}
}
