package wb.t20200125_GBCTunnels.utils;

import charlotte.tools.BinTools;

public class PumpBinBuffer {
	private byte[] _buff = BinTools.EMPTY;

	public void clear() {
		_buff = BinTools.EMPTY;
	}

	public int size() {
		return _buff.length;
	}

	public void write(byte[] data) {
		_buff = BinTools.join(_buff, data);
	}

	public byte[] read(int size) {
		byte[] ret = BinTools.getSubBytes(_buff, 0, size);
		_buff = BinTools.getSubBytes(_buff, size);
		return ret;
	}

	public byte[] getBuffer() {
		return _buff;
	}
}
