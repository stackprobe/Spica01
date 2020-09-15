package charlotte.tools;

public class D4Color {
	public double r;
	public double g;
	public double b;
	public double a;

	public D4Color() {
		// noop
	}

	public D4Color(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public D3Color withoutAlpha() {
		return new D3Color(this.r, this.g, this.b);
	}

	public I4Color toI4Color() {
		return new I4Color(
				DoubleTools.toInt(this.r * 255.0),
				DoubleTools.toInt(this.g * 255.0),
				DoubleTools.toInt(this.b * 255.0),
				DoubleTools.toInt(this.a * 255.0)
				);
	}
}
