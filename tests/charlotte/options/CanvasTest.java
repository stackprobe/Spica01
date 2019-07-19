package tests.charlotte.options;

import java.awt.Color;
import java.awt.Font;

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
		Canvas canvas = new Canvas(800, 600);

		canvas.fill(Color.BLUE);
		canvas.drawString("Canvas", new Font("メイリオ", Font.PLAIN, 200), Color.ORANGE, 400, 400);

		canvas.save("C:/temp/1.png");
	}
}
