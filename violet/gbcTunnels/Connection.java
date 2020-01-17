package violet.gbcTunnels;

import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;
import charlotte.tools.SockChannel;
import charlotte.tools.ThreadEx;

public class Connection {
	public String ident;
	public Server server;
	public SockChannel channel;
	public ThreadEx clientToServerTh;
	public ThreadEx serverToClientTh;
	public IQueue<PumpPacket> serverToClientPackets = new QueueUnit<PumpPacket>();
	public KickableWaiter waiter = new KickableWaiter(false);
	public boolean dead = false;
}
