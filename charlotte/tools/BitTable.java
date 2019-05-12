package charlotte.tools;

public class BitTable {
	private BitList _buffer;
	private int _w;
	private int _h;

	public BitTable(int w, int h) {
		if(w < 1 || h < 1) {
			throw new IllegalArgumentException();
		}
		_buffer = new BitList((long)w * h);
		_w = w;
		_h = h;
	}

	public boolean getBit(int x, int y) {
		if(
				x < 0 || _w <= x ||
				y < 0 || _h <= y
				) {
			throw new IllegalArgumentException();
		}
		return _buffer.getBit(x + (long)y * _w);
	}

	public void setBit(int x, int y, boolean value) {
		if(
				x < 0 || _w <= x ||
				y < 0 || _h <= y
				) {
			throw new IllegalArgumentException();
		}
		_buffer.setBit(x + (long)y * _w, value);
	}

	public void setBits(int l, int t, int w, int h, boolean value) { // w, h: 0 ok
		if(
				l < 0 || _w <= l ||
				t < 0 || _h <= t ||
				w < 0 || _w - l < w ||
				h < 0 || _h - t < h
				) {
			throw new IllegalArgumentException();
		}

		for(int y = 0; y < h; y++) {
			_buffer.setBits(l + (long)(t + y) * _w, (long)w, value);
		}
	}

	public void invBit(int x, int y) {
		if(x < 0 || _w <= x || y < 0 || _h <= y) {
			throw new IllegalArgumentException();
		}
		_buffer.invBit(x + (long)y * _w);
	}

	public void invBits(int l, int t, int w, int h) { // w, h: 0 ok
		if(
				l < 0 || _w <= l ||
				t < 0 || _h <= t ||
				w < 0 || _w - l < w ||
				h < 0 || _h - t < h
				) {
			throw new IllegalArgumentException();
		}

		for(int y = 0; y < h; y++) {
			_buffer.invBits(l + (long)(t + y) * _w, (long)w);
		}
	}
}
