package violet.gbcTunnels;

import charlotte.tools.BinTools;
import charlotte.tools.SockChannel;

public interface IPump {
	default void recvWhile(PumpPacket packet, int size) throws Exception {
		//pump(packet); // del

		if(packet.getResData().length < size) {
			int millis = 0;

			do {
				if(millis < 100) {
					millis++;
				}
				final int f_millis = millis;

				SockChannel.critical.unsection_a(() -> Thread.sleep(f_millis));

				{
					PumpPacket pp = packet.getTemp();

					pp.data = BinTools.EMPTY;
					pump(pp);
					packet.resDataParts.add(pp.getResData());
				}
			}
			while(packet.getResData().length < size);
		}
	}

	default void pump(PumpPacket packet) throws Exception {
		pump(packet, null);
	}

	void pump(PumpPacket packet, IPump nextPump) throws Exception;
}
