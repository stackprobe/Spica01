package charlotte.tools;

public class D4Rect {
	public double l;
	public double t;
	public double w;
	public double h;

	public D4Rect() {
		// noop
	}

	public D4Rect(double l, double t, double w, double h) {
		this.l = l;
		this.t = t;
		this.w = w;
		this.h = h;
	}

	public static D4Rect ltrb(double l, double t, double r, double b) {
		return new D4Rect(l, t, r - l, b - t);
	}

	public double getR() {
		return this.l + this.w;
	}

	public double getB() {
		return this.t + this.h;
	}
}
