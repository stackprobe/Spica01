package wb.t20181218;

public class PictureInfo {
	private String _file;

	public PictureInfo(String file) {
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
		throw null; // TODO
	}
}
