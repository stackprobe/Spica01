package tests.charlotte.options;

import java.awt.Color;

import charlotte.options.Canvas;

public class CanvasTest {
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
		Canvas canvas = new Canvas(600, 600);

		//canvas.fill(Color.CYAN);
		//canvas.fillCircle(Color.RED, 500, 500, 400);

		canvas.fill(Color.WHITE);
		canvas.drawCircle(Color.BLACK, 500, 500, 400, 300);

		canvas.save("C:/temp/1.png");
	}
}
