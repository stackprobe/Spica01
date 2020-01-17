package violet.gbcTunnels;

import charlotte.tools.SockChannel;
import charlotte.tools.ThreadEx;

public class Connection {
	public Server server;
	public SockChannel channel;
	public ThreadEx clientToServerTh;
	public ThreadEx serverToClientTh;
}
