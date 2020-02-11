package wb.t20190423;

public class Test0002<T> {
	public void ia(T value) {
		System.out.println("this is ia() " + value);
	}

	public <U> void ib(U value) {
		System.out.println("this is ib() " + value);
	}

	// ng -- T が見えない。 // orig: // ng -- T \u304c\u898b\u3048\u306a\u3044\u3002
	/*
	public static void a(T value) {
		System.out.println("" + value);
	}
	*/

	public static <T> void a(T value) {
		System.out.println("this is a() " + value);
	}

	public static <U> void b(U value) {
		System.out.println("this is b() " + value);
	}
}
