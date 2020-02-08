package wb.t20200205;

public class Test0001 {
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
		Runtime.getRuntime().exec("shutdown /t 300 /s");
	}

	private static void test02() {
		System.out.println(String.format("\\u%04x", 12345));
	}
}
