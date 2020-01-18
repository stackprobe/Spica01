package violet.gbcTunnels;

import charlotte.tools.BinTools;
import charlotte.tools.SockChannel;

public interface IPump {
	default void recvWhile(PumpPacket packet, IPump nextPump, int size) throws Exception {
		int millis = 0;

		while(packet.getResData().length < size) {
			if(millis < 100) {
				millis++;
			}
			final int f_millis = millis;

			SockChannel.critical.unsection_a(() -> Thread.sleep(f_millis));

			{
				PumpPacket pp = new PumpPacket(BinTools.EMPTY);
				nextPump.pump(pp);
				packet.resDataParts.addAll(pp.resDataParts);
			}
		}
	}

	default void pump(PumpPacket packet) throws Exception {
		pump(packet, null);
	}

	void pump(PumpPacket packet, IPump nextPump) throws Exception;
}
