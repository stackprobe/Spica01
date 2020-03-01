package wb.t20200229_CompImagesDir;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.DoubleTools;
import charlotte.tools.FileTools;
import charlotte.tools.IteratorTools;
import charlotte.tools.ListTools;
import charlotte.tools.RTError;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class CompImagesDir {
	public static void main(String[] args) {
		try {
			Ground.init();
			try {
				main2();
			}
			finally {
				Ground.fnlz();
			}

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void main2() throws Exception {
		// ----

		//main3("C:/etc/Instagram", "C:/etc/Instagram_old");
		//main3("C:/temp/p10/a", "C:/temp/p10/b");
		main3("C:/temp/p30/a", "C:/temp/p30/b");
		//main3("C:/temp/p100/a", "C:/temp/p100/b");
		//main3("C:/temp/p300/a", "C:/temp/p300/b");

		// ----
	}

	private static class ImageFInfo {
		public File f;
		public String hash;
		public Thumbnail thumb;
	}

	private static List<ImageFInfo> _imageFInfosA = new ArrayList<ImageFInfo>();
	private static List<ImageFInfo> _imageFInfosB = new ArrayList<ImageFInfo>();

	private static List<ImageFInfo> _imageFInfosOnlyA;
	private static List<ImageFInfo> _imageFInfosOnlyB;

	private static void main3(String rRootDirA, String rRootDirB) throws Exception {
		File rRootDA = new File(rRootDirA);
		File rRootDB = new File(rRootDirB);

		if(rRootDA.isDirectory() == false) {
			throw new Exception("Bad rRootDirA: " + rRootDirA);
		}
		if(rRootDB.isDirectory() == false) {
			throw new Exception("Bad rRootDirB: " + rRootDirB);
		}
		collectImageFInfos(rRootDA, _imageFInfosA);
		collectImageFInfos(rRootDB, _imageFInfosB);

		for(List<ImageFInfo> infos : ListTools.lot(_imageFInfosA, _imageFInfosB)) {
			infos.sort((a, b) -> RTError.get(() -> {
				int ret = StringTools.comp.compare(a.hash, b.hash);

				if(ret != 0) {
					return ret;
				}
				ret = StringTools.compIgnoreCase.compare(a.f.getCanonicalPath(), b.f.getCanonicalPath());
				return ret;
			}
			));
		}

		{
			List<ImageFInfo> onlyA = new ArrayList<ImageFInfo>();
			List<ImageFInfo> bothA = new ArrayList<ImageFInfo>();
			List<ImageFInfo> bothB = new ArrayList<ImageFInfo>();
			List<ImageFInfo> onlyB = new ArrayList<ImageFInfo>();

			ListTools.merge(
					_imageFInfosA,
					_imageFInfosB,
					onlyA,
					bothA,
					bothB,
					onlyB,
					(a, b) -> StringTools.comp.compare(a.hash, b.hash)
					);

			_imageFInfosOnlyA = onlyA;
			_imageFInfosOnlyB = onlyB;
		}

		for(ImageFInfo i : IteratorTools.join(_imageFInfosOnlyA, _imageFInfosOnlyB)) {
			i.thumb = new Thumbnail(i.f);
		}
		for(List<ImageFInfo> infos : ListTools.lot(_imageFInfosOnlyA, _imageFInfosOnlyB)) {
			infos.sort((a, b) -> RTError.get(() -> {
				int ret = DoubleTools.comp.compare(a.thumb.getBrightness(), b.thumb.getBrightness());

				if(ret != 0) {
					return ret;
				}
				ret = StringTools.compIgnoreCase.compare(a.f.getCanonicalPath(), b.f.getCanonicalPath());
				return ret;
			}
			));
		}

		// test
		/*
		try(CsvFileWriter writer = new CsvFileWriter("C:/temp/a.csv")) {
			for(ImageFInfo ia : _imageFInfosOnlyA) {
				for(ImageFInfo ib : _imageFInfosOnlyB) {
					writer.writeCell(String.format("%.9f", Math.abs(ia.thumb.getBrightness() - ib.thumb.getBrightness())));
				}
				writer.endRow();
			}
		}
		*/

		// test
		/*
		try(CsvFileWriter writer = new CsvFileWriter("C:/temp/b.csv")) {
			for(ImageFInfo ia : _imageFInfosOnlyA) {
				for(ImageFInfo ib : _imageFInfosOnlyB) {
					writer.writeCell(String.format("%.9f", Thumbnail.getDifferent(ia.thumb, ib.thumb)));
				}
				writer.endRow();
			}
		}
		*/

		try(CsvFileWriter writer = new CsvFileWriter("C:/temp/matrix.csv")) {
			for(ImageFInfo ia : _imageFInfosOnlyA) {
				for(ImageFInfo ib : _imageFInfosOnlyB) {
					double d = Math.abs(ia.thumb.getBrightness() - ib.thumb.getBrightness());

					if(d < 0.1) {
						writer.writeCell(String.format("%.9f", Thumbnail.getDifferent(ia.thumb, ib.thumb)));
					}
					else {
						writer.writeCell("");
					}
				}
				writer.endRow();
			}
		}
	}

	private static void collectImageFInfos(File rootD, List<ImageFInfo> dest) {
		Utils.lssFs(rootD, f -> RTError.run(() -> {
			if(isImageF(f)) {
				ImageFInfo i = new ImageFInfo();

				System.out.println("< " + f.getCanonicalPath()); // test

				i.f = f;
				i.hash = BinTools.Hex.toString(SecurityTools.getSHA512File(f.getCanonicalPath()));

				System.out.println("> " + i.hash); // test

				dest.add(i);
			}
		}
		));
	}

	private static boolean isImageF(File f) {
		String ext = FileTools.getExtension(f.getName());

		return
				ext.equalsIgnoreCase(".bmp") ||
				ext.equalsIgnoreCase(".gif") ||
				ext.equalsIgnoreCase(".jpeg") ||
				ext.equalsIgnoreCase(".jpg") ||
				ext.equalsIgnoreCase(".png");
	}
}
