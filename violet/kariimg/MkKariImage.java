package violet.kariimg;

import java.awt.Color;
import java.awt.Font;

import charlotte.options.Canvas;
import charlotte.options.Canvas2;

public class MkKariImage {
	public Color frameColor = new Color(255, 255, 255);
	public Color backColor = Color.BLUE;
	public Color foreColor = new Color(255, 255, 255);

	public static final int CORNER_SHARP = 1;
	public static final int CORNER_STAIR = 2;
	public static final int CORNER_CURVE = 3;

	public int corner = CORNER_SHARP;

	public int frameWidth = 10;
	public int width = 400;
	public int height = 200;
	public String fontName = "Impact";
	public int fontStyle = Font.PLAIN;
	public int fontSize = 100;

	//public String text = "FIRST LINE:SECOND LINE";
	public String text = "START";
	public int lineSpan = 10;

	public String destFile = "C:/temp/Output.png";

	// <---- prm

	private Canvas _canvas;

	public void perform() throws Exception {
		_canvas = new Canvas(width, height);

		_canvas.fill(frameColor);
		_canvas.fillRect(backColor, frameWidth, frameWidth, width - frameWidth * 2, height - frameWidth * 2);

		{
			final int corner_w = frameWidth * 2;
			final int corner_h = frameWidth * 2;

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

		c.fill(backColor);
		c2 = c.toCanvas2();
		c2.drawString(line, new Font(fontName, fontStyle, fontSize), foreColor, width / 2, height);
		c = c2.toCanvas();
		c = c.cutoutUnmatch(v -> v.equals(backColor) == false);

		return c;
	}

	private void processCorner(int l, int t, int w, int h, int rotDeg) {
		Canvas canvas = new Canvas(w, h);

		switch(corner) {
		case CORNER_SHARP:
			canvas.fill(backColor);
			canvas.fillRect(frameColor, 0, 0, w, h / 2);
			canvas.fillRect(frameColor, 0, 0, w / 2, h);
			break;

		case CORNER_STAIR:
			canvas.fill(new Color(0, 0, 0, 0));
			canvas.fillRect(frameColor, 0, h / 2, w, h / 2);
			canvas.fillRect(frameColor, w / 2, 0, w / 2, h);
			break;

		case CORNER_CURVE:
			canvas.fill(new Color(0, 0, 0, 0));
			canvas.drawCircle(backColor, w - 0.5, h - 0.5, w * 0.9);
			canvas.drawCircle(frameColor, w - 0.5, h - 0.5, w, w / 2);
			break;

		default:
			throw null;
		}
		canvas = canvas.rotate(rotDeg);

		_canvas.paste(canvas, l, t);
	}
}
