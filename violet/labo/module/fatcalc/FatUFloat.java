package violet.labo.module.fatcalc;

public class FatUFloat {
	public FatEUInt inner;
	public int decimalPoint;

	public FatUFloat() {
		this(new FatEUInt());
	}

	public FatUFloat(FatEUInt inner) {
		this(inner, 0);
	}

	public FatUFloat(FatEUInt inner, int decimalPoint) {
		this.inner = inner;
		this.decimalPoint = decimalPoint;
	}

	public void normalize() {
		inner.normalize();

		int count = Math.min(inner.exponent, decimalPoint);

		inner.exponent -= count;
		decimalPoint -= count;
	}

	public void sync(FatUFloat another) {
		int count = decimalPoint - another.decimalPoint;

		if(count < 0) {
			inner.exponent -= count;
			decimalPoint -= count;
		}
	}
}
