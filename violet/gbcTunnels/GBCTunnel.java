package violet.gbcTunnels;

public class GBCTunnel {
	public static void main(String[] args) {
		try {
			perform();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void perform() throws Exception {
		GBCTunnelGround.pump = new Pump();
		GBCTunnelGround.pump.start();
		try {
			executeServer();
		}
		finally {
			GBCTunnelGround.pump.end();
			GBCTunnelGround.pump = null;
		}
	}

	private static void executeServer() throws Exception {
		int serverNum = GBCTunnelProps.connectors.length;
		CCServer[] servers = new CCServer[serverNum];

		for(int index = 0; index < serverNum; index++) {
			servers[index] = new CCServer(GBCTunnelProps.connectors[index]);
			servers[index].start();
		}
		waitToUserEnd();

		for(int index = 0; index < serverNum; index++) {
			servers[index].end();
		}
	}

	private static void waitToUserEnd() throws Exception {
		System.in.read();
	}
}
