package violet.gbcTunnels;

import charlotte.tools.QueueUnit;
import charlotte.tools.SockChannel;

public class ClientConnection {
	public QueueUnit<PumpPacket> toClientPackets = new QueueUnit<PumpPacket>();
	public KickableWaiter waiter = new KickableWaiter(new ClientConnectionWaiter());

	private SockChannel _channel;

	public ClientConnection(SockChannel channel) {
		_channel = channel;
	}
}
