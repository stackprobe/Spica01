package violet.labo.module.fatcalc;

import charlotte.tools.IntTools;
import charlotte.tools.RTError;

public class FatUFloat {
	private int _radix;
	private FatIntList _figures;
	private int _exponent;
	private int _start;
	private int _end;
	private boolean _unsureRange;

	public boolean remained = false;

	public FatUFloat() {
		this(10);
	}

	public FatUFloat(int radix) {
		this(radix, new int[0]);
	}

	public FatUFloat(int radix, int[] figures) {
		this(radix, figures, 0);
	}

	public FatUFloat(int radix, int[] figures, int exponent) {
		if(radix < FatConsts.RADIX_MIN || FatConsts.RADIX_MAX < radix) {
			throw new IllegalArgumentException("Bad radix: " + radix);
		}
		if(figures == null) {
			throw new IllegalArgumentException("Bad figures: null");
		}
		if(exponent < -IntTools.IMAX || IntTools.IMAX < exponent) {
			throw new IllegalArgumentException("Bad exponent: " + exponent);
		}
		_radix = radix;
		_figures = new FatIntList(figures);
		_exponent = exponent;
		_start = 0;
		_end = figures.length;
		_unsureRange = true;

		checkFigures();
		ensureRange();
	}

	private void checkFigures() {
		for(int figure : _figures) {
			if(figure < 0 || _radix <= figure) {
				throw new IllegalArgumentException("Bad figures: " + figure + ", " + _figures);
			}
		}
	}

	private void ensureRange() {
		if(_unsureRange) {
			_unsureRange = false;

			while(_start < _end) {
				if(_figures.get(_end - 1) != 0) {
					while(_figures.get(_start) == 0) {
						_start++;
					}
					break;
				}
				_end--;
			}
		}
	}

	public int radix() {
		return _radix;
	}

	public int start() {
		ensureRange();
		return _exponent + _start;
	}

	public int end() {
		ensureRange();
		return _exponent + _end;
	}

	public int size() {
		return end() - start();
	}

	public boolean isZero() {
		return size() == 0;
	}

	public int get(int index) {
		if(index < -IntTools.IMAX || IntTools.IMAX < index) {
			throw new IllegalArgumentException("Bad index: " + index);
		}
		index -= _exponent;

		return index < _start || _end <= index ? 0 : _figures.get(index);
	}

	public void set(int index, int figure) {
		if(index < -IntTools.IMAX || IntTools.IMAX < index) {
			throw new IllegalArgumentException("Bad index: " + index);
		}
		if(figure < 0 || _radix <= figure) {
			throw new IllegalArgumentException("Bad figure: " + figure);
		}
		index -= _exponent;

		if(index < 0) {
			int exSize = -index;

			_figures.shift(exSize);

			if(_exponent - exSize < -IntTools.IMAX) {
				throw new RTError("Bad exponent: " + _exponent);
			}
			_exponent -= exSize;
			_start += exSize;
			_end += exSize;
			index += exSize;
		}
		if(_figures.size() <= index) {
			_figures.resize(index + 1);
		}
		_figures.set(index, figure);

		if(figure != 0) {
			_start = Math.min(_start, index);
			_end = Math.max(_end, index + 1);
		}
		_unsureRange = true;
	}

	public void shift(int count) {
		if(count < -IntTools.IMAX || IntTools.IMAX < count) {
			throw new IllegalArgumentException("Bad count: " + count);
		}
		int exponentNew = _exponent + count;

		if(exponentNew < -IntTools.IMAX || IntTools.IMAX < exponentNew) {
			throw new IllegalArgumentException("Bad exponent: " + exponentNew + ", " + count);
		}
		_exponent = exponentNew;
	}

	public void resizeCapacity(int size) {
		_figures.resizeCapacity(size);
	}

	public void debugPrint() {
		//System.out.println("FatUFloat: " + _radix + ", " + _exponent + ", [" + String.join(", ", ListTools.select(_figures, v -> "" + v)) + "]");

		StringBuffer buff = new StringBuffer();

		buff.append("FatUFloat: " + _radix + ", " + start() + ", [");

		for(int index = start(); index < end(); index++) {
			if(index != start()) {
				buff.append(", ");
			}
			buff.append(get(index));
		}
		buff.append("]");

		System.out.println(buff.toString());
	}
}
