package charlotte.tools;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class SockServer {
	public int portNo = 59999;
	public int backLog = 100;
	public int connectMax = 30;

	public static Consumer<Throwable> errorOccurred = e -> e.printStackTrace(System.out);
	public static Consumer<Throwable> errorOccurred_fatal = e -> e.printStackTrace();
	public static Runnable errorOccurred_recvFirstLineIdleTimeout = () -> System.out.println("RECV_FIRST_LINE_IDLE_TIMEOUT");

	public abstract void connected(SockChannel channel) throws Exception;

	public boolean interlude() throws Exception {
		return System.in.available() == 0;
	}

	private List<ThreadEx> _connectedThs = new ArrayList<ThreadEx>();

	public void perform() throws Exception {
		SockChannel.critical.section_a(() -> {
			SockChannel.BlockingHandlerMonitor blockingHandlerMonitor = new SockChannel.BlockingHandlerMonitor();

			try(ServerSocket listener = new ServerSocket()) {
				listener.setReuseAddress(true);
				listener.setSoTimeout(2000); // accept() no taimuauto
				listener.bind(new InetSocketAddress(portNo), backLog);

				while(interlude()) {
					Socket handler = connect(listener);

					if(handler != null) {
						_connectedThs.add(new ThreadEx(() -> SockChannel.critical.section_a(() -> {
							try {
								SockChannel channel = new SockChannel();
								channel.handler = handler;
								channel.blockingHandlerMonitor = blockingHandlerMonitor;
								channel.postSetHandler();
								connected(channel);
							}
							catch(HTTPServerChannel.RecvFirstLineIdleTimeoutException e) {
								errorOccurred_recvFirstLineIdleTimeout.run();
							}
							catch(Throwable e) {
								errorOccurred.accept(e);
							}

							try {
								handler.close();
							}
							catch(Throwable e) {
								errorOccurred.accept(e);
							}
						}
						)));
					}

					blockingHandlerMonitor.check();
					_connectedThs.removeIf(connectedTh -> RTError.get(() -> connectedTh.isEnded()));
				}
			}
			catch(Throwable e) {
				errorOccurred_fatal.accept(e);
			}

			blockingHandlerMonitor.burst();
			stop();
		});
	}

	private Socket connect(ServerSocket listener) throws Exception {
		if(_connectedThs.size() < connectMax) {
			try {
				return SockChannel.critical.unsection_get(() -> listener.accept());
			}
			catch(SocketTimeoutException e) {
				// noop
			}
		}
		else {
			SockChannel.critical.unsection_a(() -> _connectedThs.get(0).isEnded(100)); // FIXME zen-setsuzoku de machitai
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
			connectedTh.waitToEnd(SockChannel.critical);
		}
		_connectedThs.clear();
	}
}
