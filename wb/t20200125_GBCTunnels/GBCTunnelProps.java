package wb.t20200125_GBCTunnels;

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

	/**
	 * "" == input when running
	 */
	public static String server =
			//"localhost"; // test
			/////////////////////// $_git:secret

			// $_git:secret
			//"localhost";
			"";
			//*/

	public static int portNo =
			//60003; // test
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

	/**
	 * "" == input when running
	 */
	public static String passphrase =
			//""; // test
			/////////////////////////////////// $_git:secret

			// $_git:secret
			"";
			//*/
}
