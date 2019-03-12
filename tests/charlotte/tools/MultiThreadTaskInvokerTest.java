package tests.charlotte.tools;

import charlotte.tools.MultiThreadTaskInvoker;

public class MultiThreadTaskInvokerTest {
	public static void main(String[] args) {
		try {
			test01();
			//test02();
			//test03();

			System.out.print("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		try(MultiThreadTaskInvoker mtti = new MultiThreadTaskInvoker()) {
			for(int c = 0; c < 10; c++) {
				mtti.addTask(() -> {
					System.out.println("*1");
					Thread.sleep(500);
					System.out.println("*2");
				});
			}
		}
	}

	private static void test02() {
		try {
			try(MultiThreadTaskInvoker mtti = new MultiThreadTaskInvoker()) {
				for(int c = 0; c < 1000; c++) {
					final int f_c = c;

					mtti.addTask(() -> {
						throw new Exception("c: " + f_c);
					});
				}
				mtti.relayThrow();
			}
		}
		catch(Throwable e) {
			e.printStackTrace(System.out);
		}
	}

	private static void test03() throws Exception {
		try(MultiThreadTaskInvoker mtti = new MultiThreadTaskInvoker()) {
			for(int c = 0; c < 1000000; c++) {
				mtti.addTask(() -> {
					// noop
				});
			}
			mtti.relayThrow();
		}
	}
}
