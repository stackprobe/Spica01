package wb.t20190625;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class Test0002 {
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

	private static final int V_MUL = 4;
	private static final int IMG_W = V_MUL * 960;
	private static final int IMG_H = V_MUL * 480;

	private static BufferedImage _bi;
	private static Graphics _g;

	private static void main2() throws Exception {
		_bi = new BufferedImage(IMG_W, IMG_H, BufferedImage.TYPE_INT_ARGB);
		_g = _bi.getGraphics();

		_g.setColor(new Color(0, 0, 0, 255));
		_g.fillRect(0, 0, IMG_W, IMG_H);

		_g.setColor(new Color(255, 255, 255, 255));
		drawString("cerulean.charlotte", new Font("Ink Free", Font.PLAIN, 300), IMG_W / 2, IMG_H / 2 + 90, -0.5);
		//drawString("STACKPROBE", new Font("Impact", Font.PLAIN, 300), IMG_W / 2, IMG_H / 2 + 120, -0.5);

		save("C:/temp/Logo.png", "png");
	}

	private static void drawString(String str, Font font, int x, int y, double xRate) {
		_g.setFont(font);

		FontMetrics fm = _g.getFontMetrics();

		int w = fm.stringWidth(str);
		//int h = fm.getHeight();

		_g.drawString(str, (int)(x + w * xRate), y);
	}

	private static void fillCircle(double centerX, double centerY, double r) {
		for(int x = 0; x < IMG_W; x++) {
			for(int y = 0; y < IMG_H; y++) {
				double dX = x - centerX;
				double dY = y - centerY;
				double d = Math.sqrt(dX * dX + dY * dY);

				if(d < r) {
					drawPoint(x, y);
				}
			}
		}
	}

	private static void drawPoint(int x, int y) {
		_g.drawLine(x, y, x, y);
	}

	private static void save(String file, String format) throws Exception {
		try(FileOutputStream writer = new FileOutputStream(file)) {
			ImageIO.write(_bi, format, writer);
		}
		expand(file, IMG_W / V_MUL, IMG_H / V_MUL);
	}

	private static void expand(String file, int w, int h) throws Exception {
		Runtime.getRuntime().exec("C:/app/Kit/ImgTools/ImgTools.exe /rwf \"" + file + "\" /e " + w + " " + h).waitFor();
	}
}
