package charlotte.tools;

import java.util.List;

public class RandomUnit implements AutoCloseable {
	public interface IRandomNumberGenerator extends AutoCloseable {
		byte[] getBlock();
	}

	private IRandomNumberGenerator _rng;

	public RandomUnit(IRandomNumberGenerator rng) {
		_rng = rng;
	}

	@Override
	public void close() throws Exception {
		if(_rng != null ) {
			_rng.close();
			_rng = null;
		}
	}

	private byte[] _cache = BinTools.EMPTY;
	private int _rIndex = 0;

	public byte getByte() {
		if(_cache.length <= _rIndex) {
			_cache = _rng.getBlock();
			_rIndex = 0;
		}
		return _cache[_rIndex++];
	}

	public byte[] getBytes(int length) {
		byte[] dest = new byte[length];

		for(int index = 0; index < length; index++) {
			dest[index] = getByte();
		}
		return dest;
	}

	public short getShort() {
		byte[] r = getBytes(2);

		return
				(short)
				(
					((r[0] & 0xff) << 0) |
					((r[1] & 0xff) << 8)
				);
	}

	public int getInt24() {
		byte[] r = getBytes(3);

		return
				((r[0] & 0xff) << 0) |
				((r[1] & 0xff) << 8) |
				((r[2] & 0xff) << 16);
	}

	public int getInt() {
		byte[] r = getBytes(4);

		return
				((r[0] & 0xff) << 0) |
				((r[1] & 0xff) << 8) |
				((r[2] & 0xff) << 16) |
				((r[3] & 0xff) << 24);
	}

	public long getLong() {
		byte[] r = getBytes(8);

		return
				((r[0] & 0xffL) << 0) |
				((r[1] & 0xffL) << 8) |
				((r[2] & 0xffL) << 16) |
				((r[3] & 0xffL) << 24) |
				((r[4] & 0xffL) << 32) |
				((r[5] & 0xffL) << 40) |
				((r[6] & 0xffL) << 48) |
				((r[7] & 0xffL) << 56);
	}

	public long getLong(long modulo) {
		if(modulo <= 0L) {
			throw new RTError("modulo <= 0");
		}
		if(modulo == 1L) {
			return 0L;
		}
		long r_mod = (Long.MAX_VALUE % modulo + 1L) % modulo;
		long r;

		do {
			r = getLong() & Long.MAX_VALUE;
		}
		while(r < r_mod);

		r %= modulo;

		return r;
	}

	public int getInt(int modulo) {
		return (int)getLong((long)modulo);
	}

	public long getRangeLong(long minval, long maxval) {
		return getLong(maxval - minval + 1L) + minval;
	}

	public int getRangeInt(int minval, int maxval) {
		return (int)getRangeLong((long)minval, (long)maxval);
	}

	public double getReal() { // [0,1]
		return (getInt() & Integer.MAX_VALUE) / (double)Integer.MAX_VALUE;
	}

	public double getReal2() { // [0,1)
		return (getInt() & Integer.MAX_VALUE) / (double)(Integer.MAX_VALUE + 1L);
	}

	public double getReal3() { // (0,1)
		return (getInt() & Integer.MAX_VALUE) / (double)(Integer.MAX_VALUE + 1L) + 0.5;
	}

	public <T> void shuffle(T[] arr) {
		shuffle(IArrays.asList(arr));
	}

	public <T> void shuffle(List<T> list) {
		for(int index = list.size(); 1 < index; index--) {
			ListTools.swap(list, getInt(index), index - 1);
		}
	}

	public <T> T chooseOne(T[] arr) {
		return chooseOne(IArrays.asList(arr));
	}

	public <T> T chooseOne(List<T> list) {
		return list.get(getInt(list.size()));
	}
}
