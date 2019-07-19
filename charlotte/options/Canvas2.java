package charlotte.options;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import charlotte.tools.FileTools;

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

		g.dispose();
		g = null;
	}
}
