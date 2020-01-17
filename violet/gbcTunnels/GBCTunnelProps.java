package violet.gbcTunnels;

public class GBCTunnelProps {
	public static class Connector {
		public int portNo;
		public String trackName;

		public Connector(int portNo, String trackName) {
			this.portNo = portNo;
			this.trackName = trackName;
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
			"localhost";
			//*/

	public static int port =
			////// $_git:secret

			// $_git:secret
			80;
			//*/

	public static String passphrase =
			/////////////////////////////////// $_git:secret

			// $_git:secret
			"";
			//*/
}
