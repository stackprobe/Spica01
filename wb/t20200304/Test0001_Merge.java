package wb.t20200304;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.CSemaphore;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.RTError;
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
		/*
		String[] onlyA  = readHtmlSimpleImages("C:/wb2/20200304_Instagram/onlyAImages.html");
		String[] onlyB  = readHtmlSimpleImages("C:/wb2/20200304_Instagram/onlyBImages.html");
		String[] bothAB = readHtmlSimpleImages("C:/wb2/20200304_Instagram/sameImages.html");
		*/
		String[] onlyA  = readHtmlSimpleImages("C:/wb2/20200306_Instagram/onlyAImages.html");
		String[] onlyB  = readHtmlSimpleImages("C:/wb2/20200306_Instagram/onlyBImages.html");
		String[] bothAB = readHtmlSimpleImages("C:/wb2/20200306_Instagram/sameImages.html");

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

		FileTools.delete("C:/temp/thumbs");
		FileTools.createDir("C:/temp/thumbs");

		{
			List<String> lines = new ArrayList<String>();

			lines.add("<html>");
			lines.add("<head>");
			lines.add("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
			lines.add("</head>");
			lines.add("<body>");
			lines.add("<table>");

			for(String[] row : rows) {
				lines.add("<tr>");
				lines.add("<td>");

				if(row[0].length() != 0) {
					lines.add("<a href=\"" + row[0] + "\"><img src=\"" + getThumb(row[0]) + "\"></a>");
				}
				lines.add("</td>");
				lines.add("<td>");

				if(row[1].length() != 0) {
					lines.add("<a href=\"" + row[1] + "\"><img src=\"" + getThumb(row[1]) + "\"></a>");
				}
				lines.add("</td>");
				lines.add("</tr>");
			}
			lines.add("</table>");
			lines.add("</body>");
			lines.add("</html>");

			FileTools.writeAllLines("C:/temp/Merged.html", lines, StringTools.CHARSET_UTF8);
		}

		for(Thread th : _mkThumbThs) {
			th.join();
		}
		_mkThumbThs.clear();
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

	private static int _thumbCounter = 0;
	private static CSemaphore _mkThumbSemaphore = new CSemaphore(4);
	private static List<Thread> _mkThumbThs = new ArrayList<Thread>();

	private static String getThumb(String imgFile) throws Exception {
		String thumbFile = "C:/temp/thumbs/" + (_thumbCounter++) + ".png";

		System.out.println("< " + imgFile);
		System.out.println("> " + thumbFile);

		_mkThumbSemaphore.enter();

		Process proc = Runtime.getRuntime().exec("C:/app/Kit/ImgTools/ImgTools.exe /rf \"" + imgFile + "\" /wf \"" + thumbFile + "\" /ew 100");

		Thread th = new Thread(() -> RTError.run(() -> {
			proc.waitFor();

			_mkThumbSemaphore.leave();
		}
		));

		th.start();

		if(1 <= _mkThumbThs.size() && _mkThumbThs.get(0).isAlive() == false) {
			_mkThumbThs.remove(0);
		}
		_mkThumbThs.add(th);

		return thumbFile;
	}
}
