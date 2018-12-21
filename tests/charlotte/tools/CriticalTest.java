package tests.charlotte.tools;

import charlotte.tools.Critical;
import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;
import charlotte.tools.ThreadEx;

public class CriticalTest {
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

	private static Critical _test01_critical = new Critical();
	private static int _test01_count = 0;

	private static void test01() throws Exception {
		IQueue<ThreadEx> ths = new QueueUnit<ThreadEx>();

		for(int c = 0; c < 100; c++) {
			ths.enqueue(new ThreadEx(() -> {
				for(int d = 0; d < 100; d++) {
					_test01_critical.section(() -> {
						_test01_count++;

						/*
						{
							int tmp = _test01_count;

							Thread.sleep(1);

							_test01_count = tmp + 1;
						}
						*/
					});
				}
			}
			));
		}

		while(ths.hasElements()) {
			ths.dequeue().waitToEnd();
		}

		System.out.println("100 x 100 == " + _test01_count);
	}
}
