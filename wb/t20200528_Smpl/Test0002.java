package wb.t20200528_Smpl;

import java.util.List;

import charlotte.tools.SortedList;
import charlotte.tools.StringTools;

public class Test0002 {
	public static void main(String[] args) {
		try {
			perform();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static class RecordInfo {
		public String entity;
		public String version;
		public String recordId;
		public String tfCode;
	}

	private static SortedList<RecordInfo> _records;

	private static void perform() throws Exception {
		_records = new SortedList<RecordInfo>((a, b) -> {
			int ret = StringTools.comp.compare(a.entity, b.entity);
			if(ret != 0) {
				return ret;
			}

			ret = StringTools.comp.compare(a.version, b.version);
			if(ret != 0) {
				return ret;
			}

			ret = StringTools.comp.compare(a.recordId, b.recordId);
			return ret;
		});

		readRecordFile("Record-0001:entity:version:recordId:tfCode");
		readRecordFile("Record-0002:entity:version:recordId:tfCode");
		readRecordFile("Record-0003:entity:version:recordId:tfCode");
		readRecordFile("Record-0004:entity:version:recordId:tfCode");

		String file = identToCsvFile("Log");

		XTable xt = new XTable();

		try(HTable reader = new HTable(file)) {
			while(reader.read()) {
				String entity = reader.getCell("log-entity");
				String version = reader.getCell("log-version");
				String recordId = reader.getCell("log-recordId");
				String logType = reader.getCell("type");
				String logTime = reader.getCell("time");

				String tfCode;

				{
					RecordInfo ferret = new RecordInfo();

					ferret.entity = entity;
					ferret.version = version;
					ferret.recordId = recordId;

					List<RecordInfo> matchedRecords = _records.getMatch(_records.getFerret(ferret));

					if(matchedRecords.size() == 0) {
						tfCode = "<NULL>";
					}
					else {
						tfCode = matchedRecords.get(0).tfCode;
					}
				}

				System.out.println(String.join(", ", entity, version, recordId, logType, logTime, tfCode)); // test

				// ----

				String logTimeYM = logTime.substring(0, 7);

				xt.increment(logType + "_" + tfCode, logTimeYM);
			}
		}

		xt.writeToCsvFile("C:/temp/t20200528_Test0002_out.csv");
	}

	private static void readRecordFile(String sPrms) throws Exception {
		System.out.println("Loading... " + sPrms); // test

		String[] prms = sPrms.split("[:]");
		int c = 0;

		String ident = prms[c++];
		String entityColNm = prms[c++];
		String versionColNm = prms[c++];
		String recordIdColNm = prms[c++];
		String tfCodeColNm = prms[c++];

		String file = identToCsvFile(ident);

		try(HTable reader = new HTable(file)) {
			while(reader.read()) {
				String entity = reader.getCell(entityColNm);
				String version = reader.getCell(versionColNm);
				String recordId = reader.getCell(recordIdColNm);
				String tfCode = reader.getCell(tfCodeColNm);

				RecordInfo record = new RecordInfo();

				record.entity = entity;
				record.version = version;
				record.recordId = recordId;
				record.tfCode = tfCode;

				_records.add(record);
			}
		}

		System.out.println("Loaded"); // test
	}

	private static String identToCsvFile(String ident) {
		String file = "C:/temp/" + ident + ".csv";
		return file;
	}
}
