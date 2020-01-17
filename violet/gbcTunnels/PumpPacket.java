package violet.gbcTunnels;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;

public class PumpPacket {
	public Connection connection;

	/**
	 * null == DISCONNECT
	 */
	public byte[] data;

	public PumpPacket(Connection connection, byte[] data) {
		this.connection = connection;
		this.data = data;
	}

	public PumpPacket(PumpPacket original) {
		this(original.connection, original.data);
	}

	public String url;
	public byte[] resBody;
	public List<byte[]> resDataParts = new ArrayList<byte[]>();

	public byte[] getResData() {
		if(resDataParts.size() == 0) {
			return BinTools.EMPTY;
		}
		if(2 <= resDataParts.size()) {
			byte[] tmp = BinTools.join(resDataParts);

			resDataParts.clear();
			resDataParts.add(tmp);
		}
		return resDataParts.get(0);
	}
}
