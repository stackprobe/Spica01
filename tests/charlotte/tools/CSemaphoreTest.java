package tests.charlotte.tools;

import charlotte.tools.CSemaphore;
import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;
import charlotte.tools.ThreadEx;

public class CSemaphoreTest {
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

	private static CSemaphore _test01_semaphore = new CSemaphore(20);
	private static Object _test01_SYNCROOT = new Object();
	private static int _test01_count = 0;

	private static void test01() throws Exception {
		IQueue<ThreadEx> ths = new QueueUnit<ThreadEx>();

		for(int c = 0; c < 100; c++) {
			ths.enqueue(new ThreadEx(() -> {
				for(int d = 0; d < 100; d++) {
					_test01_semaphore.section(() -> {
						synchronized(_test01_SYNCROOT) {
							System.out.println("+ " + _test01_count);
							_test01_count++;
							System.out.println("> " + _test01_count);
						}

						Thread.sleep(1);

						synchronized(_test01_SYNCROOT) {
							System.out.println("- " + _test01_count);
							_test01_count--;
							System.out.println("> " + _test01_count);
						}
					});
				}
			}
			));
		}

		while(ths.hasElements()) {
			ths.dequeue().waitToEnd();
		}
	}
}
