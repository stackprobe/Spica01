package tests.charlotte.tools;

import charlotte.tools.SockChannel;
import charlotte.tools.SockServer;

public class SockServerTest {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		SockServer ss = new SockServer() {
			@Override
			public void connected(SockChannel channel) throws Exception {
				System.out.println("**0");

				byte[] buff = new byte[10];

				for(int c = 0; c < 3; c++) {
					System.out.println("**1");
					channel.recv(buff);
					System.out.println("**2");
					channel.send(buff);
					System.out.println("**3");
				}
				System.out.println("**4");
			}
		};

		System.out.println("*1");
		ss.start();
		System.out.println("*2");

		System.in.read();

		System.out.println("*3");
		ss.stop_B();
		System.out.println("*4");
	}
}
