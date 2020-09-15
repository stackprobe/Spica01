package charlotte.tools;

import java.awt.Color;

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

	public I3Color withoutAlpha() {
		return new I3Color(this.r, this.g, this.b);
	}

	public D4Color toD4Color() {
		return new D4Color(
				this.r / 255.0,
				this.g / 255.0,
				this.b / 255.0,
				this.a / 255.0
				);
	}

	public Color toColor() {
		return new Color(this.r, this.g, this.b, this.a);
	}
}
