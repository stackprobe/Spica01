package tests.charlotte.tools;

import charlotte.tools.MultiThreadEx;

public class MultiThreadExTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.print("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		try {
			try(MultiThreadEx mte = new MultiThreadEx()) {
				mte.add(() -> { });
				mte.add(() -> { });
				mte.add(() -> { });
				mte.add(() -> { throw new Exception("REIGAI_A"); });
				mte.add(() -> { throw new Exception("REIGAI_B"); });
				mte.add(() -> { throw new Exception("REIGAI_C"); });
				mte.add(() -> { });
				mte.add(() -> { });
				mte.add(() -> { });

				mte.relayThrow();
			}
		}
		catch(Throwable e) {
			e.printStackTrace(System.out);
		}
	}
}
