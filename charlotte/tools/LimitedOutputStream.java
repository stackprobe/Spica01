package charlotte.tools;

import java.io.IOException;
import java.io.OutputStream;

public class LimitedOutputStream extends OutputStream {
	private OutputStream _out;
	private long _remaining;

	public LimitedOutputStream(OutputStream out, long limit) {
		_out = out;
		_remaining = limit;
	}

	@Override
	public void write(int b) throws IOException {
		beforeWrite(1);
		_out.write(b);
	}

	@Override
	public void write(byte[] buff, int offset, int length) throws IOException {
		beforeWrite(length);
		_out.write(buff, offset, length);
	}

	private void beforeWrite(int length) {
		if(_remaining < (long)length) {
			throw new RTError("Over the limit !!!");
		}
		_remaining -= (long)length;
	}

	@Override
	public void close() throws IOException {
		if(_out != null) {
			_out.close();
			_out = null;
		}
	}
}
