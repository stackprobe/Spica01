package wb.t20181115;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

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

	private static void test01() {
		for(int c = 0; c < 3000; c++) {
			byte[] src = SecurityTools.cRandom.getBytes(SecurityTools.cRandom.getInt(1000));
			String mid = Base64B.toString(src);
			byte[] ans = Base64B.toBytes(mid);

			System.out.println(src.length + ", " + mid.length() + ", " + ans.length); // test

			if(BinTools.comp_array.compare(src, ans) != 0) {
				throw null; // bugged !!!
			}
		}
	}

	private static void test02() {
		System.out.println("go.1");

		byte[] src = SecurityTools.cRandom.getBytes(100000000); // 100 MB

		System.out.println("go.2");

		long t1 = System.currentTimeMillis();
		String mid = Base64B.toString(src);
		long t2 = System.currentTimeMillis();
		byte[] ans = Base64B.toBytes(mid);
		long t3 = System.currentTimeMillis();

		System.out.println(src.length + ", " + mid.length() + ", " + ans.length); // test

		System.out.println("t1: " + (t2 - t1));
		System.out.println("t2: " + (t3 - t2));
	}

	public static class Base64B {
		private static final String ALPHABET = StringTools.ALPHA + StringTools.alpha + StringTools.DECIMAL + "()";

		public static String toString(byte[] src) {
			StringBuffer buff = new StringBuffer();
			int value = 0;

			for(int index = 0; index < src.length; index++) {
				if(index % 3 == 0) {
					buff.append(ALPHABET.charAt(value));
					value = 0;
				}
				value |= (src[index] & 0xff) << ((index % 3) * 2);
				buff.append(ALPHABET.charAt(value & 0x3f));
				value >>= 6;
			}
			buff.append(ALPHABET.charAt(value));

			return buff.substring(1);
		}

		public static byte[] toBytes(String src) {
			List<Byte> buff = new ArrayList<Byte>();
			int value = 0;

			for(int index = 0; index < src.length(); index++) {
				if(index % 4 == 0) {
					value = ALPHABET.indexOf(src.charAt(index++));
				}
				value |= ALPHABET.indexOf(src.charAt(index)) << (8 - (index % 4) * 2);
				buff.add((byte)(value & 0xff));
				value >>= 8;
			}
			return BinTools.toArray(buff);
		}
	}

	public static class Base62B {
		// ???
	}
}
