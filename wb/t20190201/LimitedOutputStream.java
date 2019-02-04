package wb.t20190201;

import java.io.IOException;
import java.io.OutputStream;

public class LimitedOutputStream extends OutputStream {
	private OutputStream _out;
	private long _limit;
	private long _count;

	public LimitedOutputStream(OutputStream out, long limit) {
		_out = out;
		_limit = limit;
	}

	@Override
	public void write(int b) throws IOException {
		check(1);
		_out.write(b);
	}

	@Override
	public void write(byte[] buff, int offset, int length) throws IOException {
		check(length);
		_out.write(buff, offset, length);
	}

	private void check(int length) throws IOException {
		if(_limit < _count + length) {
			throw new IOException("Over " + _limit + " bytes !!!");
		}
		_count += length;
	}
}
