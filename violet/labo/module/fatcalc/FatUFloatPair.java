package violet.labo.module.fatcalc;

import charlotte.tools.RTError;

public class FatUFloatPair {
	private FatUFloat _a;
	private FatUFloat _b;
	private int _radix;

	public FatUFloatPair(FatUFloat a, FatUFloat b) {
		_a = a;
		_b = b;
		_radix = a.radix();

		if(_radix != b.radix()) {
			throw new IllegalArgumentException("Bad radix: " + _radix + ", " + b.radix());
		}
	}

	private void add(FatUFloat a, int index, long value) {
		while(value != 0L) {
			value += a.get(index);
			a.set(index, (int)(value % _radix));
			value /= _radix;
			index++;
		}
	}

	private void inverse(FatUFloat a, long valueAdd) {
		int start = a.start();
		int end = a.end();

		for(int index = start; index < end; index++) {
			a.set(index, _radix - 1 - a.get(index));
		}
		add(a, start, valueAdd);
	}

	public void add() {
		int start = Math.min(_a.start(), _b.start());
		int end = Math.max(_a.end(), _b.end());
		int carry = 0;

		for(int index = start; index < end; index++) {
			int value = _a.get(index) + _b.get(index) + carry;

			carry = value / _radix;

			_a.set(index, value % _radix);
		}
		_a.set(end, carry);
	}

	public int sub() {
		int start = Math.min(_a.start(), _b.start());
		int end = Math.max(_a.end(), _b.end());
		int carry = 0;

		for(int index = start; index < end; index++) {
			int value = _a.get(index) - _b.get(index) + carry + _radix;

			carry = value / _radix - 1;

			_a.set(index, value % _radix);
		}
		if(carry == -1) {
			_a.set(end, _radix - 1);
			inverse(_a, 1L);
		}
		return carry * 2 + 1;
	}

	public FatUFloat mul() {
		FatUFloat answer = new FatUFloat(_radix);

		for(int index = _a.start(); index < _a.end(); index++) {
			for(int ndx = _b.start(); ndx < _b.end(); ndx++) {
				add(answer, index + ndx, (long)_a.get(index) * _b.get(ndx));
			}
		}
		return answer;
	}

	public FatUFloat div() {
		if(_b.isZero()) {
			throw new RTError("Zero divide");
		}

		{
			int numer = _radix - 1;
			int denom = _b.get(_b.end() - 1);
			int d = numer / denom;

			FatUFloat dd = new FatUFloat(_radix, new int[] { d }, 0);

			_a = new FatUFloatPair(_a, dd).mul();
			_b = new FatUFloatPair(_b, dd).mul();
		}

		FatUFloat answer = new FatUFloat(_radix);

		for(; ; ) {
			int ae = _a.end();
			int be = _b.end();

			if(ae < be) {
				break;
			}
			if(ae == be) {
				int numer = _a.get(ae - 1);
				int denom = _b.get(be - 1) + 1;
				int d = numer / denom;

				add(answer, 0, (long)d);

				int ret = new FatUFloatPair(_a, new FatUFloatPair(_b, new FatUFloat(_radix, new int[] { d }, 0)).mul()).sub();
				if(ret != 1) {
					throw null; // never
				}
				break;
			}

			{
				long numer = _a.get(ae - 2) + (long)_a.get(ae - 1) * _radix;
				long denom = _b.get(be - 1) + 1L;
				long d = numer / denom;
				int di = ae - be - 1;

				add(answer, di, d);

				int dL = (int)(d % _radix);
				int dH = (int)(d / _radix);

				int ret = new FatUFloatPair(_a, new FatUFloatPair(_b, new FatUFloat(_radix, new int[] { dL, dH }, di)).mul()).sub();
				if(ret != 1) {
					throw null; // never
				}
			}
		}
		while(_a.isZero() == false) {
			if(sub() == -1) {
				answer.remained = true;
				break;
			}
			add(answer, 0, 1L);
		}
		return answer;
	}
}
