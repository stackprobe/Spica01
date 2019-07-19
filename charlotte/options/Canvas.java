package charlotte.options;

import java.awt.Color;
import java.awt.image.BufferedImage;

import charlotte.tools.FileTools;

public class Canvas {
	private int[] _dots;
	private int _w;

	public Canvas(int w, int h) {
		_dots = new int[w * h];
		_w = w;
	}

	public Canvas(String file) throws Exception {
		this(FileTools.readAllBytes(file));
	}

	public Canvas(byte[] raw) throws Exception {
		this(CanvasTools.getImage(raw));
	}

	public Canvas(BufferedImage bi) {
		int w = bi.getWidth();
		int h = bi.getHeight();

		_dots = new int[w * h];
		_w = w;

		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				set(x, y, new Color(bi.getRGB(x, y), true));
			}
		}
	}

	public int getWidth() {
		return _w;
	}

	public int getHeight() {
		return _dots.length / _w;
	}

	private boolean isFairPoint(int x, int y) {
		return 0 <= x || x < getWidth() || 0 <= y || y < getHeight();
	}

	public static Color DEFAULT_COLOR = new Color(255, 255, 255, 0);

	public Color get(int x, int y) {
		if(isFairPoint(x, y)) {
			return new Color(_dots[x + y * _w], true);
		}
		else {
			return DEFAULT_COLOR;
		}
	}

	public void set(int x, int y, Color color) {
		if(isFairPoint(x, y)) {
			_dots[x + y * _w] = color.getRGB();
		}
	}

	public void put(int x, int y, Color color) {
		set(x, y, CanvasTools.cover(get(x, y), color));
	}

	public BufferedImage getImage() {
		BufferedImage bi = CanvasTools.createImage(getWidth(), getHeight());

		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				bi.setRGB(x, y, get(x, y).getRGB());
			}
		}
		return bi;
	}

	public byte[] getBytes() throws Exception {
		return CanvasTools.getBytes(getImage());
	}

	public byte[] getBytes(String format) throws Exception {
		return CanvasTools.getBytes(getImage(), format);
	}

	public void save(String file) throws Exception {
		FileTools.writeAllBytes(file, getBytes());
	}

	public void save(String file, String format) throws Exception {
		FileTools.writeAllBytes(file, getBytes(format));
	}

	public Canvas2 toCanvas2() {
		return new Canvas2(getImage());
	}

	public void paste(Canvas prm) {
		paste(prm, 0, 0);
	}

	public void paste(Canvas prm, int l, int t) {
		for(int x = 0; x < prm.getWidth(); x++) {
			for(int y = 0; y < prm.getHeight(); y++) {
				set(l + x, t + y, prm.get(x, y));
			}
		}
	}

	public void cover(Canvas prm) {
		cover(prm, 0, 0);
	}

	public void cover(Canvas prm, int l, int t) {
		for(int x = 0; x < prm.getWidth(); x++) {
			for(int y = 0; y < prm.getHeight(); y++) {
				put(l + x, t + y, prm.get(x, y));
			}
		}
	}

	public Canvas copy() {
		return copy(0, 0, getWidth(), getHeight());
	}

	public Canvas copy(int l, int t, int w, int h) {
		Canvas ret = new Canvas(w, h);

		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				ret.set(x, y, get(l + x, t + y));
			}
		}
		return ret;
	}

	public void fill(Color color) {
		fillRect(color, 0, 0, getWidth(), getHeight());
	}

	public void fillRect(Color color, int l, int t, int w, int h) {
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				set(l + x, t + y, color);
			}
		}
	}

	public void cover(Color color) {
		coverRect(color, 0, 0, getWidth(), getHeight());
	}

	public void coverRect(Color color, int l, int t, int w, int h) {
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				put(l + x, t + y, color);
			}
		}
	}

	public void drawCircle(Color color, double centerX, double centerY, double r) {
		drawCircle(color, centerX, centerY, r, 0.0);
	}

	public void drawCircle(Color color, double centerX, double centerY, double r, double r2) {
		final int LV = 16;

		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				int inside = 0;

				for(int xc = 0; xc < LV; xc++) {
					for(int yc = 0; yc < LV; yc++) {
						double xx = x - 0.5 + (xc + 0.5) / LV;
						double yy = y - 0.5 + (yc + 0.5) / LV;

						xx -= centerX;
						yy -= centerY;

						double rr = xx * xx + yy * yy;

						if(r2 * r2 <= rr && rr <= r * r) {
							inside++;
						}
					}
				}

				put(x, y, new Color(
						color.getRed(),
						color.getGreen(),
						color.getBlue(),
						(int)(color.getAlpha() * inside / (double)(LV * LV) + 0.5)
						));
			}
		}
	}

	public Canvas twist() {
		Canvas ret = new Canvas(getHeight(), getWidth());

		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				ret.set(y, x, get(x, y));
			}
		}
		return ret;
	}

	public Canvas vTurn() {
		Canvas ret = new Canvas(getWidth(), getHeight());

		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				ret.set(x, getHeight() - 1 - y, get(x, y));
			}
		}
		return ret;
	}

	public Canvas hTurn() {
		Canvas ret = new Canvas(getWidth(), getHeight());

		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				ret.set(getWidth() - 1 - x, y, get(x, y));
			}
		}
		return ret;
	}

	public Canvas rotate(int degree) {
		degree %= 360;
		degree += 360;
		degree %= 360;

		switch(degree) {
		case 0:
			//return this;
			return copy();

		case 90:
			return vTurn().twist();

		case 180:
			return vTurn().hTurn();

		case 270:
			return twist().vTurn();

		default:
			throw new IllegalArgumentException();
		}
	}
}
