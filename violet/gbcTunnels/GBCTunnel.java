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
			Ground.pump = new Pump();
			Ground.pump.th = new ThreadEx(() -> pumpTh());

			waitToUserEnd();

			Ground.death = true;

			for(Server server : Ground.servers) {
				server.th.waitToEnd(SockChannel.critical);
			}
			Ground.pump.th.waitToEnd(SockChannel.critical);

			ExceptionDam.section(eDam -> {
				for(Server server : Ground.servers) {
					final Server f_server = server;
					eDam.invoke(() -> f_server.th.relayThrow());
				}
				eDam.invoke(() -> Ground.pump.th.relayThrow());
			});
		});
	}

	private static void waitToUserEnd() throws Exception {
		System.out.println("Press ESCAPE to exit.");

		SockChannel.critical.unsection_a(() -> {
			for(; ; ) {
				int chr = System.in.read();

				if(chr == 0x1b) {
					break;
				}
			}
		});

		System.out.println("exiting...");
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
						PumpPacket pumpPacket = new PumpPacket();

						pumpPacket.credential = connection.credential;
						pumpPacket.data = BinTools.getSubBytes(data, offset, size);

						Ground.pump.clientToServerPackets.enqueue(pumpPacket);
						Ground.pump.waiter.kick();
					});
				}
			}
			catch(Throwable e) {
				e.printStackTrace(System.out); // maybe disconnected or network error
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
					while(connection.serverToClientPackets.hasElements()) {
						PumpPacket pumpPacket = connection.serverToClientPackets.dequeue();

						connection.channel.send(pumpPacket.data);
					}
					connection.waiter.waitForMoment();
				}
			}
			catch(Throwable e) {
				e.printStackTrace(System.out); // maybe disconnected or network error
			}
			finally {
				connection.dead = true;
			}
		});
	}

	private static void pumpTh() throws Exception {
		SockChannel.critical.section_a(() -> {
			while(Ground.death == false) {
				while(Ground.pump.clientToServerPackets.hasElements()) {
					PumpPacket pumpPacket = Ground.pump.clientToServerPackets.dequeue();
					PumpPacket resPumpPacket = null;
					String url = serialize(pumpPacket);

					for(; ; ) {
						if(Ground.death) {
							return;
						}

						try {
							HTTPClient hc = new HTTPClient(url);

							if(GBCTunnelProps.proxyDomain != null) {
								hc.proxyDomain = GBCTunnelProps.proxyDomain;
								hc.proxyPortNo = GBCTunnelProps.proxyPortNo;
							}
							hc.get();

							resPumpPacket = deserialize(hc.resBody);

							break;
						}
						catch(Throwable e) {
							e.printStackTrace(System.out); // maybe (server | network) error
						}

						SockChannel.critical.unsection_a(() -> Thread.sleep(2000)); // wait by (server | network) error
					}

					if(resPumpPacket.flag == Consts.FLAG_NONE) {
						throw null; // TODO
					}
					else {
						throw null; // TODO
					}
				}
				Ground.pump.waiter.waitForMoment();
			}
		});
	}

	private static String serialize(PumpPacket pumpPacket) {
		throw null; // TODO
	}

	private static PumpPacket deserialize(byte[] data) {
		throw null; // TODO
	}
}
