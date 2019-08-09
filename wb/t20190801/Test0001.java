package wb.t20190801;

import java.awt.Color;

import charlotte.tools.Canvas;
import charlotte.tools.DoubleTools;

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
		test01_a(
				//////////////////////////////////////////////////// $_git:secret
				////////////////////////////////////////////////////// $_git:secret
				);
	}

	private static void test01_a(String file) throws Exception {
		//test01_a_01(file);
		test01_a_02(file);
	}

	private static void test01_a_01(String file) throws Exception {
		Canvas canvas = new Canvas(file);

		for(int y = 0; y < canvas.getHeight(); y++) {
			for(int x = 0; x + 3 <= canvas.getWidth(); x += 3) {
				Color p = canvas.get(x, y);
				Color p2 = canvas.get(x + 1, y);
				Color p3 = canvas.get(x + 2, y);

				canvas.set(x, y, new Color(
						DoubleTools.toInt((p.getRed() + p2.getRed() + p3.getRed()) / 3.0),
						0,
						0,
						255
						));
				canvas.set(x + 1, y, new Color(
						0,
						DoubleTools.toInt((p.getGreen() + p2.getGreen() + p3.getGreen()) / 3.0),
						0,
						255
						));
				canvas.set(x + 2, y, new Color(
						0,
						0,
						DoubleTools.toInt((p.getBlue() + p2.getBlue() + p3.getBlue()) / 3.0),
						255
						));
			}
		}
		canvas.save("C:/temp/output.png");
	}

	private static void test01_a_02(String file) throws Exception {
		Canvas canvas = new Canvas(file);

		for(int x = 0; x + 2 <= canvas.getWidth(); x += 2) {
			for(int y = 0; y + 2 <= canvas.getHeight(); y += 2) {
				Color p = canvas.get(x, y);
				Color p2 = canvas.get(x, y + 1);
				Color p3 = canvas.get(x + 1, y);
				Color p4 = canvas.get(x + 1, y + 1);

				int r = DoubleTools.toInt((p.getRed()   + p2.getRed()   + p3.getRed()   + p4.getRed())   / 4.0);
				int g = DoubleTools.toInt((p.getGreen() + p2.getGreen() + p3.getGreen() + p4.getGreen()) / 4.0);
				int b = DoubleTools.toInt((p.getBlue()  + p2.getBlue()  + p3.getBlue()  + p3.getBlue())  / 4.0);
				int s = DoubleTools.toInt((r + g + b) / 3.0);

				canvas.set(x,     y,     new Color(s, s, s, 255));
				canvas.set(x,     y + 1, new Color(r, 0, 0, 255));
				canvas.set(x + 1, y + 1, new Color(0, g, 0, 255));
				canvas.set(x + 1, y,     new Color(0, 0, b, 255));
			}
		}
		canvas.save("C:/temp/output2.png");
	}
}
