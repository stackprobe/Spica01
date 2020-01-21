package violet.gbcTunnels.pumps;

import charlotte.tools.Base64Unit;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;
import violet.gbcTunnels.GBCTunnel;
import violet.gbcTunnels.GBCTunnelProps;

public class HTTPPump {
	private static Base64Unit.NoPadding _base64 = Base64Unit.createByC6364P("-_=").noPadding();

	public static byte[] pump(byte[] data) throws Exception {
		String url = "http://" + GBCTunnelProps.server + ":" + GBCTunnelProps.portNo + "/blueSteel/" + _base64.encode(data) + ".html";

		byte[] resBody = GBCTunnel.pump2(url);

		String sResBody = new String(resBody, StringTools.CHARSET_ASCII);
		String sResData = resBodyStringToResDataEnclosed(sResBody).inner();
		byte[] resData = _base64.decode(sResData);

		return resData;
	}

	private static StringTools.Enclosed resBodyStringToResDataEnclosed(String sResBody) {
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
