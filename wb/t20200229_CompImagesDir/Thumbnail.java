package wb.t20200229_CompImagesDir;

import java.io.File;

import charlotte.tools.Canvas;

public class Thumbnail {
	public static final int THUMB_W = 100;
	public static final int THUMB_H = 100;

	private File _matrixF;
	private double _brightness;

	public Thumbnail(File f) throws Exception {
		_matrixF = new File(Ground.wd.makePath() + ".bmp");

		expand(f.getCanonicalPath(), _matrixF.getCanonicalPath(), THUMB_W, THUMB_H);

		{
			Canvas matrix = getMatrix();
			int b = 0;

			for(int x = 0; x < THUMB_W; x++) {
				for(int y = 0; y < THUMB_H; y++) {
					b +=
							matrix.get(x, y).getRed() +
							matrix.get(x, y).getGreen() +
							matrix.get(x, y).getBlue();
				}
			}
			_brightness = b / 255.0 * 3 * THUMB_W * THUMB_H;
		}
	}

	public Canvas getMatrix() throws Exception {
		return new Canvas(_matrixF.getCanonicalPath());
	}

	public double getBrightness() {
		return _brightness;
	}

	private static void expand(String rFile, String wFile, int w, int h) throws Exception {
		Runtime.getRuntime().exec("C:/app/Kit/ImgTools/ImgTools.exe /rf \"" + rFile + "\" /wf \"" + wFile + "\" /e " + w + " " + h).waitFor();
	}
}
