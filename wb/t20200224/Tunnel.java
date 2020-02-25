package wb.t20200224;

import charlotte.tools.BinTools;
import charlotte.tools.ExceptionDam;
import charlotte.tools.SockChannel;
import charlotte.tools.SockClient;
import charlotte.tools.SockServer;
import charlotte.tools.StringTools;
import charlotte.tools.ThreadEx;

public class Tunnel {
	public static void main(String[] args) {
		try {
			new Tunnel().perform();

			System.out.println("End");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	//public String forwardHost = "localhost";
	public String forwardHost = "192.168.123.100";
	//public int forwardPort = 8080;
	public int forwardPort = 60080;
	public int recvPort = 80;

	public void perform() throws Exception {
		SockServer ss = new SockServer() {
			private int connectCount = 0;

			@Override
			public void connected(SockChannel channel) throws Exception {
				new ChannelTunnel(channel, connectCount++).perform();
			}
		};

		ss.portNo = recvPort;

		ss.perform();
	}

	private class ChannelTunnel {
		private SockChannel _channel;
		private int _connectNo;

		public ChannelTunnel(SockChannel channel, int connectNo) {
			_channel = channel;
			_connectNo = connectNo;
		}

		public void perform() throws Exception {
			ExceptionDam.section(eDam -> SockChannel.critical.unsection_a(() -> {
				System.out.println("[CONNECTED] ID:" + _connectNo + " Client->Tunnel");

				try(SockClient forwardChannel = new SockClient()) {
					forwardChannel.connect(forwardHost, forwardPort);

					System.out.println("[CONNECTED] ID:" + _connectNo + " Tunnel->Forward");

					@SuppressWarnings("resource")
					ThreadEx th = new ThreadEx(() -> {
						SockChannel.critical.section_a(() -> {
							channelPerform(_channel, forwardChannel, "UP");
						});
					});

					eDam.invoke(() -> {
						channelPerform(forwardChannel, _channel, "DOWN");
					});

					th.waitToEnd(SockChannel.critical);

					eDam.invoke(() -> th.relayThrow());
				}

				System.out.println("[DISCONNECT] ID:" + _connectNo);
			}
			));
		}

		private void channelPerform(SockChannel recvChannel, SockChannel sendChannel, String direction) throws Exception {
			byte[] buff = new byte[65536];

			for(; ; ) {
				recvChannel.recv(buff, (data, offset, length) -> {
					printData(direction, data, offset, length);
					sendChannel.send(data, offset, length);
				});
			}
		}

		private void printData(String direction, byte[] data, int offset, int length) throws Exception {
			System.out.println(
					"[" +
					direction +
					"] ID:" +
					_connectNo
					);

			System.out.println(
					new String(BinTools.getSubBytes(data, offset, offset + length), StringTools.CHARSET_ASCII)
					);
		}
	}
}
