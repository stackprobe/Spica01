package wb.t20181211;

public class Test0002 {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		String str = "あいうえお"; // orig: String str = "\u3042\u3044\u3046\u3048\u304a";

		System.out.println(StrConv.i().toHalf(str));
	}
}
