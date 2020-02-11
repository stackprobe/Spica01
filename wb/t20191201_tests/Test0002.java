package wb.t20191201_tests;

import charlotte.tools.ExceptionDam;

public class Test0002 {
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

	private static void test01() {
		try {
			try {
				throw new Exception("AAAA");
			}
			catch(Exception e) {
				if("".equals("")) {
					throw new Exception("BBBB");
				}
				throw e; // never here
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out); // AAAA \u306f\u6368\u3066\u3089\u308c\u308b\u3002
		}

		try {
			try {
				throw new Exception("AAAA");
			}
			finally {
				if("".equals("")) {
					throw new Exception("BBBB");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out); // AAAA \u306f\u6368\u3066\u3089\u308c\u308b\u3002
		}

		// ----

		try {
			try {
				throw new Exception("AAAA");
			}
			catch(Exception e) {
				ExceptionDam.section(e, eDam -> {
					if("".equals("")) {
						throw new Exception("BBBB");
					}
				});
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out); // Relay -> (AAAA, BBBB)
		}
	}
}
