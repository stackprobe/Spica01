package violet.gbcTunnels;

public interface IPump {
	default void pump(PumpPacket packet) throws Exception {
		pump(packet, null);
	}

	void pump(PumpPacket packet, IPump nextPump) throws Exception;
}
