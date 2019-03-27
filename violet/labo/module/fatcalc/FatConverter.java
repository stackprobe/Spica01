package violet.labo.module.fatcalc;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
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

	private List<Integer> _figures = new ArrayList<Integer>();
	private int _exponent = 0;
	private int _sign = 1;
	private boolean _remained = false;

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
		ListTools.reverse(_figures);
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
			_figures = ListTools.toList(_figures.subList(start, end));
		}
		if(_figures.size() == 0) {
			_exponent = 0;
			_sign = 1;
		}
	}

	public FatFloat getFloat() {
		normalize();

		throw null; // TODO
	}

	public FatFloat getFloat(String str) {
		throw null; // TODO
	}

	public String getString(FatFloat src) {
		throw null; // TODO
	}
}
