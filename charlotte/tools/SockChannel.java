package charlotte.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SockChannel {
	private Socket _handler;
	private InputStream _reader;
	private OutputStream _writer;

	public static boolean stopFlag;

	public SockChannel(Socket handler) {
		_handler = handler;
	}

	public void open() throws Exception {
		_reader = _handler.getInputStream();
		_writer = _handler.getOutputStream();
	}

	public int read(byte[] buff, int offset, int size) throws Exception {
		return _reader.read(buff, offset, size);
	}

	public void write(byte[] buff, int offset, int size) throws Exception {
		_writer.write(buff, offset, size);
	}

	public void close() throws Exception {
		_handler.close();
		_handler = null;
	}
}
