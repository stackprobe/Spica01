package charlotte.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Canvas2 {
	private BufferedImage _bi;

	public Canvas2(int w, int h) {
		if(CanvasTools.isFairImageSize(w, h) == false) {
			throw new IllegalArgumentException();
		}
		_bi = CanvasTools.createImage(w, h);
	}

	public Canvas2(String file) throws Exception {
		this(FileTools.readAllBytes(file));
	}

	public Canvas2(byte[] raw) throws Exception {
		this(CanvasTools.getImage(raw));
	}

	public Canvas2(BufferedImage bi) {
		_bi = bi;
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

	public byte[] getBytes() throws Exception {
		return CanvasTools.getBytes(_bi);
	}

	public byte[] getBytes(String format) throws Exception {
		return CanvasTools.getBytes(_bi, format);
	}

	public void save(String file) throws Exception {
		FileTools.writeAllBytes(file, getBytes());
	}

	public void save(String file, String format) throws Exception {
		FileTools.writeAllBytes(file, getBytes(format));
	}

	public Canvas toCanvas() {
		return new Canvas(_bi);
	}

	public Graphics getGraphics() {
		Graphics2D g = (Graphics2D)_bi.getGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		return g;
	}

	// memo: g.drawString() \u306e (x, y) \u306f\u3001\u63cf\u753b\u3057\u305f\u6587\u5b57\u5217\u306e\u5de6\u4e0b\u306e\u5ea7\u6a19\u3063\u307d\u3044\u3002\u4f59\u767d\u306f\u5165\u3089\u306a\u3044\u306e\u3067\u6587\u5b57\u672c\u4f53\u306f\u5ea7\u6a19\u306e\u3059\u3050\u8fd1\u304f\u3002

	// xRate == -0.5 ==> \u4e2d\u592e\u306b\u5bc4\u3063\u3066\u304f\u308c\u308b\u3002
	// yRate == xxxx ==> \u8981\u8abf\u6574

	public static final double DRAW_STRING_DEFAULT_X_RATE = -0.5;
	public static final double DRAW_STRING_DEFAULT_Y_RATE = 0.25; // \u30e1\u30a4\u30ea\u30aa\u5411\u3051

	public void drawString(String str, Font font, Color color, int x, int y) {
		drawString(str, font, color, x, y, DRAW_STRING_DEFAULT_X_RATE);
	}

	public void drawString(String str, Font font, Color color, int x, int y, double xRate) {
		drawString(str, font, color, x, y, xRate, DRAW_STRING_DEFAULT_Y_RATE);
	}

	public void drawString(String str, Font font, Color color, int x, int y, double xRate, double yRate) {
		Graphics g = getGraphics();

		g.setFont(font);

		FontMetrics fm = g.getFontMetrics();

		int w = fm.stringWidth(str);
		int h = fm.getHeight();

		g.setColor(color);
		g.drawString(str, (int)(x + w * xRate + 0.5), (int)(y + h * yRate + 0.5));

		g.dispose();
		g = null;
	}

	public void drawPolygon(Color color, int[] xPoints, int[] yPoints) {
		int nPoints = xPoints.length;

		if(nPoints != yPoints.length) {
			throw new IllegalArgumentException();
		}
		Graphics g = getGraphics();

		g.setColor(color);
		g.drawPolygon(xPoints, yPoints, nPoints);

		g.dispose();
		g = null;
	}

	public void fillPolygon(Color color, int[] xPoints, int[] yPoints) {
		int nPoints = xPoints.length;

		if(nPoints != yPoints.length) {
			throw new IllegalArgumentException();
		}
		Graphics g = getGraphics();

		g.setColor(color);
		g.fillPolygon(xPoints, yPoints, nPoints);

		g.dispose();
		g = null;
	}
}
