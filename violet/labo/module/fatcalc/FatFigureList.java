package violet.labo.module.fatcalc;

import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.IterableTools;
import charlotte.tools.ListTools;

public class FatFigureList {
	private int _radix;
	private List<Integer> _figures;
	private int _exponent;

	public FatFigureList() {
		this(10);
	}

	public FatFigureList(int radix) {
		this(radix, new int[0]);
	}

	public FatFigureList(int radix, int[] figures) {
		this(radix, figures, 0);
	}

	public FatFigureList(int radix, int[] figures, int exponent) {
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
		_figures = ListTools.toList(IntTools.asList(figures));
		_exponent = exponent;

		checkFigures();
		trim();
	}

	private void checkFigures() {
		for(int figure : _figures) {
			if(figure < 0 || _radix <= figure) {
				throw new IllegalArgumentException("Bad figures: " + figure + ", " + _figures);
			}
		}
	}

	public void trim() {
		while(1 <= _figures.size() && _figures.get(_figures.size() - 1) == 0) {
			_figures.remove(_figures.size() - 1);
		}
		int index = ListTools.indexOf(_figures, figure -> figure != 0);

		if(index == -1) {
			_figures.clear();
			_exponent = 0;
		}
		else {
			ListTools.removeRange(_figures, 0, index);
			_exponent += index;

			if(IntTools.IMAX < _exponent) {
				throw new IllegalArgumentException("Bad exponent: " + _exponent);
			}
		}
	}

	public int radix() {
		return _radix;
	}

	public int start() {
		return _exponent;
	}

	public int end() {
		return _exponent + _figures.size();
	}

	public int get(int index) {
		if(index < -IntTools.IMAX || IntTools.IMAX < index) {
			throw new IllegalArgumentException("Bad index: " + index);
		}
		index -= _exponent;

		if(index < 0 || _figures.size() <= index) {
			return 0;
		}
		return _figures.get(index);
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
			ListTools.insertRange(_figures, 0, IterableTools.repeat(0, -index));
			index = 0;
		}
		while(_figures.size() <= index) {
			_figures.add(0);
		}
		_figures.set(index, figure);
	}

	public void inverse() {
		for(int index = 0; index < _figures.size(); index++) {
			_figures.set(index, _radix - 1 - _figures.get(index));
		}
	}

	public boolean isZero() {
		trim();
		return _figures.size() == 0;
	}
}
