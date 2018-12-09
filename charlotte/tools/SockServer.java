package charlotte.tools;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public abstract class SockServer {
	protected int portNo = 59999;
	protected int backLog = 100;
	protected int connectMax = 30;

	protected abstract void connected(SockChannel channel);

	private ThreadEx _th;
	private List<ThreadEx> _connectedThs = new ArrayList<ThreadEx>();

	public void start() {
		_th = new ThreadEx(() -> {
			try {
				try(ServerSocket listener = new ServerSocket()) {
					listener.setReuseAddress(true);
					listener.setSoTimeout(2000); // accept()のタイムアウト
					listener.bind(new InetSocketAddress(portNo), backLog);

					while(_stopFlag == false) {
						try {
							Socket handler = connect(listener);

							if(handler != null) {
								SockChannel channel = new SockChannel(handler);

								handler = null;

								_connectedThs.add(new ThreadEx(() -> {
									try {
										channel.open();
										connected(channel);
									}
									catch(Throwable e) {
										e.printStackTrace(System.out);
									}

									try {
										channel.close();
									}
									catch(Throwable e) {
										e.printStackTrace(System.out);
									}
								}
								));
							}

							_connectedThs.removeIf(connectedTh -> RTError.get(() -> connectedTh.isEnded()));
						}
						catch(Throwable e) {
							e.printStackTrace();

							System.out.println("5秒間待機します。"); // ここへの到達は想定外。ノーウェイトでループしないように。
							Thread.sleep(5000);
							System.out.println("5秒間待機しました。");
						}
					}
				}

				for(ThreadEx connectedTh : _connectedThs) {
					connectedTh.waitToEnd();
				}
				_connectedThs.clear();
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
		});
	}

	private Socket connect(ServerSocket listener) throws Exception {
		if(_connectedThs.size() < connectMax) {
			try {
				return listener.accept();
			}
			catch(SocketTimeoutException e) {
				// noop
			}
		}
		else {
			_connectedThs.get(0).isEnded(100); // FIXME 全接続で待ちたい。
		}
		return null;
	}

	public boolean isRunning(int millis) throws Exception {
		if(_th != null && _th.isEnded(millis)) {
			_th = null;
		}
		return _th != null;
	}

	private boolean _stopFlag = false;

	public void stop() {
		_stopFlag = false;
	}

	public void stop_B_channelSafe() throws Exception {
		stop();

		while(isRunning(2000)) {
			// noop
		}
	}

	public void stop_B() throws Exception {
		SockChannel.stopFlag = true;
		stop_B_channelSafe();
		SockChannel.stopFlag = false;
	}
}
