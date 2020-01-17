package violet.gbcTunnels;

import charlotte.tools.QueueUnit;
import charlotte.tools.SockChannel;

public class ClientConnection {
	public QueueUnit<PumpPacket> toClientPackets = new QueueUnit<PumpPacket>();
	public KickableWaiter waiter = new KickableWaiter(new ClientConnectionWaiter());

	private SockChannel _channel;
	private CCServer _server;

	public ClientConnection(SockChannel channel, CCServer server) {
		_channel = channel;
		_server = server;
	}

	public void connectedThSCSync() {
		byte[] buff = new byte[2500];

		try {
			while(_server.isDeath() == false) {
				_channel.recv(buff, (data, offset, size) -> {

				});
			}
		}
		catch(Throwable e) {
			// TODO
		}
	}
}
