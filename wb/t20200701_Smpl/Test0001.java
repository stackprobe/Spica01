package wb.t20200701_Smpl;

import charlotte.tools.CsvFileWriter;
import charlotte.tools.SecurityTools;

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
		makePairFile("C:/temp/Pair.csv");
		makeIdentFile("C:/temp/T-Ident.csv");
		makeIdentFile("C:/temp/F-Ident.csv");
		makeRecordFile("C:/temp/Record-0001.csv");
		makeRecordFile("C:/temp/Record-0002.csv");
		makeRecordFile("C:/temp/Record-0003.csv");
		makeRecordFile("C:/temp/Record-0004.csv");
	}

	private static void makePairFile(String wFile) throws Exception {
		try(CsvFileWriter writer = new CsvFileWriter(wFile)) {
			writer.writeCell("Ident");
			writer.writeCell("T-Status");
			writer.writeCell("F-Status");
			writer.endRow();

			for(int ident = 0; ident < 1000000; ident++) {
				if(SecurityTools.cRandom.getInt(3) == 0) { // 1 of 3
					writer.writeCell(String.format("%06d", ident));

					switch(SecurityTools.cRandom.getInt(3)) {
					case 0:
						writer.writeCell("X-03");
						writer.writeCell("X-03");
						break;

					case 1:
						writer.writeCell("C-01");
						writer.writeCell("");
						break;

					case 2:
						writer.writeCell("");
						writer.writeCell("C-02");
						break;

					default:
						throw null;
					}

					writer.endRow();
				}
			}
		}
	}

	private static void makeIdentFile(String wFile) throws Exception {
		try(CsvFileWriter writer = new CsvFileWriter(wFile)) {
			writer.writeCell("Ident");
			writer.writeCell("LiveFlag");
			writer.endRow();

			for(int ident = 0; ident < 1000000; ident++) {
				if(SecurityTools.cRandom.getInt(3) == 0) { // 1 of 3
					writer.writeCell(String.format("%06d", ident));
					writer.writeCell(SecurityTools.cRandom.chooseOne(new String[] { "Y", "N" }));
					writer.endRow();
				}
			}
		}
	}

	private static void makeRecordFile(String wFile) throws Exception {
		try(CsvFileWriter writer = new CsvFileWriter(wFile)) {
			writer.writeCell("Ident");
			writer.writeCell("CurrentFlag");
			writer.endRow();

			for(int c = 0; c < 1000000; c++) {
				int ident = SecurityTools.cRandom.getInt(1000000);

				writer.writeCell(String.format("%06d", ident));
				writer.writeCell(SecurityTools.cRandom.chooseOne(new String[] { "Y", "N" }));
				writer.endRow();
			}
		}
	}
}
