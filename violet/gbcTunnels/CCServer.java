package violet.gbcTunnels;

import charlotte.tools.SockChannel;
import charlotte.tools.SockServer;
import charlotte.tools.ThreadEx;

public class CCServer {
	private GBCTunnelProps.Connector _connector;

	public CCServer(GBCTunnelProps.Connector connector) {
		_connector = connector;
	}

	private SockServer _ss;
	private ThreadEx _th;
	private boolean _death = false;

	public void start() {
		_ss = new SockServer() {
			@Override
			public void connected(SockChannel channel) throws Exception {
				CCServer.this.connectedThSCSync(channel);
			}
		};

		_ss.portNo = _connector.portNo;

		_th = new ThreadEx(() -> _ss.perform());
	}

	public void end() throws Exception {
		_death = true;

		_th.waitToEnd();
		_th = null;
	}

	private void connectedThSCSync(SockChannel channel) {
		ClientConnection cc = new ClientConnection(channel, this);

		cc.connectedThSCSync();
	}

	public boolean isDeath() {
		return _death;
	}
}
