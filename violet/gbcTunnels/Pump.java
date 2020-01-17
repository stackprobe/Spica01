package violet.gbcTunnels;

import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;
import charlotte.tools.ThreadEx;

public class Pump {
	public ThreadEx th;
	public IQueue<PumpPacket> clientToServerPackets = new QueueUnit<PumpPacket>();
	public KickableWaiter waiter = new KickableWaiter(true);
}
