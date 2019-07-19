package charlotte.options;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import charlotte.tools.FileTools;

public class Canvas {
	private BufferedImage _bi;

	public Canvas(int w, int h) {
		this(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
	}

	public Canvas(BufferedImage bi) {
		_bi = bi;

		fill(new Color(100, 128, 150, 0));
	}

	public int getWidth() {
		return _bi.getWidth();
	}

	public int getHeight() {
		return _bi.getHeight();
	}

	public BufferedImage getImage() {
		return _bi;
	}

	public Graphics getGraphics() {
		Graphics2D g = (Graphics2D)_bi.getGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		return g;
	}

	public byte[] getBytes(String format) throws Exception {
		try(ByteArrayOutputStream mem = new ByteArrayOutputStream()) {
			ImageIO.write(_bi, format, mem);
			return mem.toByteArray();
		}
	}

	public byte[] getBytes() throws Exception {
		return getBytes("png");
	}

	public void save(String file) throws Exception {
		FileTools.writeAllBytes(file, getBytes());
	}

	public Canvas load(String file) throws Exception {
		return load(FileTools.readAllBytes(file));
	}

	public Canvas load(byte[] raw) throws Exception {
		try(ByteArrayInputStream mem = new ByteArrayInputStream(raw)) {
			BufferedImage bi = ImageIO.read(mem);
			Canvas ret = new Canvas(bi.getWidth(), bi.getHeight());
			ret.paste(new Canvas(bi));
			return ret;
		}
	}

	public void paste(Canvas src) {
		paste(src, 0, 0);
	}

	public void paste(Canvas src, int l, int t) {
		for(int x = 0; x < src.getWidth(); x++) {
			for(int y = 0; y < src.getHeight(); y++) {
				set(l + x, t + y, src.get(x, y));
			}
		}
	}

	public void overwrap(Canvas src) {
		overwrap(src, 0, 0);
	}

	public void overwrap(Canvas src, int l, int t) {
		getGraphics().drawImage(
				src.getImage(),
				l,
				t,
				l + src.getWidth(),
				t + src.getHeight(),
				0,
				0,
				src.getWidth(),
				src.getHeight(),
				null
				);
	}

	public Canvas copy() {
		return copy(0, 0, getWidth(), getHeight());
	}

	public Canvas copy(int l, int t, int w, int h) {
		Canvas ret = new Canvas(w, h);

		ret.getImage().getGraphics().drawImage(
				_bi,
				0,
				0,
				w,
				h,
				l,
				t,
				l + w,
				t + h,
				null
				);

		return ret;
	}

	public Color get(int x, int y) {
		return new Color(_bi.getRGB(x, y));
	}

	public void set(int x, int y, Color color) {
		_bi.setRGB(x, y, color.getRGB());
	}

	public void fill(Color color) {
		fillRect(color, 0, 0, getWidth(), getHeight());
	}

	public void fillRect(Color color, int l, int t, int w, int h) {
		Graphics g = getGraphics();

		g.setColor(color);
		g.fillRect(l, t, w, h);
	}

	public void fillOval(Color color, int l, int t, int w, int h) {
		Graphics g = getGraphics();

		g.setColor(color);
		g.fillOval(l, t, w, h);
	}

	public void drawString(String str, Font font, Color color, int x, int y) {
		drawString(str, font, color, x, y, -0.5);
	}

	public void drawString(String str, Font font, Color color, int x, int y, double xRate) {
		Graphics g = getGraphics();

		g.setFont(font);

		FontMetrics fm = g.getFontMetrics();

		int w = fm.stringWidth(str);

		g.setColor(color);
		g.drawString(str, (int)(x + w * xRate), y);
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
