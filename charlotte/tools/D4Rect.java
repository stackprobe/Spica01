package charlotte.tools;

public class D4Rect {
	public double l;
	public double t;
	public double w;
	public double h;

	public D4Rect() {
		// noop
	}

	public D4Rect(D2Point lt, D2Size size) {
		this.l = lt.x;
		this.t = lt.y;
		this.w = size.w;
		this.h = size.h;
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

	public D2Point toLT() {
		return new D2Point(this.l, this.t);
	}

	public D2Point toRT() {
		return new D2Point(this.getR(), this.t);
	}

	public D2Point toRB() {
		return new D2Point(this.getR(), this.getB());
	}

	public D2Point toLB() {
		return new D2Point(this.l, this.getB());
	}

	public D2Size toSize() {
		return new D2Size(this.w, this.h);
	}

	public I4Rect toI4Rect() {
		return new I4Rect(
				DoubleTools.toInt(this.l),
				DoubleTools.toInt(this.t),
				DoubleTools.toInt(this.w),
				DoubleTools.toInt(this.h)
				);
	}
}
