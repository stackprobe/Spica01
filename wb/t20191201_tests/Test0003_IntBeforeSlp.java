package wb.t20191201_tests;

import charlotte.tools.DebugTools;

/**
 * sleepする前にinterruptしてもちゃんとinterruptedするかテスト
 *
 */
public class Test0003_IntBeforeSlp {
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

	private static Thread _th1;
	private static Thread _th2;

	private static void test01() throws Exception {
		_th1 = new Thread(() -> {
			for(int c = 0; c < 100000000; c++) {
				new String(); // dummy heavy operation (x1)
			}

			System.out.println("2 : interrupt go");
			_th2.interrupt();
			System.out.println("3 : interrupt done");
		});

		_th2 = new Thread(() -> {
			System.out.println("1 : _th2 started");

			for(int c = 0; c < 200000000; c++) {
				new String(); // dummy heavy operation (x2)
			}

			System.out.println("4 : sleep go");
			DebugTools.mustThrow(() -> Thread.sleep(5000)); // <---- java.lang.InterruptedException: sleep interrupted
			System.out.println("5 : sleep done");
		});

		_th1.start();
		_th2.start();

		_th1.join();
		_th2.join();
	}
}
