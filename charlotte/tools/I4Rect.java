package charlotte.tools;

public class I4Rect {
	public int l;
	public int t;
	public int w;
	public int h;

	public I4Rect() {
		// noop
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
}
