package wb.t20191206_httpserverfwdemo.module.fatcalc_v1;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IArray;
import charlotte.tools.IntTools;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;

public class FatConverter {
	private int _radix;
	private int _rdx;
	private int _rdxw;

	public FatConverter(int radix) {
		if(radix < FatConsts.RADIX_MIN || FatConsts.RADIX_MAX < radix) {
			throw new IllegalArgumentException("Bad radix: " + radix);
		}
		_radix = radix;
		_rdx = radix;
		_rdxw = 1;

		while(_rdx <= IntTools.IMAX / radix) {
			_rdx *= radix;
			_rdxw++;
		}
	}

	private static final String DIGIT_36 = StringTools.DECIMAL + StringTools.alpha;

	private FatIntList _figures = new FatIntList();
	private int _exponent = 0;
	private int _sign = 1;
	private boolean _remained = false;

	public void exponent(int count) {
		_exponent += count;
	}

	public void setString(String str) {
		if(str == null) {
			throw new IllegalArgumentException("Bad string: " + str);
		}

		// init
		{
			_figures.clear();
			_exponent = 0;
			_sign = 1;
			_remained = false;
		}

		boolean readDot = false;

		for(int index = 0; index < str.length(); index++) {
			char chr = str.charAt(index);

			if(chr == '-') {
				_sign = -1;
			}
			else if(chr == '.') {
				readDot = true;
			}
			else if(chr == '[') {
				int value = 0;

				for(; ; ) {
					chr = str.charAt(++index);

					if(chr == ']') {
						break;
					}
					int val = StringTools.DECIMAL.indexOf(chr);

					if(val == -1) {
						throw new IllegalArgumentException("Bad character: " + chr);
					}
					if((long)_radix <= value * 10L + val) {
						throw new IllegalArgumentException("Overflow: " + value + ", " + val + ", " + _radix);
					}
					value *= 10;
					value += val;
				}
				addToFigures(value, readDot);
			}
			else {
				chr = Character.toLowerCase(chr);
				int val = DIGIT_36.indexOf(chr);

				if(val == -1) {
					throw new IllegalArgumentException("Bad character: " + chr);
				}
				addToFigures(val, readDot);
			}
		}
		_figures.reverse();
		normalize();
	}

	private void addToFigures(int value, boolean readDot) {
		if(_radix <= value) {
			throw new IllegalArgumentException("Overflow: " + value + ", " + _radix);
		}
		_figures.add(value);

		if(readDot) {
			_exponent--;
		}
	}

	private void normalize() {
		int start = 0;
		int end = _figures.size();

		while(0 < end && _figures.get(end - 1) == 0) {
			end--;
		}
		while(start < end && _figures.get(start) == 0) {
			start++;
			_exponent++;
		}
		if(0 < start || end < _figures.size()) {
			_figures.makeMeSubList(start, end);
		}
		if(_figures.size() == 0) {
			_exponent = 0;

			if(_remained == false) {
				_sign = 1;
			}
		}
	}

	public String getString(int basement) {
		normalize();

		StringBuffer buff = new StringBuffer();

		int start = Math.min(0, -_exponent);
		int end = Math.max(_figures.size(), -_exponent + 1);

		if(_remained) {
			buff.append('*');

			if(basement < 0 || IntTools.IMAX < basement) {
				throw null; // never
			}
			if(start < -basement) {
				throw null; // never
			}
			start = -basement;
		}
		for(int index = start; index < end; index++) {
			if(index == -_exponent) {
				buff.append('.');
			}
			int value;

			if(0 <= index && index < _figures.size()) {
				value = _figures.get(index);
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
		if(_sign == -1) {
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

	public void setFloat(FatFloat operand) {
		if(operand == null) {
			throw new IllegalArgumentException("Bad operand: null");
		}
		if(operand.figures().radix() != _rdx) {
			throw new IllegalArgumentException("Bad operand radix: " + operand.figures().radix() + ", " + _rdx);
		}
		_figures.clear();

		for(int index = operand.figures().start(); index < operand.figures().end(); index++) {
			int value = operand.figures().get(index);

			for(int ndx = 0; ndx < _rdxw; ndx++) {
				_figures.add(value % _radix);
				value /= _radix;
			}
		}
		_exponent = operand.figures().start() * _rdxw;
		_sign = operand.sign();
		_remained = operand.figures().remained;

		normalize();
	}

	public FatFloat getFloat() {
		normalize();

		IArray<Integer> figarr;
		int figarrexp = _exponent;

		{
			int t;
			int a;
			int z;

			t = _exponent % _rdxw;
			t += _rdxw;
			t %= _rdxw;

			a = t;
			figarrexp -= t;

			t = _figures.size() % _rdxw;
			t = _rdxw - t;
			t %= _rdxw;

			z = t;

			figarr = new IArray<Integer>() {
				@Override
				public int length() {
					return a + _figures.size() + z;
				}

				@Override
				public Integer get(int index) {
					return index < a || a + _figures.size() <= index ? 0 : _figures.get(index - a);
				}

				@Override
				public void set(int index, Integer element) {
					throw new RTError("forbidden");
				}
			};
		}

		List<Integer> bundles = new ArrayList<Integer>();

		for(int index = 0; index < figarr.length(); index += _rdxw) {
			int value = 0;
			int scale = 1;

			for(int ndx = 0; ndx < _rdxw; ndx++) {
				value += figarr.get(index + ndx) * scale;
				scale *= _radix;
			}
			bundles.add(value);
		}
		return new FatFloat(new FatUFloat(_rdx, IntTools.toArray(bundles), figarrexp / _rdxw), _sign);
	}
}
