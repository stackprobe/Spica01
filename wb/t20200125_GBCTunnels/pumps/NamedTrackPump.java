package wb.t20200125_GBCTunnels.pumps;

import charlotte.tools.BinTools;
import charlotte.tools.StringTools;
import wb.t20200125_GBCTunnels.Ground;

public class NamedTrackPump {
	public static byte[] pump(byte[] data) throws Exception {
		if(
				Ground.connections.get().trackNameSent == false &&
				Ground.connections.get().disconnect == false
				) {
			data = BinTools.join(new byte[][] {
				Ground.connections.get().server.connector.trackName.getBytes(StringTools.CHARSET_SJIS),
				new byte[] { 0x0d, 0x0a }, // CR-LF
				data
			});

			Ground.connections.get().trackNameSent = true;
		}
		return CipherPump.pump(data);
	}
}
