package charlotte.tools;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SockClient extends SockChannel implements AutoCloseable {
	public SockClient() {
		critical.enter();
		blockingHandlerMonitor = new BlockingHandlerMonitor();
		blockingHandlerMonitor.startTh();
	}

	public void connect(String domain, int portNo) throws Exception {
		connect(domain, portNo, 20000); // 20 sec
	}

	public void connect(String domain, int portNo, int connectTimeoutMillis) throws Exception {
		handler = new Socket();
		handler.connect(new InetSocketAddress(InetAddress.getByName(domain), portNo), connectTimeoutMillis);
		postSetHandler();
	}

	/**
	 * FIXME このメソッドは例外を投げないこと。
	 *
	 */
	@Override
	public void close() throws Exception {
		if(handler != null) {
			blockingHandlerMonitor.endTh();
			blockingHandlerMonitor = null;

			try {
				handler.close();
			}
			catch(Throwable e) {
				e.printStackTrace();
			}

			handler = null;

			try {
				critical.leave();
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
