package violet.gbcTunnels;

import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;
import charlotte.tools.SockChannel;
import charlotte.tools.ThreadEx;
import violet.gbcTunnels.utils.KickableWaiter;
import violet.gbcTunnels.utils.PumpBinBuffer;

public class Connection {
	public Server server;
	public SockChannel channel;
	public ThreadEx clientToServerTh;
	public ThreadEx serverToClientTh;
	public ThreadEx pumpTh;
	public IQueue<byte[]> clientToServerBuff = new QueueUnit<byte[]>();
	public IQueue<byte[]> serverToClientBuff = new QueueUnit<byte[]>();
	public KickableWaiter clientToServerWaiter = new KickableWaiter();
	public KickableWaiter serverToClientWaiter = new KickableWaiter();
	public boolean clientToServerDead = false;
	public boolean serverToClientDead = false;
	public boolean pumpDead = false;

	// ---- NamedTrackPump ----

	public boolean trackNameSent = false;

	// ---- CipherPump ----

	public byte[] decCounter = null;
	public byte[] encCounter = null;
	public PumpBinBuffer cipherPumpRecvBuffer = new PumpBinBuffer();

	// ---- BoomerangPump ----

	public byte[] credential = null;
	public boolean foregroundFlag = true;

	// ---- HTTPPump ----

	// none

	// ----

	public boolean disconnect = false;
}
