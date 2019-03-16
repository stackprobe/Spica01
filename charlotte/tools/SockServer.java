package charlotte.tools;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
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
									channel.parent = this;
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

	public class BlockingHandlerManager {
		private class Info {
			public Socket handler;
			public long timeoutMillis; // -1 == INFINITE

			public void close() {
				try {
					handler.close();
				}
				catch(Throwable e) {
					e.printStackTrace(System.out);
				}
			}
		}

		private List<Info> _infos = new ArrayList<Info>();

		public void add(Socket handler, long timeoutMillis) {
			Info info = new Info();

			info.handler = handler;
			info.timeoutMillis = timeoutMillis == -1L ? -1L : System.currentTimeMillis() + timeoutMillis;

			_infos.add(info);
		}

		public boolean remove(Socket handler) {
			return _infos.removeIf(info -> info.handler == handler);
		}

		public void check() {
			long now = System.currentTimeMillis();

			for(int index = _infos.size() - 1; 0 <= index; index--) {
				Info info = _infos.get(index);

				if(info.timeoutMillis != -1L && info.timeoutMillis <= now) {
					info.close();
					_infos.remove(index);
				}
			}
		}

		public void burst() {
			for(Info info : _infos) {
				info.close();
			}
			_infos.clear();
		}
	}

	public BlockingHandlerManager blockingHandlerManager = new BlockingHandlerManager();
}
