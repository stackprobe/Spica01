package violet.tunnel;

public class GBCTunnel {
	public static void main(String[] args) {
		try {
			main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void main2() {
		startPump();
		executeServer();
		endPump();
	}

	private static void executeServer() {
		// TODO
	}

	private static void startPump() {
		// TODO
	}

	private static void endPump() {
		// TODO
	}
}
