package violet.gbcTunnels;

import charlotte.tools.QueueUnit;
import charlotte.tools.ThreadEx;
import violet.gbcTunnels.serializers.BoomerangSerializer;
import violet.gbcTunnels.serializers.CipherSerializer;
import violet.gbcTunnels.serializers.HTTPSerializer;

public class Pump {
	public QueueUnit<PumpPacket> toServerPackets = new QueueUnit<PumpPacket>();
	public KickableWaiter waiter = new KickableWaiter(new PumpWaiter());

	private ThreadEx _th;
	private boolean _death = false;;

	public void start() {
		_th = new ThreadEx(() -> perform());
	}

	public void end() throws Exception {
		_death = true;

		_th.waitToEnd();
		_th = null;
	}

	private void perform() throws Exception {
		while(_death == false) {
			if(toServerPackets.hasElements()) {
				PumpPacket packet = toServerPackets.dequeue();
				try {
					encode(packet);

					PumpPacket resPacket = packet.invoke();

					decode(resPacket);

					resPacket.ownerConnection.toClientPackets.enqueue(resPacket);
					resPacket.ownerConnection.waiter.kick();

					waiter.reset();
				}
				catch(Throwable e) {
					// TODO retry ? -- packet
				}
			}
			waiter.waitForMoment();
		}
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
