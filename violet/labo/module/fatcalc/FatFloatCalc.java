package violet.labo.module.fatcalc;

public class FatFloatCalc {
	public int radix;

	public FatFloatCalc(int radix) {
		this.radix = radix;
	}

	public void add(FatFloat a, FatFloat b) {
		a.normalize();
		b.normalize();

		if(a.sign == b.sign) {
			new FatUFloatCalc(radix).add(a.inner, b.inner);
		}
		else {
			a.sign *= new FatUFloatCalc(radix).sub(a.inner, b.inner);
		}
	}

	public void sub(FatFloat a, FatFloat b) {
		a.normalize();
		b.normalize();

		b.sign *= -1;

		add(a, b);
	}

	public FatFloat mul(FatFloat a, FatFloat b) {
		a.normalize();
		b.normalize();

		return new FatFloat(new FatUFloatCalc(radix).mul(a.inner, b.inner), a.sign * b.sign);
	}

	public FatFloat div(FatFloat a, FatFloat b) {
		a.normalize();
		b.normalize();

		return new FatFloat(new FatUFloatCalc(radix).div(a.inner, b.inner), a.sign * b.sign);
	}
}
