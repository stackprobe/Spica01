package wb.t20181115;

public class Test0003 {
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
		System.out.println(EraCalendar.i().getEraDate(20200209));
	}
}
