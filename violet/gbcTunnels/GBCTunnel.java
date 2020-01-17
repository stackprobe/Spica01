package violet.gbcTunnels;

import charlotte.tools.BinTools;
import charlotte.tools.ExceptionDam;
import charlotte.tools.HTTPClient;
import charlotte.tools.SecurityTools;
import charlotte.tools.SockChannel;
import charlotte.tools.SockServer;
import charlotte.tools.ThreadEx;

public class GBCTunnel {
	public static void main(String[] args) {
		try {
			perform();

			System.out.println("end");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void perform() throws Exception {
		SockChannel.critical.section_a(() -> {
			Ground.servers = new Server[GBCTunnelProps.connectors.length];

			for(int index = 0; index < Ground.servers.length; index++) {
				Server server = new Server();
				server.connector = GBCTunnelProps.connectors[index];
				Ground.servers[index] = server;
			}
			for(Server server : Ground.servers) {
				final Server f_server = server;
				server.th = new ThreadEx(() -> serverTh(f_server));
			}
			waitToUserEnd();

			Ground.death = true;

			for(Server server : Ground.servers) {
				server.th.waitToEnd(SockChannel.critical);
			}
			ExceptionDam.section(eDam -> {
				for(Server server : Ground.servers) {
					final Server f_server = server;
					eDam.invoke(() -> f_server.th.relayThrow());
				}
			});
		});
	}

	private static void waitToUserEnd() throws Exception {
		System.out.println("Press ENTER key to end.");

		SockChannel.critical.unsection_a(() -> System.in.read());

		System.out.println("ending...");
	}

	private static void serverTh(Server server) throws Exception {
		SockChannel.critical.section_a(() -> {
			SockServer ss = new SockServer() {
				@Override
				public void connected(SockChannel channel) throws Exception {
					SockChannel.critical.unsection_a(() -> connectedTh(server, channel)); // HACK mod-del
				}
			};

			ss.portNo = server.connector.portNo;

			SockChannel.critical.unsection_a(() -> ss.perform());
		});
	}

	private static void connectedTh(Server server, SockChannel channel) throws Exception {
		SockChannel.critical.section_a(() -> { // HACK del
			Connection connection = new Connection();

			connection.credential = SecurityTools.cRandom.getBytes(Consts.CREDENTIAL_SIZE);
			connection.server = server;
			connection.channel = channel;

			Ground.connections.put(connection.credential, connection);

			connection.clientToServerTh = new ThreadEx(() -> clientToServerTh(connection));
			connection.serverToClientTh = new ThreadEx(() -> serverToClientTh(connection));

			connection.clientToServerTh.waitToEnd(SockChannel.critical);
			connection.serverToClientTh.waitToEnd(SockChannel.critical);

			Ground.connections.remove(connection.credential);
		});
	}

	private static void clientToServerTh(Connection connection) throws Exception {
		SockChannel.critical.section_a(() -> {
			try {
				byte[] buff = new byte[GBCTunnelProps.pumpPacketDataSizeMax];

				while(Ground.death == false && connection.dead == false) {
					connection.channel.recv(buff, (data, offset, size) -> {
						PumpPacket packet = new PumpPacket(connection, BinTools.getSubBytes(data, offset, size));
						PumpPacket resPacket = pump(packet);

						connection.serverToClientPackets.enqueue(resPacket);
						connection.waiter.kick();
					});
				}
			}
			catch(Throwable e) {
				System.out.println("C-TO-S-FAULT");
				e.printStackTrace(System.out);
			}
			finally {
				connection.dead = true;
			}
		});
	}

	private static void serverToClientTh(Connection connection) throws Exception {
		SockChannel.critical.section_a(() -> {
			try {
				while(Ground.death == false && connection.dead == false) {
					if(connection.serverToClientPackets.hasElements()) {
						PumpPacket resPacket = connection.serverToClientPackets.dequeue();

						connection.channel.send(resPacket.data);
						connection.waiter.kick();
					}
					else {
						PumpPacket packet = new PumpPacket(connection, BinTools.EMPTY);
						PumpPacket resPacket = pump(packet);

						if(1 <= resPacket.data.length) {
							connection.channel.send(resPacket.data);
							connection.waiter.kick();
						}
					}
					connection.waiter.waitForMoment();
				}
			}
			catch(Throwable e) {
				System.out.println("S-TO-C-FAULT");
				e.printStackTrace(System.out);
			}
			finally {
				connection.dead = true;
			}
		});
	}

	private static PumpPacket pump(PumpPacket packet) throws Exception {
		String url = serialize(packet);
		byte[] resData;

		for(int trial = 1; ; trial++) {
			if(8 <= trial) {
				throw new Exception("PUMP-TRIAL-OVER");
			}
			if(2 <= trial) {
				SockChannel.critical.unsection_a(() -> Thread.sleep(2000));
			}
			if(Ground.death) {
				throw new Exception("PUMP-DEATH");
			}

			try {
				HTTPClient hc = new HTTPClient(url);

				if(GBCTunnelProps.proxyDomain != null) {
					hc.proxyDomain = GBCTunnelProps.proxyDomain;
					hc.proxyPortNo = GBCTunnelProps.proxyPortNo;
				}
				hc.get();
				resData = hc.resBody;
				break;
			}
			catch(Throwable e) {
				System.out.println("PUMP-FAULT");
				e.printStackTrace(System.out);
			}
		}
		PumpPacket resPacket = deserialize(packet, resData);
		return resPacket;
	}

	private static String serialize(PumpPacket packet) {
		throw null; // TODO
	}

	/**
	 * throw Exception when ERROR or DISCONNECT
	 *
	 * @param packet
	 * @param data
	 * @return
	 */
	private static PumpPacket deserialize(PumpPacket packet, byte[] data) {
		throw null; // TODO
	}
}
