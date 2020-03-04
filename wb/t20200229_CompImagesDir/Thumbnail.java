package wb.t20200229_CompImagesDir;

import java.awt.Color;
import java.io.File;
import java.util.Map;

import charlotte.tools.Canvas;
import charlotte.tools.MapTools;

public class Thumbnail {
	public static final int THUMB_W = 100;
	public static final int THUMB_H = 100;

	private static long _identCounter = 0;

	private long _ident;
	private File _matrixF;
	private double _brightness;

	public Thumbnail(File f) throws Exception {
		System.out.println("Make Thumbnail " + f.getCanonicalPath()); // test

		_ident = _identCounter++;
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
			_brightness = b / (255.0 * 3 * THUMB_W * THUMB_H);
		}

		System.out.println("Make Thumbnail OK!"); // test
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

	private static Map<String, Double> _differentMap = MapTools.<Double>create();

	public static double getDifferent(Thumbnail a, Thumbnail b) throws Exception {
		String k = a._ident + "_" + b._ident;

		if(_differentMap.containsKey(k) == false) {
			_differentMap.put(k, getDifferent_noCache(a, b));
		}
		return _differentMap.get(k);
	}

	private static double getDifferent_noCache(Thumbnail a, Thumbnail b) throws Exception {
		return getDifferent(a.getMatrix(), b.getMatrix());
	}

	private static double getDifferent(Canvas a, Canvas b) {
		double diffValue = 0.0;

		for (int x = 0; x < Thumbnail.THUMB_W; x++) {
			for (int y = 0; y < Thumbnail.THUMB_H; y++) {
				Color color1 = a.get(x, y);
				Color color2 = b.get(x, y);

				diffValue += getDifferent(color1.getRed(), color2.getRed());
				diffValue += getDifferent(color1.getGreen(), color2.getGreen());
				diffValue += getDifferent(color1.getBlue(), color2.getBlue());
			}
		}
		return diffValue;
	}

	private static double getDifferent(int a, int b) {
		double d = (double)a - b;

		d /= 255.0;

		d *= d;
		d *= d;
		d *= d;

		return d;
	}
}
