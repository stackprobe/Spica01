package violet.labo.module.csvtable.tests;

import java.io.File;
import java.util.Arrays;

import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;
import charlotte.tools.WorkingDir;
import charlotte.tools.Wrapper;
import violet.labo.module.csvtable.HeaderRow;
import violet.labo.module.csvtable.HeaderTable;

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

	private static void typeAll(String dir) throws Exception {
		System.out.println(">");

		for(File f : Wrapper.create(new File(dir).listFiles())
				.accept(w -> Arrays.sort(w, (a, b) -> StringTools.compIgnoreCase.compare(a.getName(), b.getName())))
				.get()) {
			String file = f.getCanonicalPath();

			System.out.println(file);

			for(String line : FileTools.readAllLines(file, StringTools.CHARSET_SJIS)) {
				System.out.println("\t" + line);
			}
		}

		System.out.println("<");
	}

	private static void test01() throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			String file = wd.getPath("file0001.csv");

			try(CsvFileWriter writer = new CsvFileWriter(file)) {
				writer.writeRow("column0001:column0002:column0003".split("[:]"));
			}
			HeaderTable ht = new HeaderTable(file);

			{
				HeaderRow hr = ht.createRow();

				hr.set("column0003", "aaaa");
				hr.set("column0002", "bbbb");
				hr.set("column0001", "cccc");

				ht.addRow(hr);
			}

			{
				HeaderRow hr = ht.createRow();

				hr.set("column0003", "xxx");
				hr.set("column0002", "xxx");
				hr.set("column0001", "ttt");

				ht.addRow(hr);
			}

			{
				HeaderRow hr = ht.createRow();

				hr.set("column0003", "aaaa2");
				hr.set("column0002", "bbbb2");
				hr.set("column0001", "cccc2");

				ht.addRow(hr);
			}

			typeAll(wd.getPath("."));

			ht.update(hr -> {
				if(hr.get("column0001").equals("ttt")) {
					hr.set("column0003", "yyy");
				}
			});

			typeAll(wd.getPath("."));

			ht.update(hr -> {
				if(hr.get("column0002").equals("bbbb2")) {
					hr.delete();
				}
			});

			typeAll(wd.getPath("."));
		}
	}
}
