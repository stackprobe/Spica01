package violet.labo.module.csvtable.utils.extra.tests;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.SecurityTools;
import violet.labo.module.csvtable.utils.extra.BinFileSorter;

public class BinFileSorterTest {
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
		//test01_a(0); // 0バイトのレコードng
		test01_a(1);
		test01_a(2);
		test01_a(3);
		test01_a(16);
		test01_a(64);
	}

	private static void test01_a(int recordSize) throws Exception {
		test01_b(recordSize, 1, 1);
		test01_b(recordSize, 10, 10);
		test01_b(recordSize, 100, 100);
		test01_b(recordSize, 1000, 30);
		test01_b(recordSize, 10000, 10);
	}

	private static void test01_b(int recordSize, int recordCountLimit, int testcnt) throws Exception {
		System.out.println(recordSize + ", " + recordCountLimit + ", " + testcnt); // test

		while(1 <= testcnt) {
			test01_c(recordSize, recordCountLimit);
			testcnt--;
		}
	}

	private static String MID_FILE = "C:/temp/1.tmp";

	private static void test01_c(int recordSize, int recordCountLimit) throws Exception {
		List<byte[]> records = makeTestVector(recordSize, recordCountLimit);

		FileTools.writeAllBytes(MID_FILE, BinTools.join(records));

		records.sort(BinTools.comp_array);

		try(BinFileSorter sorter = new BinFileSorter(MID_FILE) {
			@Override
			public int capacity() {
				return 1000;
			}
		}
		) {
			sorter.recordSize = recordSize;
			sorter.sort(BinTools.comp_array);
		}

		List<byte[]> records2 = BinTools.divide(FileTools.readAllBytes(MID_FILE), recordSize);

		if(ListTools.comp(records, records2, BinTools.comp_array) != 0) {
			throw null; // bugged !!!
		}
	}

	private static List<byte[]> makeTestVector(int recordSize, int recordCountLimit) {
		int recordCount = SecurityTools.cRandom.getInt(recordCountLimit);
		List<byte[]> records = new ArrayList<byte[]>();

		while(records.size() < recordCount) {
			records.add(SecurityTools.cRandom.getBytes(recordSize));
		}
		return records;
	}
}
