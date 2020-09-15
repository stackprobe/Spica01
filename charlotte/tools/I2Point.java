package charlotte.tools;

public class I2Point {
	public int x;
	public int y;

	public I2Point() {
		// noop
	}

	public I2Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public D2Point toD2Point() {
		return new D2Point(this.x, this.y);
	}
}
