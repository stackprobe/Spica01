package wb.t20181218.tests;

import java.io.File;

import javax.imageio.ImageIO;

import wb.t20181218.Thumbnail;

public class ThumbnailTest {
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
		Thumbnail thumb = new Thumbnail(100, 100);

		thumb.setFile("C:/temp/img0001.jpg");

		ImageIO.write(thumb.getImage(), "png", new File("C:/temp/img0002.png"));

		// ----

		Thumbnail thumb2 = new Thumbnail(1, 1);

		thumb2.set(thumb);

		ImageIO.write(thumb2.getImage(), "png", new File("C:/temp/img0003.png"));
	}
}
