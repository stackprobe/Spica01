package violet.gbcTunnels.pumps;

import charlotte.tools.StringTools;
import violet.gbcTunnels.IPump;
import violet.gbcTunnels.PumpPacket;

public class NamedTrackPump implements IPump {
	@Override
	public void pump(PumpPacket packet, IPump nextPump) throws Exception {
		if(packet.connection.trackNameSent == false) {
			PumpPacket pp = new PumpPacket(packet);

			pp.data = packet.connection.server.connector.trackName.getBytes(StringTools.CHARSET_SJIS);

			nextPump.pump(pp);

			packet.resDataParts.addAll(pp.resDataParts);
			packet.connection.trackNameSent = true;
		}
		nextPump.pump(packet);
	}
}
