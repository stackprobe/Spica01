package charlotte.tools;

public abstract class HTTPServer extends SockServer {
	public HTTPServer() {
		portNo = 80;
	}

	@Override
	public void connected(SockChannel channel) throws Exception {
		HandleDam.section(hDam -> {
			HTTPServerChannel hsChannel = new HTTPServerChannel(channel);

			hsChannel.hDam = hDam;
			hsChannel.recvRequest();

			httpConnected(hsChannel);

			hsChannel.sendResponse();
		});
	}

	public abstract void httpConnected(HTTPServerChannel hsChannel) throws Exception;
}
