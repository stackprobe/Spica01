package violet.gbcTunnels;

import charlotte.tools.ExceptionDam;
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
		System.in.read(); // XXX
	}

	private static void serverTh(Server server) throws Exception {
		SockChannel.critical.section_a(() -> {
			SockServer ss = new SockServer() {
				@Override
				public void connected(SockChannel channel) throws Exception {
					SockChannel.critical.unsection_a(() -> connectedTh(server, channel)); // TODO mod-del
				}
			};

			ss.portNo = server.connector.portNo;

			SockChannel.critical.unsection_a(() -> ss.perform());
		});
	}

	private static void connectedTh(Server server, SockChannel channel) throws Exception {
		SockChannel.critical.section_a(() -> { // TODO del
			Connection connection = new Connection();

			connection.server = server;
			connection.channel = channel;

			connection.clientToServerTh = new ThreadEx(() -> clientToServerTh(connection));
			connection.serverToClientTh = new ThreadEx(() -> serverToClientTh(connection));

			connection.clientToServerTh.waitToEnd(SockChannel.critical);
			connection.serverToClientTh.waitToEnd(SockChannel.critical);
		});
	}

	private static void clientToServerTh(Connection connection) throws Exception {
		SockChannel.critical.section_a(() -> {
			byte[] buff = new byte[GBCTunnelProps.pumpSendRecvSizeMax];

			connection.channel.recv(buff, (data, offset, size) -> {

			});
		});
	}

	private static void serverToClientTh(Connection connection) throws Exception {
		SockChannel.critical.section_a(() -> {
			// TODO
		});
	}

	private static void pumpTh() throws Exception {
		SockChannel.critical.section_a(() -> {
			// TODO
		});
	}
}
