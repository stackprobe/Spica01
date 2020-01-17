package violet.gbcTunnels;

public class PumpPacket {
	public byte[] credential;
	public byte[] data;
	public boolean foregroundFlag = false;
	public int flag = Consts.FLAG_NONE;
}
