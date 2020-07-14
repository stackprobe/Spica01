package wb.t20190615;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class MkFlowerPng {
	public static void main(String[] args) {
		try {
			main2();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static BufferedImage _bi;

	private static void main2() throws Exception {
		int w = 320;
		int h = 320;

		_bi = ImageIO.read(new File(
				"C:/wb2/20191201_html/HtmlTests/HtmlTest17/res/src/flower301.png"
				));

		writeFlower("C:/temp/flower301-00.png", 0, 0, w, h);
		writeFlower("C:/temp/flower301-10.png", w, 0, w, h);
		writeFlower("C:/temp/flower301-01.png", 0, h, w, h);
		writeFlower("C:/temp/flower301-11.png", w, h, w, h);

		_bi = ImageIO.read(new File(
				"C:/wb2/20191201_html/HtmlTests/HtmlTest17/res/src/flower302.png"
				));

		writeFlower("C:/temp/flower302-00.png", 0, 0, w, h);
		writeFlower("C:/temp/flower302-10.png", w, 0, w, h);
		writeFlower("C:/temp/flower302-01.png", 0, h, w, h);
		writeFlower("C:/temp/flower302-11.png", w, h, w, h);
	}

	private static void writeFlower(String wFile, int l, int t, int w, int h) throws Exception {
		int r = l + w - 1;
		int b = t + h - 1;

		int x1 = r;
		int x2 = l;
		int y1 = b;
		int y2 = t;

		for(int x = l; x <= r; x++) {
			for(int y = t; y <= b; y++) {
				int argb = _bi.getRGB(x, y);

				int a = (argb >>> 24) & 0xff;

				System.out.println("a: " + a); // test

				if(a != 0) {
					x1 = Math.min(x1, x);
					x2 = Math.max(x2, x);
					y1 = Math.min(y1, y);
					y2 = Math.max(y2, y);
				}
			}
		}

		l = x1;
		r = x2;
		t = y1;
		b = y2;

		System.out.println("LTRB: " + l + ", " + t + ", " + r + ", " + b); // test

		int MARGIN = 10;

		l -= MARGIN;
		t -= MARGIN;
		r += MARGIN;
		b += MARGIN;

		w = r - l + 1;
		h = b - t + 1;

		BufferedImage img = _bi.getSubimage(l, t, w, h);

		try(FileOutputStream writer = new FileOutputStream(wFile)) {
			ImageIO.write(img, "png", writer);
		}
	}
}
