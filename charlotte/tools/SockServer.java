package charlotte.tools;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SockServer {
	public int portNo = 59999;
	public int backLog = 100;
	public int connectMax = 30;

	public abstract void connected(SockChannel channel) throws Exception;

	public boolean interlude() throws Exception {
		return System.in.available() == 0;
	}

	public static Critical critical = new Critical();

	private List<ThreadEx> _connectedThs = new ArrayList<ThreadEx>();

	public void perform() throws Exception {
		critical.section(() -> {
			BlockingHandlerManager blockingHandlerManager = new BlockingHandlerManager();

			try(ServerSocket listener = new ServerSocket()) {
				listener.setReuseAddress(true);
				listener.setSoTimeout(2000); // accept()のタイムアウト
				listener.bind(new InetSocketAddress(portNo), backLog);

				while(interlude()) {
					try {
						Socket handler = connect(listener);

						if(handler != null) {
							_connectedThs.add(new ThreadEx(() -> critical.section(() -> {
								try {
									SockChannel channel = new SockChannel();
									channel.handler = handler;
									channel.blockingHandlerManager = blockingHandlerManager;
									channel.postSetHandler();
									connected(channel);
								}
								catch(HTTPServerChannel.RecvFirstLineIdleTimeoutException e) {
									System.out.println("FIRST_LINE_IDLE_TIMEOUT");
								}
								catch(Throwable e) {
									e.printStackTrace(System.out);
								}

								try {
									handler.close();
								}
								catch(Throwable e) {
									e.printStackTrace(System.out);
								}
							}
							)));
						}

						blockingHandlerManager.check();
						_connectedThs.removeIf(connectedTh -> RTError.get(() -> connectedTh.isEnded()));
					}
					catch(Throwable e) {
						e.printStackTrace();

						System.out.println("5秒間待機します。"); // ここへの到達は想定外。ノーウェイトでループしないように。
						critical.unsection(() -> Thread.sleep(5000));
						System.out.println("5秒間待機しました。");
					}
				}
			}

			blockingHandlerManager.burst();
			stop();
		});
	}

	private Socket connect(ServerSocket listener) throws Exception {
		if(_connectedThs.size() < connectMax) {
			try {
				return critical.unsection_get(() -> listener.accept());
			}
			catch(SocketTimeoutException e) {
				// noop
			}
		}
		else {
			critical.unsection(() -> _connectedThs.get(0).isEnded(100)); // FIXME 全接続で待ちたい。
		}
		return null;
	}

	private void stop() throws Exception {
		SockChannel.stopFlag = true;
		stop_channelSafe();
		SockChannel.stopFlag = false;
	}

	private void stop_channelSafe() throws Exception {
		for(ThreadEx connectedTh : _connectedThs) {
			connectedTh.waitToEnd(critical);
		}
		_connectedThs.clear();
	}

	public static class BlockingHandlerManager {
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
			}
		}

		private Info[] _infos = new Info[1];
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
				_infos[_infoCount + 1] = null;
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
	}
}
