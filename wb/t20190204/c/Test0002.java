package wb.t20190204.c;

import charlotte.tools.BinTools;

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

	private static void test01() throws Exception {
		try(CipherInfo ci = new CipherInfo()) {
			for(int c = 0; c < 100000; c++) {
				System.out.println(BinTools.Hex.toString(ci.encrypt(new byte[0])));
			}
		}
	}
}
