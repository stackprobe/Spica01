package wb.t20200125_GBCTunnels.utils.cbc16.tests;

import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import charlotte.tools.WorkingDir;
import wb.t20200125_GBCTunnels.utils.cbc16.CRC16;

public class CRC16Test {
	public static void main(String[] args) {
		try {
			test01();
			//test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static CRC16 _crc = new CRC16();

	private static void test01() throws Exception {
		test01_b("abc");
		test01_b("ABCDEF");
		test01_b("123456789");
		test01_b("");
	}

	private static void test01_b(String str) throws Exception {
		byte[] data = str.getBytes(StringTools.CHARSET_SJIS);

		System.out.println("[" + str + "] ----> " + String.format("%04x", _crc.compute(data, 0, data.length)));
	}

	private static void test02() throws Exception {
		for(int testCount = 0; testCount < 100; testCount++) {
			System.out.println("testCount: " + testCount);

			test02_b(SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getRangeInt(0, 1000)));
		}
	}

	private static void test02_b(byte[] data) throws Exception {
		System.out.println("< data.length == " + data.length);

		int crc1 = _crc.compute(data, 0, data.length);
		int crc2 = computeCrc16ByFactory(data);

		System.out.println("1> " + crc1);
		System.out.println("2> " + crc2);

		if(crc1 != crc2) {
			throw null; // bugged !!!
		}
	}

	private static int computeCrc16ByFactory(byte[] data) throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			String rFile = wd.makePath();
			String wFile = wd.makePath();

			FileTools.writeAllBytes(rFile, data);

			Runtime.getRuntime().exec(String.format("C:/Factory/Labo/Junk/crc.exe //O \"%s\" /F \"%s\"", wFile, rFile)).waitFor();

			String outLine = FileTools.readAllLines(wFile, StringTools.CHARSET_ASCII).get(0);
			String[] outLnTokens = outLine.split("[ ]");
			int counter = Integer.parseInt(outLnTokens[2], 16);

			return counter;
		}
	}
}
