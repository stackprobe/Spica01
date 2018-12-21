package charlotte.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SockChannel {
	private Socket _handler;
	private InputStream _reader;
	private OutputStream _writer;

	public static boolean stopFlag;

	public static final int SO_TIMEOUT = 2000;
	public int idleTimeoutMillis = 180000; // 3 min

	public void setHandler(Socket handler) {
		_handler = handler;
	}

	public void open() throws Exception {
		_handler.setSoTimeout(SO_TIMEOUT);
		_reader = _handler.getInputStream();
		_writer = _handler.getOutputStream();
	}

	public byte[] recv(int size) throws Exception {
		byte[] buff = new byte[size];
		recv(buff);
		return buff;
	}

	public void recv(byte[] data) throws Exception {
		recv(data, 0);
	}

	public void recv(byte[] data, int offset) throws Exception {
		recv(data, offset, data.length - offset);
	}

	public void recv(byte[] data, int offset, int size) throws Exception {
		if(
				offset < 0 ||
				data.length < offset ||
				size < 0 ||
				data.length - offset < size
				) {
			throw new SockChannelError(String.format("BAD_PARAMS: (0, s:%d) -> (%d, s:%d)", data.length, offset, size));
		}

		while(1 <= size) {
			int recvSize = tryRecv(data, offset, size);

			if(recvSize <= 0 || size < recvSize) {
				throw new SockChannelError("recvSize: " + recvSize);
			}
			offset += recvSize;
			size -= recvSize;
		}
	}

	private int tryRecv(byte[] data, int offset, int size) throws Exception {
		int idleMillis = 0;

		for(; ; ) {
			if(stopFlag) {
				throw new SockChannelError("RECV_STOP_REQUESTED");
			}

			long readStartedTime = System.currentTimeMillis(); // test test test

			try {
				return _reader.read(data, offset, size);
			}
			catch(SocketTimeoutException e) {
				// noop
			}

			System.out.println("read() exec time: " + (System.currentTimeMillis() - readStartedTime)); // test test test

			idleMillis += SO_TIMEOUT;

			if(idleTimeoutMillis <= idleMillis) {
				throw new SockChannelError("RECV_TIMEOUT");
			}
		}
	}

	public void send(byte[] data) throws Exception {
		send(data, 0);
	}

	public void send(byte[] data, int offset) throws Exception {
		send(data, offset, data.length - offset);
	}

	public void send(byte[] data, int offset, int size) throws Exception {
		if(
				offset < 0 ||
				data.length < offset ||
				size < 0 ||
				data.length - offset < size
				) {
			throw new SockChannelError(String.format("BAD_PARAMS: (0, s:%d) -> (%d, s:%d)", data.length, offset, size));
		}

		if(stopFlag) {
			throw new SockChannelError("SEND_STOP_REQUESTED");
		}

		if(1 <= size) {
			_writer.write(data, offset, size);
		}
	}

	/**
	 *	このメソッドは例外を投げないこと。
	 * @throws Exception 例外を投げない。
	 */
	public void close() throws Exception {
		if(_handler != null) {
			try {
				_handler.close();
			}
			catch(Throwable e) {
				e.printStackTrace();
			}

			_handler = null;
		}
	}
}
