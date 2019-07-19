package charlotte.options;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

public class CanvasTools {
	public static boolean isFairImageSize(int w, int h) {
		return 1 <= w && w <= 10000 && 1 <= h && h <= 10000 && w * h <= 10000000;
	}

	public static BufferedImage createImage(int w, int h) {
		if(isFairImageSize(w, h) == false) {
			throw new IllegalArgumentException();
		}
		return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	}

	public static BufferedImage getImage(byte[] raw) throws Exception {
		try(ByteArrayInputStream mem = new ByteArrayInputStream(raw)) {
			return ImageIO.read(mem);
		}
	}

	public static byte[] getBytes(BufferedImage bi) throws Exception {
		return getBytes(bi, "png");
	}

	public static byte[] getBytes(BufferedImage bi, String format) throws Exception {
		try(ByteArrayOutputStream mem = new ByteArrayOutputStream()) {
			ImageIO.write(bi, format, mem);
			return mem.toByteArray();
		}
	}

	public static Color cover(Color back, Color fore) {
		int fa = fore.getAlpha();
		int ba = back.getAlpha();

		ba = (int)((ba * (255 - fa)) / 255.0 + 0.5);

		return new Color(
				(int)((ba * back.getRed()   + fa * fore.getRed())   / (double)(ba + fa) + 0.5),
				(int)((ba * back.getGreen() + fa * fore.getGreen()) / (double)(ba + fa) + 0.5),
				(int)((ba * back.getBlue()  + fa * fore.getBlue())  / (double)(ba + fa) + 0.5),
				ba + fa
				);
	}
}
