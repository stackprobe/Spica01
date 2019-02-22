package violet.labo.module.csvtable.utils.tests;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.SecurityTools;
import violet.labo.module.csvtable.utils.CsvFileSorter;

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

	private static final String _test01_file = "C:/temp/test01.csv";

	private static void test01() throws Exception {
		List<String[]> rows = new ArrayList<String[]>();

		for(int index = 0; index < 1000; index++) {
			rows.add(new String[] { "" + index, "_" + index, "$$$" + index });
		}
		SecurityTools.cRandom.shuffle(rows);

		try(CsvFileWriter writer = new CsvFileWriter(_test01_file)) {
			writer.writeRows(rows);
		}
		rows = null;

		try(CsvFileSorter sorter = new CsvFileSorter(_test01_file) {
			@Override
			public int getWeight(List<String> row) {
				return 10000;
			}

			@Override
			public int capacity() {
				return 10000 * 30;
			}
		}
		) {
			sorter.sort((a, b) -> Integer.parseInt(a.get(0)) - Integer.parseInt(b.get(0)));
		}
		try(CsvFileReader reader = new CsvFileReader(_test01_file)) {
			rows = reader.readToEnd();
		}
		for(int index = 0; index < 1000; index++) {
			String[] row = rows.get(index);

			if(row[0].equals("" + index) == false) {
				throw null; // bugged !!!
			}
			if(row[1].equals("_" + index) == false) {
				throw null; // bugged !!!
			}
			if(row[2].equals("$$$" + index) == false) {
				throw null; // bugged !!!
			}
		}
	}
}
