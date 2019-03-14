package violet.labo.module.csvdb.tests;

import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.LongTools;
import charlotte.tools.SecurityTools;
import violet.labo.module.csvdb.CsvDB;

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
		CsvDB db = new CsvDB("C:/temp/CsvDB_Test");

		System.out.println("*0.1");

		db.getFile("Table").delete(row -> true); // truncate

		System.out.println("*0.2");

		for(int rowidx = 0; rowidx < 10000; rowidx++) {
			db.getFile("Table").add(new String[] {
					SecurityTools.makePassword(),
					SecurityTools.makePassword("ABCabc", 6),
					"" + SecurityTools.cRandom.getInt(IntTools.IMAX),
					"" + SecurityTools.cRandom.getLong(LongTools.IMAX_64),
			});
		}

		System.out.println("*1");
		db.getFile("Table").select().view(0, 1, false, false, row -> true, 0, 30, row -> printRow(row));
		System.out.println("*2.1");
		db.getFile("Table").select().view(1, 1, false, false, row -> true, 0, 30, row -> printRow(row));
		System.out.println("*2.2");
		db.getFile("Table").select().view(1, 1, true, false, row -> true, 0, 30, row -> printRow(row));
		System.out.println("*3");
		db.getFile("Table").select().view(2, 1, false, true, row -> true, 0, 30, row -> printRow(row));
		System.out.println("*4.1");
		db.getFile("Table").select().view(3, 1, false, true, row -> true, 0, 30, row -> printRow(row));
		System.out.println("*4.2.1");
		db.getFile("Table").select().view(3, -1, false, true, row -> true, 0, 30, row -> printRow(row));
		System.out.println("*4.2.2");
		db.getFile("Table").select().view(3, -1, false, true, row -> true, 30, 60, row -> printRow(row));
		System.out.println("*4.2.3");
		db.getFile("Table").select().view(3, -1, false, true, row -> true, 60, 90, row -> printRow(row));
		System.out.println("*5");
	}

	private static void printRow(List<String> row) {
		for(String cell : row) {
			System.out.print("\t");
			System.out.print(cell);
		}
		System.out.println();
	}
}
