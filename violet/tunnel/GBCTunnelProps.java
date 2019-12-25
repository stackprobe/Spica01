package violet.tunnel;

public class GBCTunnelProps {
	public static class Connector {
		public int port;
		public String name;

		public Connector(int port, String name) {
			this.port = port;
			this.name = name;
		}
	}

	public static Connector[] connectors = new Connector[] {
			new Connector(8080, "UPub"),
			new Connector(8888, "HPrx"),
			new Connector(59101, "RedI"),
			new Connector(59102, "RedI2"),
	};

	public static String server =
			/////////////////////// $_git:secret

			// $_git:secret
			"";
			//*/

	public static int port =
			////// $_git:secret

			// $_git:secret
			0;
			//*/
}
