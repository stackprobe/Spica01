package violet.gbcTunnels;

import charlotte.tools.QueueUnit;
import violet.gbcTunnels.filters.BoomerangFilter;
import violet.gbcTunnels.filters.CipherFilter;
import violet.gbcTunnels.filters.HTTPFilter;

public class Pump {
	public QueueUnit<PumpPacket> toServerPackets = new QueueUnit<PumpPacket>();
	public KickableWaiter waiter = new KickableWaiter(new PumpWaiter());

	public Pump() {
		throw null; // TODO
	}

	public void end() {
		throw null; // TODO
	}

	// -- TODO

	private static void encode(PumpPacket packet) {
		CipherFilter.encode(packet);
		BoomerangFilter.encode(packet);
		HTTPFilter.encode(packet);
	}

	private static void decode(PumpPacket packet) {
		HTTPFilter.decode(packet);
		BoomerangFilter.decode(packet);
		CipherFilter.decode(packet);
	}
}
