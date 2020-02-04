package wb.t20191206_httpserverfwdemo.module.fatcalc;

public class FatUFloatCalc {
	public int radix;

	public FatUFloatCalc(int radix) {
		this.radix = radix;
	}

	public void add(FatUFloat a, FatUFloat b) {
		a.normalize();
		b.normalize();

		a.sync(b);
		b.sync(a);

		new FatUIntCalc(radix).add(a.inner, b.inner);

		a.normalize();
	}

	public int sub(FatUFloat a, FatUFloat b) {
		a.normalize();
		b.normalize();

		a.sync(b);
		b.sync(a);

		int sign = new FatUIntCalc(radix).sub(a.inner, b.inner);

		a.normalize();

		return sign;
	}

	public FatUFloat mul(FatUFloat a, FatUFloat b) {
		a.normalize();
		b.normalize();

		FatUFloat answer = new FatUFloat(new FatUIntCalc(radix).mul(a.inner, b.inner), a.exponent + b.exponent);

		answer.normalize();

		return answer;
	}

	public FatUFloat div(FatUFloat a, FatUFloat b) {
		a.normalize();
		b.normalize();

		a.sync(b);
		b.sync(a);

		FatUFloat answer = new FatUFloat(new FatUIntCalc(radix).div(a.inner, b.inner), 0);

		answer.normalize();

		return answer;
	}
}
