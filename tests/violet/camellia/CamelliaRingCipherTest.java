package tests.violet.camellia;

import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.WorkingDir;
import violet.camellia.CamelliaRingCipher;

public class CamelliaRingCipherTest {
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
		for(int testCount = 1; testCount <= 1000; testCount++) {
			System.out.println("testCount: " + testCount);

			byte[] rawKey = createRawKey();
			byte[] data = createEncryptableData();

			test01_1(rawKey, data);
			test01_2(rawKey, data);
		}
	}

	private static byte[] createRawKey() {
		return SecurityTools.cRandom.getBytes(SecurityTools.cRandom.chooseOne(
				new Integer[] {
						//16 * 1,
						16 * 2,
						16 * 3,
						16 * 4,
						16 * 5,

						//24 * 1,
						24 * 2,
						24 * 3,
						24 * 4,
						24 * 5,

						32 * 1,
						32 * 2,
						32 * 3,
						32 * 4,
						32 * 5,
				}
				));
	}

	private static byte[] createEncryptableData() {
		return SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getRangeInt(0, 2000));
	}

	private static void test01_1(byte[] rawKey, byte[] data) throws Exception {
		CamelliaRingCipher cipher = new CamelliaRingCipher(rawKey);

		byte[] encData = cipher.encrypt(data);

		if(BinTools.comp_array.compare(data, encData) == 0) {
			throw null; // bugged !!!
		}
		byte[] decData = cipher.decrypt(encData);

		if(BinTools.comp_array.compare(data, decData) != 0) {
			throw null; // bugged !!!
		}
	}

	private static void test01_2(byte[] rawKey, byte[] data) throws Exception {
		CamelliaRingCipher cipher = new CamelliaRingCipher(rawKey);

		byte[] encData = cipher.encrypt(data);

		byte[] decData1 = cipher.decrypt(encData);
		byte[] decData2 = decryptByFactory(rawKey, encData);

		if(BinTools.comp_array.compare(decData1, decData2) != 0) {
			throw null; // bugged !!!
		}
	}

	private static byte[] decryptByFactory(byte[] rawKey, byte[] data) throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			String rawKeyFile = wd.makePath();
			String rDataFile = wd.makePath();
			String wDataFile = wd.makePath();

			FileTools.writeAllBytes(rawKeyFile, rawKey);
			FileTools.writeAllBytes(rDataFile, data);

			Runtime.getRuntime().exec(String.format(
					"C:/Factory/Labo/Tools/CamelliaRingCBC.exe /K \"%s\" /R \"%s\" /D /W \"%s\""
					,rawKeyFile
					,rDataFile
					,wDataFile
					))
					.waitFor();

			return FileTools.readAllBytes(wDataFile);
		}
	}
}
