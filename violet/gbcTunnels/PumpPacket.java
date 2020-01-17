package violet.gbcTunnels;

public class PumpPacket {
	public Connection connection;
	public byte[] data;

	public PumpPacket(Connection connection, byte[] data) {
		this.connection = connection;
		this.data = data;
	}
}
