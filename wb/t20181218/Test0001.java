package wb.t20181218;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import charlotte.tools.DoubleTools;

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

	private static void test01() throws Exception {
		test01("C:/temp/0001", "C:/temp/0002");
	}

	private static void test01(String dir1, String dir2) throws Exception {
		List<PicInfo> infos1 = getPicInfos(dir1);
		List<PicInfo> infos2 = getPicInfos(dir2);

		infos1.sort((a, b) -> DoubleTools.comp.compare(a.grayscale, b.grayscale));
		infos2.sort((a, b) -> DoubleTools.comp.compare(a.grayscale, b.grayscale));

		List<PicInfo> only1 = new ArrayList<PicInfo>();
		List<PicInfo> only2 = new ArrayList<PicInfo>();
		List<PicInfo> both1 = new ArrayList<PicInfo>();
		List<PicInfo> both2 = new ArrayList<PicInfo>();

		boolean keep;

		do {
			keep = false;

			if(1 <= infos1.size()) {
				int index = indexOf(infos2, infos1.get(0));

				if(index == -1) {
					only1.add(infos1.remove(0));
				}
				else {
					both1.add(infos1.remove(0));
					both2.add(infos2.remove(index));
				}
				keep = true;
			}
			if(1 <= infos2.size()) {
				int index = indexOf(infos1, infos2.get(0));

				if(index == -1) {
					only2.add(infos2.remove(0));
				}
				else {
					both1.add(infos1.remove(index));
					both2.add(infos2.remove(0));
				}
				keep = true;
			}
		}
		while(keep);

		printFiles("only1", only1);
		printFiles("only2", only2);
		printFiles("both1", both1);
		printFiles("both2", both2);
	}

	private static void printFiles(String title, List<PicInfo> infos) {
		System.out.println("<" + title + ">");

		for(PicInfo info : infos) {
			System.out.println(info.file);
		}
		System.out.println("</" + title + ">");
		System.out.println("");
	}

	private static int indexOf(List<PicInfo> infos, PicInfo target) {
		for(int index = 0; index < infos.size(); index++) {
			PicInfo info = infos.get(index);

			if(info.thumb.isSameOrAlmostSamePicture(target.thumb)) {
				return index;
			}
		}
		return -1;
	}

	private static List<PicInfo> getPicInfos(String dir) throws Exception {
		List<PicInfo> infos = new ArrayList<PicInfo>();

		for(File f : new File(dir).listFiles()) {
			infos.add(new PicInfo(f.getCanonicalPath()));
		}
		return infos;
	}

	private static class PicInfo {
		public String file;
		public double grayscale;
		public Thumbnail thumb;

		public PicInfo(String file) {
			this.file = file;

			throw null; // TODO set grayscale, thumb
		}
	}

	private static final int THUMB_W = 10;
	private static final int THUMB_H = 10;

	private static class Thumbnail {
		private double[][][] _rgbs;

		public Thumbnail(Image image) {
			initRgbs();

			throw null; // TODO
		}

		private void initRgbs() {
			_rgbs = new double[THUMB_W][][];

			for(int x = 0; x < THUMB_W; x++) {
				_rgbs[x] = new double[THUMB_H][];

				for(int y = 0; y < THUMB_H; y++) {
					_rgbs[x][y] = new double[3];
				}
			}
		}

		public boolean isSameOrAlmostSamePicture(Thumbnail other) {
			throw null; // TODO
		}
	}
}
