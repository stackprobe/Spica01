package charlotte.tools;

public class I4Color {
	public int r;
	public int g;
	public int b;
	public int a;

	public I4Color() {
		// noop
	}

	public I4Color(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	@Override
	public String toString() {
		return String.format("%02x%02x%02x%02x", this.r, this.g, this.b, this.a);
	}
}
