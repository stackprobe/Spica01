package violet.gbcTunnels.pumps;

import charlotte.tools.StringTools;
import violet.gbcTunnels.Ground;
import violet.gbcTunnels.IPump;
import violet.gbcTunnels.PumpPacket;

public class NamedTrackPump implements IPump {
	@Override
	public void pump(PumpPacket packet, IPump nextPump) throws Exception {
		if(
				Ground.currThConnections.get().trackNameSent == false &&
				Ground.currThConnections.get().disconnect == false
				) {
			{
				PumpPacket pp = new PumpPacket(
						Ground.currThConnections.get().server.connector.trackName.getBytes(StringTools.CHARSET_SJIS)
						);
				nextPump.pump(pp);
				packet.resDataParts.addAll(pp.resDataParts);
			}

			Ground.currThConnections.get().trackNameSent = true;
		}
		nextPump.pump(packet);
	}
}
