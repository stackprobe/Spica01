package violet.kariimg;

import java.awt.Color;

import charlotte.options.Canvas;

public class MkKariImage {
	public Color frameColor = new Color(255, 255, 255);
	public Color backColor = new Color(255, 255, 255, 0);
	public Color foreColor = new Color(255, 255, 255);

	public int frameWidth = 10;
	public int width = 800;
	public int height = 600;
	public int fontSize = 16;

	public String text = "START";

	public static final int CORNER_SHARP = 1;
	public static final int CORNER_STAIR = 2;
	public static final int CORNER_CURVE = 3;

	public int corner = CORNER_SHARP;

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

		_canvas.save(destFile);
	}

	private void processCorner(int l, int t, int w, int h, int rotDeg) {
		Canvas canvas = new Canvas(w, h);

		canvas.fill(backColor);

		switch(corner) {
		case CORNER_SHARP:
			canvas.fillRect(frameColor, 0, 0, w, h / 2);
			canvas.fillRect(frameColor, 0, 0, w / 2, h);
			break;

		case CORNER_STAIR:
			canvas.fillRect(frameColor, 0, h / 2, w, h / 2);
			canvas.fillRect(frameColor, w / 2, 0, w / 2, h);
			break;

		case CORNER_CURVE:
			break;

		default:
			throw null;
		}
		canvas = canvas.rotate(rotDeg);

		_canvas.paste(canvas, l, t);
	}
}
