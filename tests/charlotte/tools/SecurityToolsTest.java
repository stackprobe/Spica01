package tests.charlotte.tools;

import charlotte.tools.BinTools;
import charlotte.tools.RandomUnit;
import charlotte.tools.SecurityTools;

public class SecurityToolsTest {
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

	private static void test01() throws Exception {
		try(
				RandomUnit r1 = new RandomUnit(new SecurityTools.CSPRandomNumberGenerator());
				RandomUnit r2 = new RandomUnit(new SecurityTools.CSPRandomNumberGenerator());
				RandomUnit r3 = new RandomUnit(new SecurityTools.CSPRandomNumberGenerator());
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
}
