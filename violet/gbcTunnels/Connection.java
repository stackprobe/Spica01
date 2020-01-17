package violet.gbcTunnels;

import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;
import charlotte.tools.SockChannel;
import charlotte.tools.ThreadEx;

public class Connection {
	public byte[] credential;
	public Server server;
	public SockChannel channel;
	public ThreadEx clientToServerTh;
	public ThreadEx serverToClientTh;
	public ThreadEx pumpTh;
	public IQueue<PumpPacket> clientToServerPackets = new QueueUnit<PumpPacket>();
	public IQueue<PumpPacket> serverToClientPackets = new QueueUnit<PumpPacket>();
	public KickableWaiter clientToServerWaiter = new KickableWaiter();
	public KickableWaiter serverToClientWaiter = new KickableWaiter();
	public boolean clientToServerDead = false;
	public boolean serverToClientDead = false;
	public boolean pumpDead = false;

	// ---- NamedTrackPump ----

	public boolean trackNameSent = false;

	// ---- CipherPump ----

	// ---- BoomerangPump ----

	public boolean foregroundFlag = true;

	// ---- HTTPPump ----

	// ----
}
