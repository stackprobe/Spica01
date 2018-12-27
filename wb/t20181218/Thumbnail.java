package wb.t20181218;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Thumbnail {
	private int[][] _matrix;

	public Thumbnail(int w, int h) {
		_matrix = new int[w][];

		for(int x = 0; x < w; x++) {
			_matrix[x] = new int[h];
		}
	}

	public int width() {
		return _matrix.length;
	}

	public int height() {
		return _matrix[0].length;
	}

	public void setFile(String file) throws Exception {
		set(ImageIO.read(new File(file)));
	}

	public void set(Thumbnail src) {
		set(src.getImage());
	}

	public void set(Image img) {
		BufferedImage img2 = new BufferedImage(width(), height(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2 = img2.createGraphics();

		{
			RenderingHints rHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			rHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHints(rHints);
		}

		g2.drawImage(img, 0, 0, width(), height(), null);
		g2.dispose();

		for(int x = 0; x < width(); x++) {
			for(int y = 0; y < height(); y++) {
				_matrix[x][y] = img2.getRGB(x, y);
			}
		}
	}

	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(width(), height(), BufferedImage.TYPE_3BYTE_BGR);

		for(int x = 0; x < width(); x++) {
			for(int y = 0; y < height(); y++) {
				img.setRGB(x, y, _matrix[x][y]);
			}
		}
		return img;
	}

	public int get(int x, int y) {
		return _matrix[x][y];
	}
}
