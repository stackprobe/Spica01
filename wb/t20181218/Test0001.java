package wb.t20181218;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import charlotte.tools.CsvFileWriter;
import charlotte.tools.DoubleTools;
import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;

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
		DirInfo dirInfo1 = new DirInfo(dir1);
		DirInfo dirInfo2 = new DirInfo(dir2);

		dirInfo1.load();
		dirInfo2.load();

		int dirInfoSizeDiff = Math.abs(dirInfo1.size() - dirInfo2.size());

		String[][] rows = new String[dirInfo1.size()][];

		for(int index1 = 0; index1 < dirInfo1.size(); index1++) {
			String[] row = new String[dirInfo2.size()];

			for(int index2 = 0; index2 < dirInfo2.size(); index2++) {
				String cell;

				if(dirInfoSizeDiff + 10 < Math.abs(index1 - index2)) {
					cell = "none";
				}
				else {
					cell = String.format("%.9f", dirInfo1.get(index1).difference(dirInfo2.get(index2)));
				}
				row[index2] = cell;

				System.out.println(index1 + ", " + index2 + " -> " + cell); // test
			}
			rows[index1] = row;
		}

		try(CsvFileWriter writer = new CsvFileWriter("C:/temp/matrix.csv")) {
			writer.writeRows(rows);
		}

		while(
				dirInfo1.pairing(dirInfo2) &&
				dirInfo2.pairing(dirInfo1)
				) {
			// noop
		}
		dirInfo1.postPairing();
		dirInfo2.postPairing();

		writeToFile("C:/temp/paired_0001.txt", dirInfo1.pairedPicInfos);
		writeToFile("C:/temp/paired_0002.txt", dirInfo2.pairedPicInfos);
		writeToFile("C:/temp/unpaired_0001.txt", dirInfo1.unpairedPicInfos);
		writeToFile("C:/temp/unpaired_0002.txt", dirInfo2.unpairedPicInfos);
	}

	private static void writeToFile(String file, List<PictureInfo> picInfos) throws Exception {
		FileTools.writeAllLines(
				file,
				ListTools.select(picInfos, picInfo -> picInfo.getFile()),
				StringTools.CHARSET_SJIS
				);
	}

	private static class DirInfo {
		private String _dir;

		public DirInfo(String dir) {
			_dir = dir;
		}

		private List<PictureInfo> _picInfos = new ArrayList<PictureInfo>();

		public void load() throws Exception {
			for(File f : new File(_dir).listFiles()) {
				String file = f.getCanonicalPath();
				String ext = FileTools.getExtension(file);

				if(
						ext.equalsIgnoreCase(".bmp") ||
						ext.equalsIgnoreCase(".gif") ||
						ext.equalsIgnoreCase(".jpeg") ||
						ext.equalsIgnoreCase(".jpg") ||
						ext.equalsIgnoreCase(".png")
						) {
					PictureInfo picInfo = new PictureInfo(file);

					_picInfos.add(picInfo);
				}
			}

			_picInfos.sort((a, b) -> DoubleTools.comp.compare(a.grayscale() , b.grayscale()));
		}

		public int size() {
			return _picInfos.size();
		}

		public PictureInfo get(int index) {
			return _picInfos.get(index);
		}

		public PictureInfo remove(int index) {
			return _picInfos.remove(index);
		}

		public List<PictureInfo> direct() {
			return _picInfos;
		}

		public List<PictureInfo> pairedPicInfos = new ArrayList<PictureInfo>();
		public List<PictureInfo> unpairedPicInfos = new ArrayList<PictureInfo>();

		public boolean pairing(DirInfo other) {
			if(size() == 0 || other.size() == 0) {
				return false;
			}
			PictureInfo picInfo = remove(0);

			int index = ListTools.indexOf(
					other.direct(),
					otherPicInfo -> picInfo.isSameOrAlmostSamePicture(otherPicInfo)
					);

			if(index != -1) {
				pairedPicInfos.add(picInfo);
				other.pairedPicInfos.add(other.remove(index));
			}
			else {
				unpairedPicInfos.add(picInfo);
			}
			return true;
		}

		public void postPairing() {
			for(PictureInfo picInfo : _picInfos) {
				unpairedPicInfos.add(picInfo);
			}
		}
	}
}
