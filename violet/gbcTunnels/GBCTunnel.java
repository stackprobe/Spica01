package violet.gbcTunnels;

import charlotte.tools.BinTools;
import charlotte.tools.Critical;
import charlotte.tools.ExceptionDam;
import charlotte.tools.HTTPClient;
import charlotte.tools.SecurityTools;
import charlotte.tools.SockChannel;
import charlotte.tools.SockServer;
import charlotte.tools.ThreadEx;
import violet.gbcTunnels.pumps.BoomerangPump;
import violet.gbcTunnels.pumps.CipherPump;
import violet.gbcTunnels.pumps.HTTPPump;
import violet.gbcTunnels.pumps.NamedTrackPump;

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
		Connection connection = new Connection();

		connection.credential = SecurityTools.cRandom.getBytes(Consts.CREDENTIAL_SIZE);
		connection.server = server;
		connection.channel = channel;

		Ground.connections.put(connection.credential, connection);

		connection.clientToServerTh = new ThreadEx(() -> clientToServerTh(connection));
		connection.serverToClientTh = new ThreadEx(() -> serverToClientTh(connection));

		connection.clientToServerTh.waitToEnd(SockChannel.critical);
		connection.serverToClientTh.waitToEnd(SockChannel.critical);

		pumpDisconnect(connection);

		Ground.connections.remove(connection.credential);
	}

	private static void clientToServerTh(Connection connection) throws Exception {
		SockChannel.critical.section_a(() -> {
			try {
				byte[] buff = new byte[1024 * 1024 * 4];

				while(Ground.death == false && connection.dead == false) {
					connection.channel.recv(buff, (data, offset, size) -> {
						PumpPacket packet = new PumpPacket(connection, BinTools.getSubBytes(data, offset, size));

						pump(packet);

						connection.serverToClientPackets.enqueue(packet);
						connection.waiter.kick();
					});
				}
			}
			catch(Throwable e) {
				System.out.println("C-TO-S-FAILED");
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
						PumpPacket packet = connection.serverToClientPackets.dequeue();

						connection.channel.send(packet.getResData());
						connection.waiter.kick();
					}
					else {
						PumpPacket packet = new PumpPacket(connection, BinTools.EMPTY);

						pump(packet);

						if(1 <= packet.getResData().length) {
							connection.channel.send(packet.getResData());
							connection.waiter.kick();
						}
					}
					connection.waiter.waitForMoment();
				}
			}
			catch(Throwable e) {
				System.out.println("S-TO-C-FAILED");
				e.printStackTrace(System.out);
			}
			finally {
				connection.dead = true;
			}
		});
	}

	private static void pumpDisconnect(Connection connection) {
		try {
			PumpPacket packet = new PumpPacket(connection, null);

			pump(packet); // will throw
		}
		catch(Throwable e) {
			// noop
		}
	}

	private static Critical _pumpCritical = new Critical();

	/**
	 * throws Exception when DISCONNECT, NETWORK-ERROR
	 *
	 * @param packet
	 * @throws Exception
	 */
	private static void pump(PumpPacket packet) throws Exception {
		SockChannel.critical.unsection_a(() -> _pumpCritical.enter());
		try {
			pump_noPumpCritical(packet);
		}
		finally {
			_pumpCritical.leave();
		}
	}

	private static void pump_noPumpCritical(PumpPacket packet) throws Exception {
		pump2(packet, 0);
	}

	private static IPump[] _pumps = new IPump[] {
			new NamedTrackPump(),
			new CipherPump(),
			new BoomerangPump(),
			new HTTPPump(),
			(packet, dummyPump) -> pump3(packet),
	};

	private static void pump2(PumpPacket packet, int index) throws Exception {
		_pumps[index].pump(packet, (p, np) -> pump2(p, index + 1));
	}

	private static void pump3(PumpPacket packet) throws Exception {
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
				HTTPClient hc = new HTTPClient(packet.url);

				if(GBCTunnelProps.proxyDomain != null) {
					hc.proxyDomain = GBCTunnelProps.proxyDomain;
					hc.proxyPortNo = GBCTunnelProps.proxyPortNo;
				}
				hc.get();
				packet.resBody = hc.resBody;
				break;
			}
			catch(Throwable e) {
				System.out.println("PUMP-FAILED");
				e.printStackTrace(System.out);
			}
		}
	}
}
