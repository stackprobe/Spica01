package violet.gbcTunnels.tests;

import charlotte.tools.SockChannel;
import charlotte.tools.ThreadEx;
import violet.gbcTunnels.KickableWaiter;

public class KickableWaiterTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static boolean _death = false;

	private static void test01() throws Exception { // TODO del ???
		SockChannel.critical.section_a(() -> {
			KickableWaiter waiter = new KickableWaiter(false);

			ThreadEx thWaitLoop = new ThreadEx(() -> {
				SockChannel.critical.section_a(() -> {
					while(_death == false) {
						waiter.waitForMoment();

						System.out.println("WAKEUP!");
					}
				});
			});

			ThreadEx thWakeup = new ThreadEx(() -> {
				SockChannel.critical.section_a(() -> {
					for(; ; ) {
						int chr = SockChannel.critical.unsection_get(() -> System.in.read());

						if(chr == 'Q') {
							System.out.println("BREAK!");
							break;
						}
						if(chr == 'W') {
							System.out.println("KICK!");
							waiter.kick();
						}
					}
					_death = true;
				});
			});

			thWaitLoop.waitToEnd(SockChannel.critical);
			thWakeup.waitToEnd(SockChannel.critical);
		});
	}
}
