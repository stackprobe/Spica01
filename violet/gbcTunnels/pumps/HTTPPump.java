package violet.gbcTunnels.pumps;

import charlotte.tools.Base64Unit;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;
import violet.gbcTunnels.GBCTunnelProps;
import violet.gbcTunnels.IPump;
import violet.gbcTunnels.PumpPacket;

public class HTTPPump implements IPump {
	private Base64Unit _base64 = new Base64Unit();

	@Override
	public void pump(PumpPacket packet, IPump nextPump) throws Exception {
		packet.url = "http://" + GBCTunnelProps.server + ":" + GBCTunnelProps.portNo + "/blueSteel/" + _base64.encode(packet.data) + ".html";

		nextPump.pump(packet);

		{
			String sResBody = new String(packet.resBody, StringTools.CHARSET_ASCII);
			String sResData = resBodyStringToResDataEnclosed(sResBody).inner();
			byte[] resData = _base64.decode(sResData);

			packet.resDataParts.add(resData);
		}
	}

	private StringTools.Enclosed resBodyStringToResDataEnclosed(String sResBody) {
		StringTools.Enclosed encl = StringTools.getEnclosed(sResBody, "<caption>", "</caption>");

		if(encl != null) {
			return encl;
		}
		encl = StringTools.getEnclosed(sResBody, "<th>", "</th>");

		if(encl != null) {
			return encl;
		}
		encl = StringTools.getEnclosed(sResBody, "<td>", "</td>");

		if(encl != null) {
			return encl;
		}
		throw new RTError("Bad sResBody");
	}
}
