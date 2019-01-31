package tests.charlotte.tools;

import charlotte.tools.Base64;
import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class Base64Test {
	public static void main(String[] args) {
		try {
			test01();
			test_random();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * https://tools.ietf.org/html/rfc4648
	 *
	 */
	private static final String[] TEST_VECTOR = new String[] {
		"", "",
		"f", "Zg==",
		"fo", "Zm8=",
		"foo", "Zm9v",
		"foob", "Zm9vYg==",
		"fooba", "Zm9vYmE=",
		"foobar", "Zm9vYmFy",
	};

	private static void test01() throws Exception {
		for(int index = 0; index < TEST_VECTOR.length; index += 2) {
			byte[] plain = TEST_VECTOR[index].getBytes(StringTools.CHARSET_ASCII);
			String encoded = TEST_VECTOR[index + 1];

			String enc = new Base64().encode(plain);
			byte[] dec = new Base64().decode(encoded);

			if(BinTools.comp_array.compare(plain, dec) != 0) {
				throw null; // bugged !!!
			}
			if(encoded.equals(enc) == false) {
				throw null; // bugged !!!
			}
		}
	}

	private static void test_random() throws Exception {
		for(int c = 0; c < 1000; c++) {
			test_random(test_random_getData(1000));
		}
		for(int c = 0; c < 30; c++) {
			test_random(test_random_getData(1000000));
		}
		for(int c = 0; c < 10; c++) {
			test_random(test_random_getData(8000000));
		}
	}

	private static void test_random(byte[] data) throws Exception {
		Base64 b64 = new Base64();
		//Base64 b64 = new Base64().noPadding();

		long t1 = System.currentTimeMillis();
		String encData = b64.encode(data);
		long t2 = System.currentTimeMillis();
		byte[] decData = b64.decode(encData);
		long t3 = System.currentTimeMillis();

		System.out.println("t1-2: " + (t2 - t1));
		System.out.println("t2-3: " + (t3 - t2));

		System.out.println("data: " + data.length);
		System.out.println("encData: " + encData.length());
		System.out.println("decData: " + decData.length);

		if(BinTools.comp_array.compare(data, decData) != 0) {
			throw null; // bugged !!!
		}
	}

	private static byte[] test_random_getData(int maxSize) throws Exception {
		return SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(maxSize));
	}
}
