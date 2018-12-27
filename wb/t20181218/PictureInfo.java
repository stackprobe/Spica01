package wb.t20181218;

import charlotte.tools.RTError;

public class PictureInfo {
	private String _file;

	public PictureInfo(String file) {
		_file = file;
	}

	public String getFile() {
		return _file;
	}

	public double grayscale() {
		return RTError.get(() -> grayscale_e());
	}

	public boolean isSameOrAlmostSamePicture(PictureInfo other) {
		return difference(other) < 0.01; // XXX 要調整
	}

	public double difference(PictureInfo other) {
		return RTError.get(() -> difference_e(other));
	}

	private static final int THUMB_W = 10;
	private static final int THUMB_H = 10;

	private double difference_e(PictureInfo other) throws Exception {
		int numer = 0;
		final int denom = THUMB_W * THUMB_H * 255 * 3;

		for(int x = 0; x < THUMB_W; x++) {
			for(int y = 0; y < THUMB_H; y++) {
				int rgb1 = thumbnail().get(x, y);
				int rgb2 = other.thumbnail().get(x, y);

				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >>  8) & 0xff;
				int b1 = (rgb1 >>  0) & 0xff;

				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >>  8) & 0xff;
				int b2 = (rgb2 >>  0) & 0xff;

				int r = Math.abs(r1 - r2);
				int g = Math.abs(g1 - g2);
				int b = Math.abs(b1 - b2);

				numer += r;
				numer += g;
				numer += b;
			}
		}
		return numer * 1.0 / denom;
	}

	private Thumbnail _thumb = null;

	public Thumbnail thumbnail() throws Exception {
		if(_thumb == null) {
			_thumb = new Thumbnail(THUMB_W, THUMB_H);
			_thumb.setFile(_file);
		}
		return _thumb;
	}

	private Double _grayscale = null;

	private double grayscale_e() throws Exception {
		if(_grayscale == null) {
			Thumbnail dot = new Thumbnail(1, 1);

			dot.set(thumbnail());

			int rgb = dot.get(0, 0);

			int r = (rgb >> 16) & 0xff;
			int g = (rgb >>  8) & 0xff;
			int b = (rgb >>  0) & 0xff;

			_grayscale = (r + g + b) * 1.0 / (255 + 255 + 255);
		}
		return _grayscale.doubleValue();
	}
}
