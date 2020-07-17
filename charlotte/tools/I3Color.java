package charlotte.tools;

public class I3Color {
	public int r;
	public int g;
	public int b;

	public I3Color() {
		// noop
	}

	public I3Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Override
	public String toString() {
		return String.format("%02x%02x%02x", this.r, this.g, this.b);
	}
}
