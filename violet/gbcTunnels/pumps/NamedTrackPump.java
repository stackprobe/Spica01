package violet.gbcTunnels.pumps;

import charlotte.tools.BinTools;
import charlotte.tools.StringTools;
import violet.gbcTunnels.Ground;

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
		}
		return CipherPump.pump(data);
	}
}
