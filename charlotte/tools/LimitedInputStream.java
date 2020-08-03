package charlotte.tools;

import java.io.IOException;
import java.io.InputStream;

public class LimitedInputStream extends InputStream {
	private InputStream _in;
	private long _remaining;

	public LimitedInputStream(InputStream in, long limit) {
		_in = in;
		_remaining = limit;
	}

	@Override
	public int read() throws IOException {
		byte[] buff = new byte[1];

		if(read(buff) == -1) {
			return -1;
		}
		return buff[0] & 0xff;
	}

	@Override
	public int read(byte[] buff, int offset, int length) throws IOException {
		if(_remaining <= 0L) {
			return -1;
		}
		if(_remaining < length) {
			length = (int)_remaining;
		}
		int readSize = _in.read(buff, offset, length);

		if(readSize < 0) {
			_remaining = 0L;
		}
		else {
			_remaining -= length;
		}
		return readSize;
	}

	@Override
	public void close() throws IOException {
		if(_in != null) {
			_in.close();
			_in = null;
		}
	}
}
