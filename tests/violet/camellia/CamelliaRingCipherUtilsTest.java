package tests.violet.camellia;

import charlotte.tools.BinTools;
import violet.camellia.CamelliaRingCipherUtils;

public class CamelliaRingCipherUtilsTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		test01_b("ABC");
		test01_b("abc");
		test01_b("abc[x25]");
		test01_b("abc[x25]_");
	}

	private static void test01_b(String passphrase) throws Exception {
		System.out.println(BinTools.Hex.toString(CamelliaRingCipherUtils.generateRawKey(passphrase)) + " <---- " + passphrase);
	}
}
