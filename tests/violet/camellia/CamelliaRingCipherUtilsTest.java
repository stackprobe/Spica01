package tests.violet.camellia;

import charlotte.tools.BinTools;
import charlotte.tools.CharTools;
import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import charlotte.tools.WorkingDir;
import violet.camellia.CamelliaRingCipherUtils;

public class CamelliaRingCipherUtilsTest {
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
		test01_b("ABC");
		test01_b("abc");
		test01_b("abc");
		test01_b("abc[x25]");
		test01_b("abc_[a27]");
		test01_b("abc[$27]_");
		test01_b("abc2[$30]");
		test01_b("abc3[$31]");

		for(int testCount = 0; testCount < 100; testCount++) {
			System.out.println("testCount: " + testCount);

			String passphrase = SecurityTools.makePassword();

			test01_b(passphrase);

			passphrase +=
					"[" +
					SecurityTools.cRandom.chooseOne(CharTools.asList(StringTools.HALF.toCharArray())) +
					SecurityTools.cRandom.getRangeInt(20, 25) +
					//SecurityTools.cRandom.getRangeInt(20, 50) +
					"]";

			test01_b(passphrase);
		}
	}

	private static void test01_b(String passphrase) throws Exception {
		System.out.println("< " + passphrase);

		byte[] rawKey1 = CamelliaRingCipherUtils.generateRawKey(passphrase);
		byte[] rawKey2 = generateRawKeyByFactory(passphrase);

		System.out.println("1> " + BinTools.Hex.toString(rawKey1));
		System.out.println("2> " + BinTools.Hex.toString(rawKey2));

		if(BinTools.comp_array.compare(rawKey1, rawKey2) != 0) {
			throw null; // bugged !!!
		}
	}

	private static byte[] generateRawKeyByFactory(String passphrase) throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			String rFile = wd.makePath();
			String wFile = wd.makePath();

			FileTools.writeAllBytes(rFile, passphrase.getBytes(StringTools.CHARSET_SJIS));

			Runtime.getRuntime().exec(String.format("C:/Factory/Labo/Tools/CipherGenerateRawKey.exe \"%s\" \"%s\"", rFile, wFile)).waitFor();

			return FileTools.readAllBytes(wFile);
		}
	}
}
