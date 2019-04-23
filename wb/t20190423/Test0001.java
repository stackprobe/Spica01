package wb.t20190423;

public class Test0001 {
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
		Test0002.a("ABC");
		Test0002.b("123");

		Test0002<String> i = new Test0002<String>();

		i.ia("DEF");;
		i.ib("456");
	}
}
