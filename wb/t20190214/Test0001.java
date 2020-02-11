package wb.t20190214;

import charlotte.tools.JString;
import charlotte.tools.StringTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			//test01();
			test02();
			//test03();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		for(int chr = 0x0000; chr <= 0xffff; chr++) {
			if(JString.JChar.i().contains(chr)) {
				String s = new String(new byte[] { (byte)(chr >> 8), (byte)(chr & 0xff) }, StringTools.CHARSET_SJIS);
				char[] ca = s.toCharArray();

				//System.out.println(ca.length);

				if(ca.length != 1) {
					throw null; // surrogate pair !!?
				}

				System.out.println(String.format("%04x", ca[0] & 0xffff));
			}
		}
	}

	private static String SP_CHR = "	？？".substring(1); // orig: private static String SP_CHR = "	\ud852\udf62".substring(1);

	private static void test02() {
		System.out.println(SP_CHR);
		System.out.println(SP_CHR.length());

		char chr = SP_CHR.charAt(0);
		char chr2 = SP_CHR.charAt(1);

		System.out.println("" + chr);
		System.out.println(String.format("%x", chr & 0xffffffff));
		System.out.println("" + chr2);
		System.out.println(String.format("%x", chr2 & 0xffffffff));
	}

	private static void test03() {
		for(char chr : StringTools.HALF.toCharArray()) {
			System.out.println(String.format("%c --> %04x", chr, chr & 0xffff));
		}
	}
}
