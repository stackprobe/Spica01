package violet.labo.module.fatcalc;

public class FatEUInt {
	public FatUInt inner;
	public int exponent;

	public FatEUInt() {
		this(new FatUInt());
	}

	public FatEUInt(FatUInt inner) {
		this(inner, 0);
	}

	public FatEUInt(FatUInt inner, int exponent) {
		this.inner = inner;
		this.exponent = exponent;
	}

	public void normalize() {
		int start;

		for(start = 0; start < inner.figures.length && inner.figures[start] == 0; start++) {
			// noop
		}

		if(start < inner.figures.length) {
			int end;

			for(end = inner.figures.length; inner.figures[end - 1] == 0; end--) {
				// noop
			}

			if(0 < start || end < inner.figures.length) {
				int[] figuresNew = new int[end - start];
				System.arraycopy(inner.figures, start, figuresNew, 0, end - start);
				inner.figures = figuresNew;
				exponent += start;
			}
		}
		else {
			inner.figures = new int[0];
			exponent = 0;
		}
	}

	public void sync(FatEUInt another) {
		int count = exponent - another.exponent;

		if(0 < count) {
			int[] figuresNew = new int[inner.figures.length + count];
			System.arraycopy(inner.figures, 0, figuresNew, count, inner.figures.length);
			inner.figures = figuresNew;
			exponent -= count;
		}
	}
}
