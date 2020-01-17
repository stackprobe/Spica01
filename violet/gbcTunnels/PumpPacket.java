package violet.gbcTunnels;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.RTError;

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

	public PumpPacket(PumpPacket owner) {
		this(owner.connection, null); // set data later
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

	public byte[] readFromResData(int size) {
		byte[] resData = getResData();

		if(resData.length < size) {
			throw new RTError("Bad size: " + size);
		}
		resDataParts.set(0, BinTools.getSubBytes(resData, size));
		return BinTools.getSubBytes(resData, 0, size);
	}
}
