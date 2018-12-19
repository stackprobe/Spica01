package wb.t20181219;

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
		Dim<String> lines = new Dim<String>("none", 2, 3, 4, 5);

		System.out.println(lines.get(0, 0, 0, 0));
		System.out.println(lines.get(1, 2, 3, 4));

		for(int a = 0; a < 2; a++) {
			for(int b = 0; b < 3; b++) {
				for(int c = 0; c < 4; c++) {
					for(int d = 0; d < 5; d++) {
						lines.set(String.format("%d_%d_%d_%d", a, b, c, d), a, b, c, d);
					}
				}
			}
		}

		for(int a = 0; a < 2; a++) {
			for(int b = 0; b < 3; b++) {
				for(int c = 0; c < 4; c++) {
					for(int d = 0; d < 5; d++) {
						System.out.println(String.format("%d_%d_%d_%d == %s", a ,b, c, d, lines.get(a, b, c, d)));
					}
				}
			}
		}
	}
}
