package charlotte.tools;

public class I2Size {
	public int w;
	public int h;

	public I2Size() {
		// noop
	}

	public I2Size(int w, int h) {
		this.w = w;
		this.h = h;
	}

	public D2Size toD2Size() {
		return new D2Size(this.w, this.h);
	}
}
