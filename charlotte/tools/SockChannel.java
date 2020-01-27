package charlotte.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class SockChannel {
	public Socket handler;

	private InputStream _reader;
	private OutputStream _writer;

	public static boolean stopFlag = false;

	public long sessionTimeoutTime = -1L; // -1L == INFINITE
	public int idleTimeoutMillis = 180000; // 3 min // -1 == INFINITE

	public void postSetHandler() throws Exception {
		_reader = handler.getInputStream();
		_writer = handler.getOutputStream();
	}

	private void preRecvSend() throws Exception {
		if(stopFlag) {
			throw new RTError("CHANNEL_STOP_REQUESTED");
		}
		if(sessionTimeoutTime != -1L && sessionTimeoutTime < System.currentTimeMillis()) {
			throw new SessionTimeoutException();
		}
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
				throw new RTError("recvSize: " + recvSize + " (size: " + size + ")");
			}
			offset += recvSize;
			size -= recvSize;
		}
	}

	public void recv(byte[] buff, FileTools.IWriter writer) throws Exception {
		if(buff == null || writer == null) {
			throw new IllegalArgumentException();
		}
		int recvSize = tryRecv(buff, 0, buff.length);

		if(recvSize <= 0 || buff.length < recvSize) {
			throw new RTError("recvSize: " + recvSize + " (buff.length: " + buff.length + ")");
		}
		writer.write(buff, 0, recvSize);
	}

	private int tryRecv(byte[] data, int offset, int size) throws Exception {
		preRecvSend();

		Object blocking = blockingHandlerMonitor.add(handler, idleTimeoutMillis);
		try {
			return critical.unsection_get(() -> _reader.read(data, offset, size));
		}
		finally {
			if(blockingHandlerMonitor.remove(blocking) == -1) {
				throw new IdleTimeoutException();
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
			throw new IllegalArgumentException(String.format("(0, size: %d) -> (%d, size: %d)", data.length, offset, size));
		}

		preRecvSend();

		if(1 <= size) {
			Object blocking = blockingHandlerMonitor.add(handler, idleTimeoutMillis);
			try {
				critical.unsection_a(() -> _writer.write(data, offset, size));
			}
			finally {
				if(blockingHandlerMonitor.remove(blocking) == -1) {
					throw new IdleTimeoutException();
				}
			}
		}
	}

	public static class SessionTimeoutException extends Exception {
		// none
	}

	public static class IdleTimeoutException extends Exception {
		// none
	}

	public static Critical critical = new Critical();

	public static class BlockingHandlerMonitor {
		private class Info {
			public int selfIndex;
			public Socket handler;
			public long timeoutTimeMillis; // -1L == INFINITE

			public void close() {
				try {
					handler.close();
				}
				catch(Throwable e) {
					e.printStackTrace(System.out);
				}
				handler = null;
			}
		}

		private Info[] _infos = new Info[0];
		private int _infoCount = 0;

		public Object add(Socket handler, int timeoutMillis) {
			Info info = new Info();

			info.selfIndex = _infoCount;
			info.handler = handler;
			info.timeoutTimeMillis = timeoutMillis == -1 ? -1L : System.currentTimeMillis() + (long)timeoutMillis;

			if(_infos.length <= _infoCount) {
				_infos = Arrays.copyOf(_infos, _infos.length + 1);
			}
			_infos[_infoCount++] = info;

			return info;
		}

		public int remove(Object target) {
			return removeInfo((Info)target);
		}

		private int removeInfo(Info info) {
			int index = info.selfIndex;

			if(index != -1) {
				_infos[index] = _infos[--_infoCount];
				_infos[index].selfIndex = index;
				_infos[_infoCount] = null;
				info.selfIndex = -1;
			}
			return index;
		}

		public void check() {
			long now = System.currentTimeMillis();

			for(int index = _infoCount - 1; 0 <= index; index--) {
				Info info = _infos[index];

				if(info.timeoutTimeMillis != -1L && info.timeoutTimeMillis <= now) {
					info.close();
					removeInfo(info);
				}
			}
		}

		public void burst() {
			while(1 <= _infoCount) {
				_infos[--_infoCount].close();
				_infos[_infoCount].selfIndex = -1;
				_infos[_infoCount] = null;
			}
			_infoCount = 0;
		}

		private ThreadEx _th;
		private boolean _stopFlag = false;

		public void startTh() {
			_th = new ThreadEx(() -> critical.section_a(() -> {
				for(; ; ) {
					critical.unsection_a(() -> {
						try {
							Thread.sleep(2000); // FIXME 待ち時間に高い精度は要らない。
						}
						catch(InterruptedException e) {
							// noop
						}
					});

					if(_stopFlag) {
						break;
					}
					check();
				}
			}
			));
		}

		public void endTh() throws Exception {
			_stopFlag = true;
			_th.getThread_UNSAFE().interrupt();
			_th.waitToEnd(critical);
			_th = null;
		}
	}

	public BlockingHandlerMonitor blockingHandlerMonitor;
}
