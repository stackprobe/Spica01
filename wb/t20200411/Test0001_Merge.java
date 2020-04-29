package wb.t20200411;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;

public class Test0001_Merge {
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
		String[] onlyA  = readHtmlSimpleImages("C:/wb2/20200411_Instagram/onlyAImages.html");
		String[] onlyB  = readHtmlSimpleImages("C:/wb2/20200411_Instagram/onlyBImages.html");
		String[] bothAB = readHtmlSimpleImages("C:/wb2/20200411_Instagram/sameImages.html");

		if(bothAB.length % 2 != 0) {
			throw null; // souteigai !!!
		}

		List<String[]> aAndAB = new ArrayList<String[]>();
		List<String[]> bOnly = new ArrayList<String[]>();

		for(String a : onlyA) {
			aAndAB.add(new String[] { a, "" });
		}
		for(String b : onlyB) {
			bOnly.add(new String[] { "", b });
		}
		for(int index = 0; index < bothAB.length; index += 2) {
			aAndAB.add(new String[] { bothAB[index + 0], bothAB[index + 1] });
		}

		aAndAB.sort(
				(a, b) -> StringTools.compIgnoreCase.compare(a[0], b[0])
				);
		bOnly.sort(
				(a, b) -> StringTools.compIgnoreCase.compare(a[1], b[1])
				);

		List<String[]> rows = new ArrayList<String[]>();

		ListTools.merge_noSort(aAndAB, bOnly, rows, rows, rows, rows, (a, b) -> StringTools.compIgnoreCase.compare(a[1], b[1]));

		try(CsvFileWriter writer = new CsvFileWriter("C:/temp/Merged.csv")) {
			writer.writeRows(rows);
		}

		// ----

		try(Writer writer = FileTools.openWriter("C:/temp/CopyAOnly.bat", StringTools.CHARSET_SJIS)) {
			writer.write("C:\\Factory\\Tools\\RDMD.exe /RM C:\\123\r\n");

			for(String a : onlyA) {
				String n = FileTools.getFileName(a);

				writer.write(String.format("COPY \"C:\\etc\\Instagram_DL\\%s\" \"C:\\123\\%s\"\r\n", n, n));
			}
			writer.write("PAUSE\r\n");
		}
	}

	private static String[] readHtmlSimpleImages(String htmlFile) throws Exception {
		List<StringTools.Enclosed> encls = StringTools.getAllEnclosed(
				FileTools.readAllText(htmlFile, StringTools.CHARSET_UTF8),
				"<img src=\"",
				"\">"
				);

		List<String> lines = ListTools.select(encls, encl -> encl.inner());

		return lines.toArray(new String[lines.size()]);
	}
}
