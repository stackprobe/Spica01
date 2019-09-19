package wb.t20190918;

import java.awt.Color;

import charlotte.tools.Canvas;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		Canvas canvas = new Canvas(970, 530);

		canvas.fill(new Color(250, 200, 100));
		canvas.fillRect(new Color(200, 160, 80), 0, 24 * 8, canvas.getWidth(), canvas.getHeight() - (24 + 8) * 8);
		canvas.fillRect(new Color(150, 120, 60), 0, 24 * 12, canvas.getWidth(), canvas.getHeight() - (24 + 8) * 12);
		canvas.fillRect(new Color(100, 80, 40), 0, 24 * 14, canvas.getWidth(), canvas.getHeight() - (24 + 8) * 14);
		canvas.fillRect(new Color(50, 40, 20), 0, 24 * 15, canvas.getWidth(), canvas.getHeight() - (24 + 8) * 15);

		canvas.save("C:/temp/DungBackground.png");
	}
}
