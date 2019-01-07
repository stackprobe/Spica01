package charlotte.tools;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SockClient extends SockChannel {
	public SockClient(String domain, int portNo) throws Exception {
		this(domain, portNo, 20000); // 20 sec
	}

	public SockClient(String domain, int portNo, int connectTimeoutMillis) throws Exception {
		SockServer.critical.enter();

		Socket handler = new Socket();

		handler.connect(new InetSocketAddress(InetAddress.getByName(domain), portNo), connectTimeoutMillis);

		setHandler(handler);
		open();
	}

	@Override
	public void close() throws Exception {
		super.close();

		SockServer.critical.leave();
	}
}
