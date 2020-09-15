package charlotte.tools;

import java.awt.Color;

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

	public I4Color withAlpha() {
		return withAlpha(255);
	}

	public I4Color withAlpha(int a) {
		return new I4Color(this.r, this.g, this.b, a);
	}

	public D3Color toD3Color() {
		return new D3Color(
				this.r / 255.0,
				this.g / 255.0,
				this.b / 255.0
				);
	}

	public Color toColor() {
		return new Color(this.r, this.g, this.b);
	}
}
