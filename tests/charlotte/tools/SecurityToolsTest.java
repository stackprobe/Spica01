package tests.charlotte.tools;

import charlotte.tools.BinTools;
import charlotte.tools.RandomUnit;
import charlotte.tools.SecurityTools;

public class SecurityToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			test03();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		try(
				RandomUnit r1 = new RandomUnit(new SecurityTools.CryptographicallySecurePseudoRandomNumberGenerator());
				RandomUnit r2 = new RandomUnit(new SecurityTools.CryptographicallySecurePseudoRandomNumberGenerator());
				RandomUnit r3 = new RandomUnit(new SecurityTools.CryptographicallySecurePseudoRandomNumberGenerator());
				// --
				RandomUnit a1 = new RandomUnit(new SecurityTools.AESRandomNumberGenerator(1));
				RandomUnit a2 = new RandomUnit(new SecurityTools.AESRandomNumberGenerator(2));
				RandomUnit a3 = new RandomUnit(new SecurityTools.AESRandomNumberGenerator(3));
				// --
				RandomUnit a1_1 = new RandomUnit(new SecurityTools.AESRandomNumberGenerator(1));
				RandomUnit a1_2 = new RandomUnit(new SecurityTools.AESRandomNumberGenerator(1));
				RandomUnit a1_3 = new RandomUnit(new SecurityTools.AESRandomNumberGenerator(1));
				) {
			RandomUnit[] rs = new RandomUnit[] {
				r1,
				r2,
				r3,
				// --
				a1,
				a2,
				a3,
				// --
				a1_1,
				a1_2,
				a1_3,
			};

			for (int c = 0; c < 1000; c++) {
				for (int i = 0; i < rs.length; i++) {
					if (1 <= i) {
						System.out.print("\t");
					}
					System.out.print(BinTools.Hex.toString(rs[i].getByte()));
				}
				System.out.println("");
			}

			System.out.println("----");
			System.out.println("xx	xx	xx	e2	a1	36	e2	e2	e2"); // expected final line @ 2019.2.3
			// xx == 不定
		}
	}

	private static void test02() throws Exception {
		try(RandomUnit rand = new RandomUnit(new SecurityTools.AESRandomNumberGenerator(123))) {
			for(int c = 0; c < 100; c++) {
				System.out.println(BinTools.Hex.toString(rand.getBytes(16)));
			}
		}
	}

	private static void test03() throws Exception {
		test03_a(30000); // 30 KB
		test03_a(1000000); // 1 MB
		test03_a(30000000); // 30 MB
	}

	private static void test03_a(int scale) throws Exception {
		byte[] data = SecurityTools.cRandom.getBytes(scale);

		long t1 = System.currentTimeMillis();
		SecurityTools.getMD5(data);
		long t2 = System.currentTimeMillis();
		SecurityTools.getSHA512(data);
		long t3 = System.currentTimeMillis();

		System.out.println("MD5    time: " + (t2 - t1));
		System.out.println("SHA512 time: " + (t3 - t2));
	}
}
