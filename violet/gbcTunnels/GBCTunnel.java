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

	private static void executeServer() {
		// TODO
	}
}
