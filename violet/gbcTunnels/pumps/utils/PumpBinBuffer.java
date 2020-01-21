package violet.gbcTunnels.pumps.utils;

import charlotte.tools.BinTools;

public class PumpBinBuffer {
	private byte[] _buff = BinTools.EMPTY;

	public void write(byte[] data) {
		_buff = BinTools.join(new byte[][] { _buff, data });
	}

	public byte[] read(int size) {
		byte[] ret = BinTools.getSubBytes(_buff, 0, size);
		_buff = BinTools.getSubBytes(_buff, size);
		return ret;
	}
}
