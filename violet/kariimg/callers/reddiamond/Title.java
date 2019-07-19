package violet.kariimg.callers.reddiamond;

import java.awt.Color;
import java.awt.Font;

import violet.kariimg.MkKariImage;

public class Title {
	public static void main(String[] args) {
		try {
			main01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void main01() throws Exception {
		{
			MkKariImage mki = new MkKariImage();

			mki.frameColor = new Color(255, 255, 255);
			mki.backColor = new Color(255, 255, 255, 0);
			mki.foreColor = new Color(255, 255, 255);
			mki.corner = MkKariImage.CORNER_SHARP;
			mki.frameWidth = 5;
			mki.width = 400;
			mki.height = 400;
			mki.fontName = "游ゴシック";
			mki.fontStyle = Font.BOLD;
			mki.fontSize = 200;

			mki.text = "洞窟";
			mki.text2 = "探訪";

			mki.textY = 0.45;
			mki.text2Y = 0.93;

			mki.destFile = "C:/temp/Title.png";

			mki.perform();
		}

		{
			MkKariImage mki = new MkKariImage();

			mki.frameColor = new Color(255, 255, 255);
			mki.backColor = new Color(0, 0, 0, 128);
			mki.foreColor = new Color(255, 255, 255);
			mki.corner = MkKariImage.CORNER_CURVE;
			mki.frameWidth = 10;
			mki.width = 200;
			mki.height = 100;
			mki.fontName = "メイリオ";
			mki.fontStyle = Font.PLAIN;
			mki.fontSize = 40;

			mki.text = "スタート";
			mki.text2 = "";

			mki.textY = 0.65;
			mki.text2Y = -1.0;

			mki.destFile = "C:/temp/TitleBtnStart.png";

			mki.perform();
		}

		// TODO
	}
}
