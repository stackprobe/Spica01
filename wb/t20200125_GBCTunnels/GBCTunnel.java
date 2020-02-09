package wb.t20200125_GBCTunnels;

import java.util.Scanner;

import charlotte.tools.BinTools;
import charlotte.tools.Critical;
import charlotte.tools.ExceptionDam;
import charlotte.tools.SecurityTools;
import charlotte.tools.SockChannel;
import charlotte.tools.SockServer;
import charlotte.tools.StringTools;
import charlotte.tools.ThreadEx;
import wb.t20200125_GBCTunnels.pumps.BoomerangPump;
import wb.t20200125_GBCTunnels.pumps.CipherPump;
import wb.t20200125_GBCTunnels.pumps.NamedTrackPump;
import wb.t20200125_GBCTunnels.utils.PumpHTTPClient;

public class GBCTunnel {
	public static void main(String[] args) {
		try {
			init();
			serverMain();

			System.out.println("end");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void init() throws Exception {
		if(GBCTunnelProps.server.isEmpty()) {
			GBCTunnelProps.server = input("server");
		}
		if(GBCTunnelProps.passphrase.isEmpty()) {
			GBCTunnelProps.passphrase = input("passphrase");
		}
		CipherPump.init();
		BoomerangPump.init();
	}

	private static String input(String title) throws Exception {
		Scanner sc = new Scanner(System.in); // memo: don't try

		for(; ; ) {
			System.out.println(String.format("Input %s:", title));
			String line = sc.nextLine();
			int chkDig = SecurityTools.getSHA512(line.getBytes(StringTools.CHARSET_UTF8))[0] & 0xff;
			System.out.println(String.format("Input %s: %s (%x)", title, line, chkDig));

			System.out.println("[Y/N]:");
			String yn = sc.nextLine();

			if(yn.toLowerCase().startsWith("y")) {
				return line;
			}
		}
	}

	private static void serverMain() throws Exception {
		Ground.servers = new Server[GBCTunnelProps.connectors.length];

		for(int index = 0; index < Ground.servers.length; index++) {
			Server server = new Server();
			server.connector = GBCTunnelProps.connectors[index];
			Ground.servers[index] = server;
		}

		SockChannel.critical.section_a(() -> {
			for(Server server : Ground.servers) {
				server.th = new ThreadEx(() -> serverTh(server));
			}
		});

		waitToUserEnd();

		Ground.death = true;

		SockChannel.critical.section_a(() -> {
			for(Server server : Ground.servers) {
				server.th.waitToEnd(SockChannel.critical);
			}

			ExceptionDam.section(eDam -> {
				for(Server server : Ground.servers) {
					eDam.invoke(() -> server.th.relayThrow());
				}
			});
		});
	}

	private static void waitToUserEnd() throws Exception {
		System.out.println("Press ENTER key to end.");
		System.in.read();
		System.out.println("ending...");
	}

	private static void serverTh(Server server) throws Exception {
		SockChannel.critical.section_a(() -> {
			SockServer ss = new SockServer() {
				@Override
				public boolean interlude() {
					return Ground.death == false;
				}

				@Override
				public void connected(SockChannel channel) throws Exception {
					connectedTh(server, channel);
				}
			};

			ss.portNo = server.connector.portNo;

			SockChannel.critical.unsection_a(() -> ss.perform());
		});
	}

	private static void connectedTh(Server server, SockChannel channel) throws Exception {
		channel.idleTimeoutMillis = -1;

		Connection connection = new Connection();

		connection.server = server;
		connection.channel = channel;

		connection.clientToServerTh = new ThreadEx(() -> clientToServerTh(connection));
		connection.serverToClientTh = new ThreadEx(() -> serverToClientTh(connection));
		connection.pumpTh = new ThreadEx(() -> pumpTh(connection));

		connection.clientToServerTh.waitToEnd(SockChannel.critical);
		connection.serverToClientTh.waitToEnd(SockChannel.critical);
		connection.pumpTh.waitToEnd(SockChannel.critical);
	}

	private static void clientToServerTh(Connection connection) throws Exception {
		SockChannel.critical.section_a(() -> {
			try {
				byte[] buff = new byte[Consts.sockRecvBuffSize];

				while(
						Ground.death == false &&
						connection.serverToClientDead == false
						) {
					connection.channel.recv(buff, (data, offset, size) -> {
						connection.clientToServerBuff.enqueue(BinTools.getSubBytes(data, 0, size));
						connection.clientToServerWaiter.kick();
					});
				}
			}
			catch(Throwable e) {
				//e.printStackTrace(System.out);
				System.out.println("[C2S] " + e.getClass() + ": " + e.getMessage());
			}
			finally {
				connection.clientToServerDead = true;
			}
		});
	}

	private static void serverToClientTh(Connection connection) throws Exception {
		SockChannel.critical.section_a(() -> {
			try {
				while(
						Ground.death == false &&
						(
								connection.serverToClientBuff.hasElements() ||
								connection.pumpDead == false
						)
						) {
					while(connection.serverToClientBuff.hasElements()) {
						byte[] resData = connection.serverToClientBuff.dequeue();

						connection.channel.send(resData);
					}
					connection.serverToClientWaiter.waitForMoment();
				}
			}
			catch(Throwable e) {
				//e.printStackTrace(System.out);
				System.out.println("[S2C] " + e.getClass() + ": " + e.getMessage());
			}
			finally {
				connection.serverToClientDead = true;
			}
		});
	}

	private static void pumpTh(Connection connection) throws Exception {
		SockChannel.critical.section_a(() -> {
			try {
				Ground.connections.set(connection);

				while(
						Ground.death == false &&
						(
								connection.clientToServerBuff.hasElements() ||
								connection.clientToServerDead == false
						)
						) {
					byte[] data;

					if(connection.clientToServerBuff.hasElements()) {
						data = connection.clientToServerBuff.dequeue();

						connection.clientToServerWaiter.reset();
					}
					else {
						data = BinTools.EMPTY;
					}
					byte[] resData = pump(data);

					if(1 <= resData.length) {
						connection.serverToClientBuff.enqueue(resData);
						connection.serverToClientWaiter.kick();

						connection.clientToServerWaiter.reset();
					}
					connection.clientToServerWaiter.waitForMoment();
				}
			}
			catch(Throwable e) {
				//e.printStackTrace(System.out);
				System.out.println("[Pump] " + e.getClass() + ": " + e.getMessage());
			}
			finally {
				pumpDisconnect(connection);

				Ground.connections.set(null);

				connection.pumpDead = true;
			}
		});
	}

	private static void pumpDisconnect(Connection connection) {
		connection.disconnect = true;

		try {
			pump(BinTools.EMPTY); // will throw
		}
		catch(Throwable e) {
			// noop
		}
	}

	private static Critical _pumpCritical = new Critical();

	/**
	 * throws Exception when DISCONNECT, NETWORK-ERROR
	 *
	 * @param data
	 * @return resData
	 * @throws Exception
	 */
	private static byte[] pump(byte[] data) throws Exception {
		SockChannel.critical.unsection_a(() -> _pumpCritical.enter());
		try {
			return pump_noLock(data);
		}
		finally {
			_pumpCritical.leave();
		}
	}

	private static byte[] pump_noLock(byte[] data) throws Exception {
		/*
		 * Calling sequence
		 *
		 * --> NamedTrackPump
		 * --> CipherPump
		 * --> BoomerangPump
		 * --> HTTPPump
		 * --> pump2
		 */
		return NamedTrackPump.pump(data);
	}

	public static byte[] pump2(String url) throws Exception {
		for(int trial = 1; ; trial++) {
			if(8 <= trial) {
				throw new Exception("PUMP-TRIAL-OVER");
			}
			if(2 <= trial) {
				SockChannel.critical.unsection_a(() -> Thread.sleep(2000));
			}
			if(Ground.death) {
				throw new Exception("DEATH");
			}

			try {
				PumpHTTPClient hc = new PumpHTTPClient();

				if(GBCTunnelProps.proxyDomain != null) {
					hc.proxyDomain = GBCTunnelProps.proxyDomain;
					hc.proxyPortNo = GBCTunnelProps.proxyPortNo;
				}
				hc.get(url);

				return hc.resBody;
			}
			catch(Throwable e) {
				System.out.println("PUMP-FAILED");
				e.printStackTrace(System.out);
			}
		}
	}
}
