package tests.charlotte.tools;

import charlotte.tools.BinTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class BinToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		for(int c = -1000; c <= 1000; c++) {
			test01_Int(c);
		}
		for(int c = Integer.MIN_VALUE; c <= Integer.MIN_VALUE + 1000; c++) {
			test01_Int(c);
		}
		for(int c = Integer.MAX_VALUE; Integer.MAX_VALUE - 1000 <= c; c--) {
			test01_Int(c);
		}
		for(int c = (int)(Integer.MIN_VALUE * 0.9); c <= (int)(Integer.MAX_VALUE * 0.9); c += SecurityTools.cRandom.getInt(Integer.MAX_VALUE / 1000)) {
			test01_Long(c);
		}

		for(long c = -1000L; c <= 1000L; c++) {
			test01_Long(c);
		}
		for(long c = Long.MIN_VALUE; c <= Long.MIN_VALUE + 1000L; c++) {
			test01_Long(c);
		}
		for(long c = Long.MAX_VALUE; Long.MAX_VALUE - 1000L <= c; c--) {
			test01_Long(c);
		}
		for(long c = Integer.MIN_VALUE * 10L; c <= Integer.MAX_VALUE * 10L; c += SecurityTools.cRandom.getLong(Integer.MAX_VALUE / 1000L)) {
			test01_Long(c);
		}
		for(long c = (long)(Long.MIN_VALUE * 0.9); c <= (long)(Long.MAX_VALUE * 0.9); c += SecurityTools.cRandom.getLong(Long.MAX_VALUE / 1000000L)) {
			test01_Long(c);
		}
	}

	private static void test01_Int(int c) {
		int a = BinTools.toInt(BinTools.toBytes(c));

		//System.out.println(c + " -> " + a); // test

		if(a != c) {
			throw null; // bugged !!!
		}
	}

	private static void test01_Long(long c) {
		long a = BinTools.toLong(BinTools.toLongBytes(c));

		//System.out.println(c + " -> " + a); // test

		if(a != c) {
			throw null; // bugged !!!
		}
	}

	private static void test02() throws Exception
	{
		test02a(new String[] { "ABC" });
		test02a(new String[] { "abcdef", "123456" });
		test02a(new String[] { "\u3044\u308d\u306f", "\u306b\u307b\u3078\u3068", "\u3061\u308a\u306c\u308b\u3092" });
	}

	private static void test02a(String[] strs) throws Exception
	{
		byte[][] src = new byte[strs.length][];

		for(int index = 0; index < strs.length; index++) {
			src[index] = strs[index].getBytes(StringTools.CHARSET_UTF8);
		}
		byte[] mid = BinTools.splittableJoin(src);
		byte[][] dest = BinTools.split(mid).toArray(new byte[0][]);

		for(int index = 0; index < dest.length; index++) {
			System.out.println(new String(dest[index], StringTools.CHARSET_UTF8));
		}
		System.out.println("----");
	}
}
