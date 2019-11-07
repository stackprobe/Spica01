package violet.kariimg;

import java.awt.Color;
import java.awt.Font;

import charlotte.tools.Canvas;
import charlotte.tools.Canvas2;
import charlotte.tools.DoubleTools;

public class MkKariImage {
	public Color frameColor = new Color(255, 255, 255);
	public Color backColor = Color.BLUE;
	public Color foreColor = new Color(255, 255, 255);
	public Color gradationColor = null; // null == disabled

	public static final int CORNER_SHARP = 1;
	public static final int CORNER_STAIR = 2;
	public static final int CORNER_CURVE = 3;
	public static final int CORNER_SLOPE = 4;

	public int corner = CORNER_SHARP;

	public boolean bigCorner = false;
	public int frameWidth = 10;
	public int width = 400;
	public int height = 200;
	public String fontName = "Impact";
	public int fontStyle = Font.PLAIN;
	public int fontSize = 100;

	//public String text = "FIRST LINE:SECOND LINE";
	public String text = "START";
	public int lineSpan = 10;
	public boolean mirror = false;

	public String destFile = "C:/temp/Output.png";

	// <---- prm

	private Canvas _canvas;

	public void perform() throws Exception {
		_canvas = new Canvas(width, height);

		_canvas.fill(frameColor);
		_canvas.fillRect(backColor, frameWidth, frameWidth, width - frameWidth * 2, height - frameWidth * 2);

		{
			int corner_w = frameWidth * 2;
			int corner_h = frameWidth * 2;

			if(bigCorner) {
				corner_w = Math.min(width, height) / 2;
				corner_h = Math.min(width, height) / 2;
			}

			processCorner(0, 0, corner_w, corner_h, 0);
			processCorner(width - corner_w, 0, corner_w, corner_h, 90);
			processCorner(width - corner_w, height - corner_h, corner_w, corner_h, 180);
			processCorner(0, height - corner_h, corner_w, corner_h, 270);
		}

		{
			String[] lines = text.split("[:]");
			Canvas c;

			if(lines.length == 1) {
				c = getStringCanvas(lines[0]);
			}
			else if(lines.length == 2) {
				Canvas ca = getStringCanvas(lines[0]);
				Canvas cb = getStringCanvas(lines[1]);

				c = joinStringCanvas(ca, cb);
			}
			else {
				throw new IllegalArgumentException();
			}
			_canvas.paste(c, (width - c.getWidth()) / 2, (height - c.getHeight()) / 2);
		}

		if(gradationColor != null) {
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					Color color = _canvas.get(x, y);
					double rate = (x + y) * 1.0 / (width + height - 2);

					rate *= gradationColor.getAlpha() / 255.0;

					_canvas.set(x, y, new Color(
							color.getRed()   + DoubleTools.toInt((gradationColor.getRed()   - color.getRed())   * rate),
							color.getGreen() + DoubleTools.toInt((gradationColor.getGreen() - color.getGreen()) * rate),
							color.getBlue()  + DoubleTools.toInt((gradationColor.getBlue()  - color.getBlue())  * rate),
							color.getAlpha()
							));
					// old
					/*
					_canvas.put(x, y, new Color(
							gradationColor.getRed(),
							gradationColor.getGreen(),
							gradationColor.getBlue(),
							DoubleTools.toInt(gradationColor.getAlpha() * rate)
							));
							*/
				}
			}
		}

		if(mirror) {
			for(int x = 0; x < width / 2; x++) {
				for(int y = 0; y < height; y++) {
					Color tmp = _canvas.get(x, y);
					_canvas.set(x, y, _canvas.get(width - 1 - x, y));
					_canvas.set(width - 1 - x, y, tmp);
				}
			}
		}

		_canvas.save(destFile);
	}

	private Canvas joinStringCanvas(Canvas ca, Canvas cb) {
		int w = Math.max(ca.getWidth(), cb.getWidth());
		int h = ca.getHeight() + lineSpan + cb.getHeight();

		Canvas c = new Canvas(w, h);

		c.fill(backColor);
		c.paste(ca, (w - ca.getWidth()) / 2, 0);
		c.paste(cb, (w - cb.getWidth()) / 2, ca.getHeight() + lineSpan);

		return c;
	}

	private Canvas getStringCanvas(String line) {
		Canvas c = new Canvas(width, height * 2);
		Canvas2 c2;

		// 一旦透明なイメージに文字列を描画してから貼り付けしないと、アンチエイリアスしたところが変になることがある。@ 2019.7.24

		Color dummyBackColor = new Color(0, 0, 0, 0);

		c.fill(dummyBackColor);
		c2 = c.toCanvas2();
		c2.drawString(line, new Font(fontName, fontStyle, fontSize), foreColor, width / 2, height);
		c = c2.toCanvas();
		final Canvas f_c = c;
		c = c.cutoutUnmatch(pt -> f_c.get(pt.x, pt.y).equals(dummyBackColor) == false);

		Canvas cc = new Canvas(c.getWidth(), c.getHeight());
		cc.fill(backColor);
		cc.cover(c, 0, 0);

		return cc;
	}

	private void processCorner(int l, int t, int w, int h, int rotDeg) {
		Canvas canvas = new Canvas(w, h);

		switch(corner) {
		case CORNER_SHARP:
			canvas.fill(backColor);
			canvas.fillRect(frameColor, 0, 0, w, frameWidth);
			canvas.fillRect(frameColor, 0, 0, frameWidth, h);
			break;

		case CORNER_STAIR:
			canvas.fill(new Color(0, 0, 0, 0));
			canvas.fillRect(frameColor, 0, h - frameWidth, w, frameWidth);
			canvas.fillRect(frameColor, w - frameWidth, 0, frameWidth, h);
			break;

		case CORNER_CURVE:
			canvas.fill(new Color(0, 0, 0, 0));
			canvas.drawCircle(backColor, w - 0.5, h - 0.5, w);
			canvas.drawCircle(frameColor, w - 0.5, h - 0.5, w, w - frameWidth);
			break;

		case CORNER_SLOPE:
			canvas.fill(new Color(0, 0, 0, 0));

			{
				Canvas2 c2 = canvas.toCanvas2();

				c2.fillPolygon(
						backColor,
						new int[] {
								w,
								0,
								w,
						},
						new int[] {
								0,
								h,
								h,
						}
						);

				c2.fillPolygon(
						frameColor,
						new int[] {
								w,
								0,
								frameWidth,
								w,
						},
						new int[] {
								0,
								h,
								h,
								frameWidth,
						}
						);

				canvas = c2.toCanvas();
			}

			break;

		default:
			throw null;
		}
		canvas = canvas.rotate(rotDeg);

		_canvas.paste(canvas, l, t);
	}
}
