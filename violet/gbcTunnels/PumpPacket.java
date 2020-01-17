package violet.gbcTunnels;

import java.util.ArrayList;
import java.util.List;

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
	public List<byte[]> resDataList = new ArrayList<byte[]>();
}
