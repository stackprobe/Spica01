package charlotte.tools;

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
		return
				(short)
				(
					((getByte() & 0xff) << 0) |
					((getByte() & 0xff) << 8)
				);
	}

	public int getInt() {
		return
				((getByte() & 0xff) << 0) |
				((getByte() & 0xff) << 8) |
				((getByte() & 0xff) << 16) |
				((getByte() & 0xff) << 24);
	}

	public long getLong() {
		return
				((getByte() & 0xffL) << 0) |
				((getByte() & 0xffL) << 8) |
				((getByte() & 0xffL) << 16) |
				((getByte() & 0xffL) << 24) |
				((getByte() & 0xffL) << 32) |
				((getByte() & 0xffL) << 40) |
				((getByte() & 0xffL) << 48) |
				((getByte() & 0xffL) << 56);
	}

	public long getLong(long modulo) {
		if(modulo <= 0L) {
			throw new RTError();
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

	public long getLong(long minval, long maxval) {
		return getLong(maxval - minval + 1L) + minval;
	}

	public int getInt(int minval, int maxval) {
		return (int)getLong((long)minval, (long)maxval);
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
}
