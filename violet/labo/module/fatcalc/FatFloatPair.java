package violet.labo.module.fatcalc;

import charlotte.tools.IntTools;

public class FatFloatPair {
	private FatFloat _a;
	private FatFloat _b;

	public FatFloatPair(FatFloat a, FatFloat b) {
		_a = a;
		_b = b;
	}

	public FatFloat answer() {
		return _a;
	}

	public void add() {
		if(_a.sign() == -1) {
			if(_b.sign() == -1) {
				new FatUFloatPair(_a.figures(), _b.figures()).add();
				_a = new FatFloat(_a.figures(), -1);
			}
			else {
				_a = new FatFloat(_b.figures(), new FatUFloatPair(_b.figures(), _a.figures()).sub());
			}
		}
		else {
			if(_b.sign() == -1) {
				_a = new FatFloat(_a.figures(), new FatUFloatPair(_a.figures(), _b.figures()).sub());
			}
			else {
				new FatUFloatPair(_a.figures(), _b.figures()).add();
			}
		}
	}

	public void sub() {
		_b = new FatFloat(_b.figures(), _b.sign() * -1);
		add();
	}

	public void mul() {
		_a = new FatFloat(new FatUFloatPair(_a.figures(), _b.figures()).mul(), _a.sign() * _b.sign());
	}

	public void div(int basement) {
		if(basement < -IntTools.IMAX || IntTools.IMAX < basement) {
			throw new IllegalArgumentException("Bad basement: " + basement);
		}
		_a.figures().shift(basement);
		_a = new FatFloat(new FatUFloatPair(_a.figures(), _b.figures()).div(), _a.sign() * _b.sign());
	}
}
