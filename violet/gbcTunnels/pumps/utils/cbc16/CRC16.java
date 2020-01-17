package violet.gbcTunnels.pumps.utils.cbc16;

public class CRC16 {
	private static final int POLY = 0xa001;

	private int[] _table = new int[256];

	public CRC16() {
		for(int i = 0; i < 256; i++) {
			int crc = i;

			for(int c = 0; c < 8; c++) {
				if((crc & 1) != 0) {
					crc ^= (POLY << 1) | 1;
				}
				crc >>= 1;
			}
			_table[i] = crc;
		}
	}

	public int start() {
		return 0xffff;
	}

	public int update(int counter, byte[] data, int offset, int size) {
		for(int index = 0; index < size; index++) {
			counter = _table[(counter ^ data[index]) & 0xff] ^ (counter >> 8);
		}
		return counter;
	}

	public int finish(int counter) {
		return counter;
		//return counter ^ 0xffff;
	}

	public int compute(byte[] data, int offset, int size) {
		return finish(update(start(), data, offset, size));
	}
}
