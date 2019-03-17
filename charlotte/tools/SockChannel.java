package charlotte.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SockChannel {
	public Socket handler;
	public SockServer.BlockingHandlerManager blockingHandlerManager;

	private InputStream _reader;
	private OutputStream _writer;

	public static boolean stopFlag;

	public int idleTimeoutMillis = 180000; // 3 min // -1 == INFINITE

	public void postSetHandler() throws Exception {
		_reader = handler.getInputStream();
		_writer = handler.getOutputStream();
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
			throw new IllegalArgumentException(String.format("(0, size: %d) -> (%d, size: %d)", data.length, offset, size));
		}

		while(1 <= size) {
			int recvSize = tryRecv(data, offset, size);

			if(recvSize <= 0 || size < recvSize) {
				throw new RTError("recvSize: " + recvSize);
			}
			offset += recvSize;
			size -= recvSize;
		}
	}

	private int tryRecv(byte[] data, int offset, int size) throws Exception {
		if(stopFlag) {
			throw new RTError("RECV_STOP_REQUESTED");
		}

		Object blocking = blockingHandlerManager.add(handler, idleTimeoutMillis);
		try {
			return SockServer.critical.unsection_get(() -> _reader.read(data, offset, size));
		}
		finally {
			if(blockingHandlerManager.remove(blocking) == -1) {
				throw new IdleTimeoutException();
			}
		}
	}

	public static class IdleTimeoutException extends Exception {
		// none
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
			throw new IllegalArgumentException(String.format("(0, size: %d) -> (%d, size: %d)", data.length, offset, size));
		}

		if(stopFlag) {
			throw new RTError("SEND_STOP_REQUESTED");
		}

		if(1 <= size) {
			Object blocking = blockingHandlerManager.add(handler, idleTimeoutMillis);
			try {
				SockServer.critical.unsection(() -> _writer.write(data, offset, size));
			}
			finally {
				if(blockingHandlerManager.remove(blocking) == -1) {
					throw new IdleTimeoutException();
				}
			}
		}
	}
}
