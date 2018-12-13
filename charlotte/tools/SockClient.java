package charlotte.tools;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SockClient extends SockChannel {
	public static int connectTimeoutMillis = 20000; // 20 sec

	public SockClient(String domain, int portNo) throws Exception {
		Socket handler = new Socket();

		handler.connect(new InetSocketAddress(InetAddress.getByName(domain), portNo), connectTimeoutMillis);

		setHandler(handler);
		open();
	}
}
