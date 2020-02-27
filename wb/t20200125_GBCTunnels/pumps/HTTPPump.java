package wb.t20200125_GBCTunnels.pumps;

import charlotte.tools.Base64Unit;
import charlotte.tools.DateTimeToSec;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;
import wb.t20200125_GBCTunnels.GBCTunnel;
import wb.t20200125_GBCTunnels.GBCTunnelProps;

public class HTTPPump {
	private static Base64Unit.NoPadding _base64 = Base64Unit.createByC6364P("-_=").noPadding();
	private static long _seq = -1L;

	public static byte[] pump(byte[] data) throws Exception {
		if(_seq == -1L) {
			_seq = DateTimeToSec.Now.getDateTime() * 100000L;
		}
		String url = "http://" + GBCTunnelProps.server + ":" + GBCTunnelProps.portNo + "/" + _seq + "/blueSteel/" + _base64.encode(data) + ".html";
		_seq++;

		byte[] resBody = GBCTunnel.pump2(url);

		String sResBody = new String(resBody, StringTools.CHARSET_ASCII);
		String sResData = resBodyStringToResDataEnclosed(sResBody).inner();
		sResData = eraseGomiChars(sResData);
		byte[] resData = _base64.decode(sResData);

		return resData;
	}

	private static StringTools.Enclosed resBodyStringToResDataEnclosed(String sResBody) {
		StringTools.Enclosed encl = StringTools.getEnclosedIgnoreCase(sResBody, "<caption>", "</caption>");

		if(encl != null) {
			return encl;
		}
		encl = StringTools.getEnclosedIgnoreCase(sResBody, "<th>", "</th>");

		if(encl != null) {
			return encl;
		}
		encl = StringTools.getEnclosedIgnoreCase(sResBody, "<td>", "</td>");

		if(encl != null) {
			return encl;
		}
		throw new RTError("Bad sResBody");
	}

	private static String eraseGomiChars(String sResData) {
		StringBuffer buff = new StringBuffer();

		for(char chr : sResData.toCharArray()) {
			if(StringTools.contains(StringTools.ALPHA + StringTools.alpha + StringTools.DECIMAL + "-_", chr)) {
				buff.append(chr);
			}
		}
		return buff.toString();
	}
}
