package wb.t20190204.d;

import java.util.Arrays;

import charlotte.tools.BinTools;
import charlotte.tools.FunctionEx;
import charlotte.tools.SecurityTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		for(int c = 0; c < 10000; c++) {
			test01_a(100);
		}
		for(int c = 0; c < 100; c++) {
			test01_a(10000);
		}
		for(int c = 0; c < 10; c++) {
			test01_a(30000000); // 30 MB
		}
	}

	private static void test01_a(int dataScale) throws Exception {
		try(CipherInfo ci = new CipherInfo()) {
			byte[] data = SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(dataScale));

			long t1 = System.currentTimeMillis();
			byte[] encData = ci.addCryptoHash(data);
			long t2 = System.currentTimeMillis();
			byte[] decData = ci.unaddCryptoHash(encData);
			long t3 = System.currentTimeMillis();

			System.out.println("t1-2: " + (t2 - t1));
			System.out.println("t2-3: " + (t3 - t2));

			System.out.println("data: " + data.length);
			System.out.println("encData: " + encData.length);
			System.out.println("decData: " + decData.length);

			if(BinTools.comp_array.compare(data, decData) != 0) {
				throw null; // bugged !!!
			}
		}
	}

	private static void test02() throws Exception {
		for(int c = 0; c < 10000; c++) {
			test02_a(100);
		}
		for(int c = 0; c < 100; c++) {
			test02_a(10000);
		}
	}

	private static void test02_a(int dataScale) throws Exception {
		test02_b(dataScale, data -> {
			int size = SecurityTools.cRandom.getInt(data.length);

			data = Arrays.copyOf(data, size);

			return data;
		});

		test02_b(dataScale, data -> {
			int index = SecurityTools.cRandom.getInt(data.length);
			int bit = SecurityTools.cRandom.getInt(8);

			System.out.println("*1 " + data[index] + " " + index + " " + data.length); // test
			data[index] ^= 1 << bit;
			System.out.println("*2 " + data[index]); // test

			return data;
		});
	}

	private static void test02_b(int dataScale, FunctionEx<byte[], byte[]> falsifier) throws Exception {
		try(CipherInfo ci = new CipherInfo()) {
			byte[] data = SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(dataScale));
			byte[] encData = ci.addCryptoHash(data);

			encData = falsifier.apply(encData);

			boolean erred = false;

			try {
				ci.unaddCryptoHash(encData);
			}
			catch(Throwable e) {
				System.out.println(e.getMessage());
				erred = true;
			}

			if(erred == false) {
				throw null; // bugged !!!
			}
		}
	}
}
