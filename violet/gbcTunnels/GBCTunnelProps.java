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

	public static int boomerangSendDataSizeMax = 2500;

	public static String server =
			/////////////////////// $_git:secret

			// $_git:secret
			"localhost";
			//*/

	public static int portNo =
			////// $_git:secret

			// $_git:secret
			80;
			//*/

	/**
	 * null == no proxy
	 */
	public static String proxyDomain =
			//"router";
			null;

	public static int proxyPortNo =
			8080;

	public static String passphrase =
			/////////////////////////////////// $_git:secret

			// $_git:secret
			"";
			//*/
}
