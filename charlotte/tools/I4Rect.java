package charlotte.tools;

public class I4Rect {
	public int l;
	public int t;
	public int w;
	public int h;

	public I4Rect() {
		// noop
	}

	public I4Rect(I2Point lt, I2Size size) {
		this.l = lt.x;
		this.t = lt.y;
		this.w = size.w;
		this.h = size.h;
	}

	public I4Rect(int l, int t, int w, int h) {
		this.l = l;
		this.t = t;
		this.w = w;
		this.h = h;
	}

	public static I4Rect ltrb(int l, int t, int r, int b) {
		return new I4Rect(l, t, r - l, b - t);
	}

	public int getR() {
		return this.l + this.w;
	}

	public int getB() {
		return this.t + this.h;
	}

	public I2Point toLT() {
		return new I2Point(this.l, this.t);
	}

	public I2Point toRT() {
		return new I2Point(this.getR(), this.t);
	}

	public I2Point toRB() {
		return new I2Point(this.getR(), this.getB());
	}

	public I2Point toLB() {
		return new I2Point(this.l, this.getB());
	}

	public I2Size toSize() {
		return new I2Size(this.w, this.h);
	}

	public D4Rect toD4Rect() {
		return new D4Rect(this.l, this.t, this.w, this.h);
	}
}
