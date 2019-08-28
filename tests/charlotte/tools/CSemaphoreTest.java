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

	private static void test01() throws Exception {
		test01_a(10);
		test01_a(20);
		test01_a(30);
		test01_a(40);
		test01_a(50);
		test01_a(60);
		test01_a(70);
		test01_a(80);
		test01_a(90);
		test01_a(100);
	}

	private static Object _test01_SYNCROOT = new Object();
	private static int _test01_count;
	private static int _test01_countMax;

	private static void test01_a(int permit) throws Exception {
		IQueue<ThreadEx> ths = new QueueUnit<ThreadEx>();
		CSemaphore semaphore = new CSemaphore(permit);

		_test01_count = 0;
		_test01_countMax = 0;

		for(int c = 0; c < 100; c++) {
			ths.enqueue(new ThreadEx(() -> {
				for(int d = 0; d < 100; d++) {
					semaphore.section_a(() -> {
						synchronized(_test01_SYNCROOT) {
							//System.out.println("+ " + _test01_count);
							_test01_count++;
							_test01_countMax = Math.max(_test01_countMax, _test01_count);
							//System.out.println("> " + _test01_count);
						}

						Thread.sleep(1);

						synchronized(_test01_SYNCROOT) {
							//System.out.println("- " + _test01_count);
							_test01_count--;
							//System.out.println("> " + _test01_count);
						}
					});
				}
			}
			));
		}

		while(ths.hasElements()) {
			ths.dequeue().waitToEnd();
		}

		System.out.println(permit + " -> " + _test01_countMax + ", " + _test01_count);
	}
}
