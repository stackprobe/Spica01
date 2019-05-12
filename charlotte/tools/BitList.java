package charlotte.tools;

import java.util.Arrays;

public class BitList {
	private int[] _buffer;

	public BitList() {
		this(0L);
	}

	public BitList(long capacity) {
		_buffer = new int[(int)((capacity + 31L) >> 5)];
	}

	private void ensureCapacity(int capacity) {
		if(_buffer.length < capacity) {
			_buffer = Arrays.copyOf(_buffer, capacity);
		}
	}

	public boolean getBit(long index) {
		int i = (int)(index >> 5);

		if(_buffer.length <= i) {
			return false;
		}
		int b = (int)(index & 31L);

		return (_buffer[i] >>> b) == 1;
	}

	public void setBit(long index, boolean value) {
		int i = (int)(index >> 5);
		int b = (int)(index & 31L);

		ensureCapacity(i + 1);

		if(value) {
			_buffer[i] |= 1 << b;
		}
		else {
			_buffer[i] &= ~(1 << b);
		}
	}

	public void setBits(long index, long size, boolean value) {
		long bgn = index;
		long end = index + size;

		ensureCapacity((int)(end >> 5) + 1);

		while(bgn < end && (bgn & 31L) != 0L) {
			setBit(bgn, value);
			bgn++;
		}
		while(bgn < end && (end & 31L) != 0L) {
			end--;
			setBit(end, value);
		}
		if(bgn < end) {
			int ib = (int)(bgn >> 5);
			int ie = (int)(end >> 5);

			int c = value ? 0xffffffff : 0;

			for(int i = ib; i < ie; i++) {
				_buffer[i] = c;
			}
		}
	}

	public void invBit(long index) {
		int i = (int)(index >> 5);
		int b = (int)(index & 31L);

		ensureCapacity(i + 1);

		_buffer[i] ^= 1 << b;
	}

	public void invBits(long index, long size) {
		long bgn = index;
		long end = index + size;

		ensureCapacity((int)(end >> 5) + 1);

		while(bgn < end && (bgn & 31L) != 0L) {
			invBit(bgn);
			bgn++;
		}
		while(bgn < end && (end & 31L) != 0L) {
			end--;
			invBit(end);
		}
		if(bgn < end) {
			int ib = (int)(bgn >> 5);
			int ie = (int)(end >> 5);

			for(int i = ib; i < ie; i++) {
				_buffer[i] ^= 0xffffffff;
			}
		}
	}
}
