package wb.t20191206_httpserverfwdemo.module.csvtable.tests;

import java.io.File;
import java.util.Arrays;

import charlotte.tools.StringTools;
import wb.t20191206_httpserverfwdemo.module.csvtable.CsvFileView;

public class Test0002 {
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
		File[] fs = new File("C:/var2/res/db-export").listFiles();

		Arrays.sort(fs, (a, b) -> (int)b.length() - (int)a.length()); // 大きい順

		for(File f : fs) {
			//if(100000000L < f.length()) {
			//if(60000000L < f.length()) {
			if(40000000L < f.length()) {
				continue;
			}

			String file = f.getCanonicalPath();
			CsvFileView view = new CsvFileView(file);

			int rowcnt = view.size();

			view.filter(row -> true);
			view.sort(StringTools.comp_array);
			view.range(0, 1);

			int colcnt = view.get().get(0).length;

			System.out.println(file + ", " + rowcnt + ", " + colcnt);

			for(int colidx = 0; colidx < colcnt; colidx++) {
				int f_colidx = colidx;
				long t = System.currentTimeMillis();
				view.filter(row -> true);
				long t2 = System.currentTimeMillis();
				view.sort((a, b) -> StringTools.comp.compare(a[f_colidx], b[f_colidx]));
				long t3 = System.currentTimeMillis();
				view.range(0, 1000);
				long e = System.currentTimeMillis();

				System.out.println(colidx + " -> " + (e - t) + " (" + (t2 - t) + ", " + (t3 - t2) + ", " + (e - t3) + ")");
			}
			view.close();
		}
	}
}
