package tests.charlotte.tools;

import java.awt.Color;
import java.awt.Font;

import charlotte.tools.Canvas;
import charlotte.tools.Canvas2;

public class CanvasTest {
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
		Canvas canvas = new Canvas(600, 600);

		//canvas.fill(Color.CYAN);
		//canvas.fillCircle(Color.RED, 500, 500, 400);

		canvas.fill(Color.WHITE);
		canvas.drawCircle(Color.BLACK, 500, 500, 400, 300);

		canvas.save("C:/temp/1.png");
	}

	private static final String J_CANVAS = "キャンバス"; // orig: private static final String J_CANVAS = "\u30ad\u30e3\u30f3\u30d0\u30b9";

	private static void test02() throws Exception {
		test02_a("Canvas");
		test02_a(J_CANVAS);
		test02_a("Java 1.8");
	}

	private static int _test02_fileCount = 1;

	private static void test02_a(String str) throws Exception {
		Canvas canvas = new Canvas(600, 600);
		Canvas2 c2;

		canvas.fill(Color.WHITE);
		c2 = canvas.toCanvas2();
		c2.drawString(str, new Font("Meiryo", Font.PLAIN, 100), Color.BLACK, 300, 300);
		canvas = c2.toCanvas();

		canvas.save("C:/temp/" + _test02_fileCount++ + ".png");
	}
}
