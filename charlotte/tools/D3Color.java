package charlotte.tools;

public class D3Color {
	public double r;
	public double g;
	public double b;

	public D3Color() {
		// noop
	}

	public D3Color(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public D4Color withAlpha() {
		return withAlpha(1.0);
	}

	public D4Color withAlpha(double a) {
		return new D4Color(this.r, this.g, this.b, a);
	}

	public I3Color toI3Color() {
		return new I3Color(
				DoubleTools.toInt(this.r * 255.0),
				DoubleTools.toInt(this.g * 255.0),
				DoubleTools.toInt(this.b * 255.0)
				);
	}
}
