package wb.t20200701_Smpl;

import java.util.ArrayList;
import java.util.List;

import wb.t20200528_Smpl.HTable;
import wb.t20200528_Smpl.XTable;

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

	private static class PairInfo {
		public String ident;
		public String status_T;
		public String status_F;
	}

	private static List<PairInfo> _pairs;

	private static List<String> _idents_T;
	private static List<String> _idents_F;

	private static XTable _xt;

	private static void test01() throws Exception {
		_pairs = readPairFile("C:/temp/Pair.csv");

		_idents_T = readIdentFile("C:/temp/T-Ident.csv");
		_idents_F = readIdentFile("C:/temp/F-Ident.csv");

		_xt = new XTable();

		readRecordFile("C:/temp/Record-0001.csv", "Record-0001");
		readRecordFile("C:/temp/Record-0002.csv", "Record-0002");
		readRecordFile("C:/temp/Record-0003.csv", "Record-0003");
		readRecordFile("C:/temp/Record-0004.csv", "Record-0004");

		_xt.writeToCsvFile("C:/temp/t20200701_Test0002_out.csv");
	}

	private static List<PairInfo> readPairFile(String rFile) throws Exception {
		List<PairInfo> dest = new ArrayList<PairInfo>();

		try(HTable reader = new HTable(rFile)) {
			while(reader.read()) {
				String ident = reader.getCell("Ident");
				String status_T = reader.getCell("T-Status");
				String status_F = reader.getCell("F-Status");

				PairInfo pair = new PairInfo();

				pair.ident = ident;
				pair.status_T = status_T;
				pair.status_F = status_F;

				dest.add(pair);
			}
		}
		return dest;
	}

	private static List<String> readIdentFile(String rFile) throws Exception {
		List<String> dest = new ArrayList<String>();

		try(HTable reader = new HTable(rFile)) {
			while(reader.read()) {
				String ident = reader.getCell("Ident");
				String liveFlag = reader.getCell("LiveFlag");

				if("Y".equals(liveFlag)) {
					dest.add(ident);
				}
			}
		}
		return dest;
	}

	private static void readRecordFile(String rFile, String rFileKind) throws Exception {
		try(HTable reader = new HTable(rFile)) {
			while(reader.read()) {
				String ident = reader.getCell("Ident");
				String currentFlag = reader.getCell("CurrentFlag");

				if("Y".equals(currentFlag)) {
					PairInfo pair = findPair(ident);

					boolean existIdent_T = isExistIdent(_idents_T, ident);
					boolean existIdent_F = isExistIdent(_idents_F, ident);

					String xName = rFileKind;
					String yName =
							(pair == null ? "_" : pair.status_T + "_" + pair.status_F) +
							"_" +
							(existIdent_T ? "T" : "") +
							"_" +
							(existIdent_F ? "F" : "");

					_xt.increment(xName, yName);
				}
			}
		}
	}

	private static PairInfo findPair(String ident) {
		for(PairInfo pair : _pairs) {
			if(pair.ident.equals(ident)) {
				return pair;
			}
		}
		return null;
	}

	private static boolean isExistIdent(List<String> idents, String targIdent) {
		for(String ident : idents) {
			if(ident.equals(targIdent)) {
				return true;
			}
		}
		return false;
	}
}
