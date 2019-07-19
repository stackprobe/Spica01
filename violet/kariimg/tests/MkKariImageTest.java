package violet.kariimg.tests;

import violet.kariimg.MkKariImage;

public class MkKariImageTest {
	public static void main(String[] args) {
		try {
			new MkKariImage().perform();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
