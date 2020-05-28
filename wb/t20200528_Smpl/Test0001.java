package wb.t20200528_Smpl;

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
		writeRecordFile("C:/temp/Record-0001.csv");
		writeRecordFile("C:/temp/Record-0002.csv");
		writeRecordFile("C:/temp/Record-0003.csv");
		writeRecordFile("C:/temp/Record-0004.csv");

		writeLogFile("C:/temp/Log.csv");
	}

	private static void writeLogFile(String file) throws Exception {
		try(CsvFileWriter writer = new CsvFileWriter(file)) {
			writer.writeCell("type");
			writer.writeCell("log-recordId");
			writer.writeCell("log-version");
			writer.writeCell("log-entity");
			writer.writeCell("time");
			writer.endRow();

			for(int c = 0; c < 50000; c++) {
				createRecord();

				writer.writeCell(SecurityTools.makePassword("FX", 2)); // type
				writer.writeCell(_recordId);
				writer.writeCell(_version);
				writer.writeCell(_entity);
				writer.writeCell(createTime());
				writer.endRow();
			}
		}
	}

	private static String createTime() {
		return String.format(
				"%04d-%02d-%02d %02d:%02d%02d.%03d",
				SecurityTools.cRandom.getRangeInt(2015, 2020),
				SecurityTools.cRandom.getRangeInt(1, 12),
				SecurityTools.cRandom.getRangeInt(1, 31),
				SecurityTools.cRandom.getRangeInt(0, 23),
				SecurityTools.cRandom.getRangeInt(0, 59),
				SecurityTools.cRandom.getRangeInt(0, 59),
				SecurityTools.cRandom.getRangeInt(0, 999)
				);
	}

	private static void writeRecordFile(String file) throws Exception {
		try(CsvFileWriter writer = new CsvFileWriter(file)) {
			writer.writeCell("entity");
			writer.writeCell("version");
			writer.writeCell("recordId");
			writer.writeCell("tfCode");
			writer.endRow();

			for(int c = 0; c < 10000; c++) {
				createRecord();

				writer.writeCell(_entity);
				writer.writeCell(_version);
				writer.writeCell(_recordId);
				writer.writeCell(_tfCode);
				writer.endRow();
			}
		}
	}

	private static String _entity;
	private static String _version;
	private static String _recordId;
	private static String _tfCode;

	private static void createRecord() {
		_entity = "e" + SecurityTools.cRandom.getRangeInt(1, 2);
		_version = "v" + SecurityTools.cRandom.getRangeInt(1, 2);
		_recordId = "r" + (SecurityTools.cRandom.getInt(10000) + 10000);
		_tfCode = new String(new char[] { "TF".charAt(SecurityTools.cRandom.getInt(2)) });
	}
}
