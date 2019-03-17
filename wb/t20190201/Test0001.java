package wb.t20190201;

import charlotte.tools.BinTools;
import charlotte.tools.StringTools;
import charlotte.tools.ZipTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();
			//test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		for(int size = 0; size <= 100; size++) {
			byte[] data = ZipTools.compress(StringTools.repeat("A", size).getBytes(StringTools.CHARSET_ASCII));

			System.out.println(BinTools.Hex.toString(data));
		}
	}

	private static void test02() throws Exception {
		test02_a(990000);
		test02_a(1000000);
		//test02_a(1000001); // 例外
		//test02_a(2000000); // 例外
	}

	private static void test02_a(int size) throws Exception {
		byte[] data = StringTools.repeat("A", size).getBytes(StringTools.CHARSET_ASCII);
		byte[] c_data = ZipUtils.compress(data);
		byte[] d_data = ZipUtils.decompress(c_data);

		System.out.println("data: " + data.length);
		System.out.println("c_data: " + c_data.length);
		System.out.println("d_data: " + d_data.length);

		if(BinTools.comp_array.compare(data, d_data) != 0) {
			throw null; // bugged !!!
		}
	}
}
