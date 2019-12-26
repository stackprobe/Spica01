package violet.gbcTunnels;

import charlotte.tools.QueueUnit;
import violet.gbcTunnels.serializers.BoomerangSerializer;
import violet.gbcTunnels.serializers.CipherSerializer;
import violet.gbcTunnels.serializers.HTTPSerializer;

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

	private BoomerangSerializer _boomerangSerializer = new BoomerangSerializer();
	private CipherSerializer _cipherSerializer = new CipherSerializer();
	private HTTPSerializer _httpSerializer = new HTTPSerializer();

	private void encode(PumpPacket packet) {
		_cipherSerializer.serialize(packet);
		_boomerangSerializer.serialize(packet);
		_httpSerializer.serialize(packet);
	}

	private void decode(PumpPacket packet) {
		_httpSerializer.deserialize(packet);
		_boomerangSerializer.deserialize(packet);
		_cipherSerializer.deserialize(packet);
	}
}
