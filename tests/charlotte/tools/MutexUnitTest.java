package tests.charlotte.tools;

import charlotte.tools.MutexUnit;

public class MutexUnitTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		try(MutexUnit m = new MutexUnit("AAA")) {
			m.waitOne();
		}
	}

	private static void test02() throws Exception {
		try(MutexUnit m = new MutexUnit("AAA")) {
			m.waitOne();

			try(MutexUnit m2 = new MutexUnit("AAA")) {
				System.out.println("wait 2 sec...");
				if(m2.waitOne(2000)) throw null;
				System.out.println("done");
			}

			m.releaseMutex();

			try(MutexUnit m2 = new MutexUnit("AAA")) {
				if(m2.waitOne(2000) == false) throw null;
			}
		}
	}
}
