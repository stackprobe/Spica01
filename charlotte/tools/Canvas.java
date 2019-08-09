package charlotte.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Canvas {
	private int[] _dots;
	private int _w;

	public Canvas(int w, int h) {
		if(CanvasTools.isFairImageSize(w, h) == false) {
			throw new IllegalArgumentException();
		}
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
		return 0 <= x && x < getWidth() && 0 <= y && y < getHeight();
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

	public Canvas copy(I4Rect rect) {
		return copy(rect.l, rect.t, rect.w, rect.h);
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

	public void fillRect(Color color, I4Rect rect) {
		fillRect(color, rect.l, rect.t, rect.w, rect.h);
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

	public void coverRect(Color color, I4Rect rect) {
		coverRect(color, rect.l, rect.t, rect.w, rect.h);
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

	public Canvas cutoutUnmatch(Predicate<I2Point> match) {
		return copy(getRectMatch(match));
	}

	public I4Rect getRectMatch(Predicate<I2Point> match) {
		int l = Integer.MAX_VALUE;
		int t = Integer.MAX_VALUE;
		int r = -1;
		int b = -1;

		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				if(match.test(new I2Point(x, y))) {
					l = Math.min(l, x);
					t = Math.min(t, y);
					r = Math.max(r, x);
					b = Math.max(b, y);
				}
			}
		}
		if(r == -1) {
			throw new RTError("no matched pixels");
		}
		int w = r - l + 1;
		int h = b - t + 1;

		return new I4Rect(l, t, w, h);
	}

	public I4Rect getRectSpread(int startX, int startY, Predicate<I2Point> match) {
		int[] l = new int[] { Integer.MAX_VALUE };
		int[] t = new int[] { Integer.MAX_VALUE };
		int[] r = new int[] { -1 };
		int[] b = new int[] { -1 };

		this.spread(startX, startY, pt -> {
			if(match.test(pt)) {
				int x = pt.x;
				int y = pt.y;

				l[0] = Math.min(l[0], x);
				t[0] = Math.min(t[0], y);
				r[0] = Math.max(r[0], x);
				b[0] = Math.max(b[0], y);

				return true;
			}
			return false;
		});
		if(r[0] == -1) {
			throw new RTError("no matched pixels");
		}
		int w = r[0] - l[0] + 1;
		int h = b[0] - t[0] + 1;

		return new I4Rect(l[0], t[0], w, h);
	}

	public I4Rect getRectSameColor(int startX, int startY) {
		Color targetColor = this.get(startX, startY);

		return this.getRectSpread(startX, startY, pt -> {
			int x = pt.x;
			int y = pt.y;

			return this.get(x, y) == targetColor;
		});
	}

	public void fillSameColor(int startX, int startY, Color color) {
		spreadSameColor(startX, startY, pt -> set(pt.x, pt.y, color));
	}

	public void spreadSameColor(int startX, int startY, Consumer<I2Point> reaction) {
		Color targetColor = get(startX, startY);

		spread(startX, startY, pt -> {
			int x = pt.x;
			int y = pt.y;

			if(get(x, y).equals(targetColor)) {
				reaction.accept(pt);
				return true;
			}
			return false;
		});
	}

	public void spread(int startX, int startY, Predicate<I2Point> match) {
		BitTable reachedMap = new BitTable(this.getWidth(), this.getHeight());
		IQueue<I2Point> pts = new QueueUnit<I2Point>();

		pts.enqueue(new I2Point(startX, startY));

		while(pts.hasElements()) {
			I2Point pt = pts.dequeue();
			int x = pt.x;
			int y = pt.y;

			if(isFairPoint(x, y) && reachedMap.getBit(x, y) == false && match.test(pt)) {
				reachedMap.setBit(x, y, true);
				pts.enqueue(new I2Point(x - 1, y));
				pts.enqueue(new I2Point(x + 1, y));
				pts.enqueue(new I2Point(x, y - 1));
				pts.enqueue(new I2Point(x, y + 1));
			}
		}
	}
}
