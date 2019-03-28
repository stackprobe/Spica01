package violet.labo.module.fatcalc;

public class FatEUIntCalc {
	public int radix;

	public FatEUIntCalc(int radix) {
		this.radix = radix;
	}

	public void add(FatEUInt a, FatEUInt b) {
		a.normalize();
		b.normalize();

		a.sync(b);
		b.sync(a);

		new FatUIntCalc(radix).add(a.inner, b.inner);

		a.normalize();
	}

	public void sub(FatEUInt a, FatEUInt b) {
		a.normalize();
		b.normalize();

		a.sync(b);
		b.sync(a);

		new FatUIntCalc(radix).sub(a.inner, b.inner);

		a.normalize();
	}

	public FatEUInt mul(FatEUInt a, FatEUInt b) {
		a.normalize();
		b.normalize();

		FatEUInt answer = new FatEUInt(new FatUIntCalc(radix).mul(a.inner, b.inner), a.exponent + b.exponent);

		answer.normalize();

		return answer;
	}

	public FatEUInt div(FatEUInt a, FatEUInt b) {
		a.normalize();
		b.normalize();

		a.sync(b);
		b.sync(a);

		FatEUInt answer = new FatEUInt(new FatUIntCalc(radix).div(a.inner, b.inner), 0);

		answer.normalize();

		return answer;
	}
}
