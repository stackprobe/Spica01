package wb.t20181218;

import charlotte.tools.RTError;

public class PictureInfo {
	private String _file;

	public PictureInfo(String file) throws Exception {
		_file = file;
	}

	public String getFile() {
		return _file;
	}

	public double grayscale() {
		throw null; // TODO
	}

	public boolean isSameOrAlmostSamePicture(PictureInfo other) {
		return difference(other) < 0.1;
	}

	public double difference(PictureInfo other) {
		return RTError.get(() -> difference_e(other));
	}

	public double difference_e(PictureInfo other) throws Exception {
		throw null; // TODO
	}
}
