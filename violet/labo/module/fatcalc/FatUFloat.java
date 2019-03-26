package violet.labo.module.fatcalc;

import charlotte.tools.RTError;

public class FatUFloat {
	private FatFigureList _figures;

	public FatUFloat() {
		this(new FatFigureList());
	}

	public FatUFloat(FatFigureList figures) {
		if(figures == null) {
			throw new IllegalArgumentException("Bad figures: " + figures);
		}
		_figures = figures;
	}

	public FatFigureList figures() {
		return _figures;
	}

	private void checkAnother(FatUFloat another) {
		if(another == null) {
			throw new IllegalArgumentException("Bad another: null");
		}
		if(_figures.radix() != another._figures.radix()) {
			throw new IllegalArgumentException("Bad another radix: " + another._figures.radix());
		}
	}

	private void add(int index, long value) {
		while(value != 0L) {
			value += _figures.get(index);
			_figures.set(index, (int)(value % _figures.radix()));
			value /= _figures.radix();
			index++;
		}
	}

	public void add(FatUFloat another) {
		checkAnother(another);

		int start = Math.min(_figures.start(), another._figures.start());
		int end = Math.max(_figures.end(), another._figures.end());
		int carry = 0;

		for(int index = start; index < end; index++) {
			int value = _figures.get(index) + another._figures.get(index) + carry;

			carry = value / _figures.radix();

			_figures.set(index, value % _figures.radix());
		}
		_figures.set(end, carry);
	}

	public int sub(FatUFloat another) {
		checkAnother(another);

		int start = Math.min(_figures.start(), another._figures.start());
		int end = Math.max(_figures.end(), another._figures.end());
		int carry = 0;

		for(int index = start; index < end; index++) {
			int value = _figures.get(index) - another._figures.get(index) + carry + _figures.radix();

			carry = value / _figures.radix() - 1;

			_figures.set(index, value % _figures.radix());
		}
		if(carry == -1) {
			_figures.inverse();
			add(_figures.start(), 1L);
		}
		return carry;
	}

	public FatUFloat mul(FatUFloat another) {
		checkAnother(another);

		FatUFloat answer = new FatUFloat(new FatFigureList(_figures.radix()));

		for(int index = _figures.start(); index < _figures.end(); index++) {
			for(int ndx = another._figures.start(); ndx < another._figures.end(); ndx++) {
				answer.add(index + ndx, (long)_figures.get(index) * another._figures.get(ndx));
			}
		}
		return answer;
	}

	public FatUFloat div(FatUFloat another) {
		checkAnother(another);

		FatUFloat answer = new FatUFloat(new FatFigureList(_figures.radix()));

		if(another._figures.isZero()) {
			throw new RTError("Zero divide");
		}
		int numer = _figures.radix();
		//another._figures.trim();
		int denom = another._figures.get(another._figures.end() - 1);
		int d = numer / denom;

		FatUFloat scale = new FatUFloat(new FatFigureList(_figures.radix(), new int[] { d }, 0));

		FatUFloat a = mul(scale);
		FatUFloat b = another.mul(scale);

		a.divSub(b, answer);

		return answer;
	}

	private void divSub(FatUFloat another, FatUFloat answer) {
		for(; ; ) {
			_figures.trim();
			//another._figures.trim();

			int e = _figures.end();
			int e2 = another._figures.end();

			if(e < e2) {
				break;
			}
			if(e == e2) {
				int numer = _figures.get(e - 1);
				int denom = another._figures.get(e2 - 1) + 1;
				int d = numer / denom;

				answer.add(0, d);

				int ret = sub(another.mul(new FatUFloat(new FatFigureList(_figures.radix(), new int[] { d }, 0))));
				if(ret != 1) {
					throw null; // never
				}
				break;
			}

			{
				long numer = _figures.get(e - 2) + (long)_figures.get(e - 1) * _figures.radix();
				long denom = another._figures.get(e2 - 1) + 1L;
				long d = numer / denom;
				int scale = e - e2 - 1;

				answer.add(scale, d);

				int dL = (int)(d % _figures.radix());
				int dH = (int)(d / _figures.radix());

				int ret = sub(another.mul(new FatUFloat(new FatFigureList(_figures.radix(), new int[] { dL, dH }, scale))));
				if(ret != 1) {
					throw null; // never
				}
			}
		}
		while(sub(another) == 1) {
			answer.add(0, 1L);
		}
	}
}
