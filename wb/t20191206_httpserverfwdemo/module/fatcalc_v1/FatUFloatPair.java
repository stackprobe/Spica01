package wb.t20191206_httpserverfwdemo.module.fatcalc_v1;

import charlotte.tools.RTError;
import wb.t20191206_httpserverfwdemo.module.fatcalc_v1.tests.Stopwatch;

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
		int start = _b.start();
		int end = _b.end();
		int carry = 0;

		for(int index = start; index < end || carry == 1; index++) {
			int value = _a.get(index) + _b.get(index) + carry;

			carry = value / _radix;

			_a.set(index, value % _radix);
		}
	}

	public int sub() {
		int start = _b.start();
		int end = _b.end();
		int end2 = Math.max(_a.end(), _b.end()) + 1;
		//int end2 = Math.max(_a.end(), _b.end()); // old @ 2336
		int carry = 0;

		for(int index = start; (index < end || carry == -1) && index < end2; index++) {
			int value = _a.get(index) - _b.get(index) + carry + _radix;

			carry = value / _radix - 1;

			_a.set(index, value % _radix);
		}
		if(carry == -1) {
			//_a.set(end, _radix - 1); // deleted @ 2336
			inverse(_a, 1L);
		}
		return carry * 2 + 1;
	}

	public FatUFloat mul() {
		FatUFloat answer = new FatUFloat(_radix);
		int start = _a.start();
		int end = _a.end();
		int s = _b.start();
		int e = _b.end();

		answer.resizeCapacity(end + e);

		for(int index = start; index < end; index++) {
			for(int ndx = s; ndx < e; ndx++) {
				add(answer, index + ndx, (long)_a.get(index) * _b.get(ndx));
			}
		}
		return answer;
	}

	public FatUFloat div() {
		if(_b.isZero()) {
			throw new RTError("Zero divide");
		}

		//_a.debugPrint();
		//_b.debugPrint();
		Stopwatch sw = new Stopwatch("div");
		sw.start("M");
		{
			int numer = _radix;
			int denom = _b.get(_b.end() - 1) + 1;
			int d = numer / denom;

			//System.out.println("d: " + d); // test

			FatUFloat dd = new FatUFloat(_radix, new int[] { d }, 0);

			_a = new FatUFloatPair(_a, dd).mul();
			_b = new FatUFloatPair(_b, dd).mul();
		}

		FatUFloat answer = new FatUFloat(_radix);

		//_a.debugPrint();
		//_b.debugPrint();
		sw.start("L");
		for(; ; ) {
			int ae = _a.end();
			int be = _b.end();

			//sw.start("Li_" + ae + "_" + be);
			if(ae < be) {
				break;
			}
			if(ae == be) {
				int numer = _a.get(ae - 1);
				int denom = _b.get(be - 1) + 1;
				int d = numer / denom;

				if(d == 0) {
					break;
				}
				add(answer, 0, (long)d);

				int ret = new FatUFloatPair(_a, new FatUFloatPair(_b, new FatUFloat(_radix, new int[] { d }, 0)).mul()).sub();
				if(ret != 1) {
					throw null; // never
				}
			}
			else {
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

		_a.debugPrint();
		_b.debugPrint();
		//answer.debugPrint();
		sw.start("L2");
		if(_a.isZero() == false) {
			if(sub() == 1) {
				add(answer, 0, 1L);
				answer.remained = _a.isZero() == false;
			}
			else {
				answer.remained = true;
			}
		}
		/*
		while(_a.isZero() == false) {
			if(sub() == -1) {
				answer.remained = true;
				break;
			}
			add(answer, 0, 1L);
		}
		*/
		sw.stop();
		sw.debugPrint();
		return answer;
	}
}
