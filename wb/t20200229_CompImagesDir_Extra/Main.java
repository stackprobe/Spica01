package wb.t20200229_CompImagesDir_Extra;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.HandleDam;
import charlotte.tools.StringTools;

public class Main {
	public static void main(String[] args) {
		try {
			main2();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void main2() throws Exception {
		htmlFileToCsvFile(
				"C:/temp/onlyAImages.html",
				"C:/temp/onlyAImages.csv"
				);
		htmlFileToCsvFile(
				"C:/temp/onlyBImages.html",
				"C:/temp/onlyBImages.csv"
				);
		htmlFileToCsvFile(
				"C:/temp/sameImages.html",
				"C:/temp/sameImages.csv"
				);
		htmlFileToCsvFile(
				"C:/temp/completelySameImages.html",
				"C:/temp/completelySameImages.csv"
				);

		csvFileToSimpleText(
				"C:/temp/onlyAImages.csv",
				1,
				"C:/temp/onlyAImages.txt"
				);
		csvFileToSimpleText(
				"C:/temp/onlyBImages.csv",
				1,
				"C:/temp/onlyBImages.txt"
				);
		csvFileToSimpleText(
				"C:/temp/sameImages.csv",
				2,
				"C:/temp/sameImages_AImages.txt",
				"C:/temp/sameImages_BImages.txt"
				);
		csvFileToSimpleText(
				"C:/temp/completelySameImages.csv",
				2,
				"C:/temp/completelySameImages_AImages.txt",
				"C:/temp/completelySameImages_BImages.txt"
				);
	}

	private static void htmlFileToCsvFile(String rFile, String wFile) throws Exception {
		try(
				Reader reader = FileTools.openReader(rFile, StringTools.CHARSET_UTF8);
				CsvFileWriter writer = new CsvFileWriter(wFile);
				) {
			for(; ; ) {
				String line = FileTools.readLine(reader);

				if(line == null) {
					break;
				}
				if("</tr>".equals(line)) {
					writer.endRow();
				}
				else {
					StringTools.Enclosed encl = StringTools.getEnclosed(line, "<img src=\"", "\">");

					if(encl != null) {
						writer.writeCell(encl.inner());
					}
				}
			}
		}
	}

	private static void csvFileToSimpleText(String rFile, int colcnt, String... wFiles) throws Exception {
		HandleDam.section(hDam -> {
			CsvFileReader reader;
			Writer[] writers = new Writer[colcnt];

			hDam.add(reader  = new CsvFileReader(rFile));

			for(int colidx = 0; colidx < colcnt; colidx++) {
				hDam.add(writers[colidx] = FileTools.openWriter(wFiles[colidx], StringTools.CHARSET_SJIS));
			}
			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				if(row.size() != colcnt) {
					throw null; // souteigai !!!
				}
				for(int colidx = 0; colidx < colcnt; colidx++) {
					writers[colidx].write(row.get(colidx));
					writers[colidx].write('\n');
				}
			}
		});
	}
}
