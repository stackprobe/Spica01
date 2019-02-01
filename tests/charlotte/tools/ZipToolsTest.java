package tests.charlotte.tools;

import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import charlotte.tools.WorkingDir;
import charlotte.tools.ZipTools;

public class ZipToolsTest {
	public static void main(String[] args) {
		try {
			test01();
			//test_random();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			FileTools.createDir(wd.getPath("root/a"));
			FileTools.writeAllText(wd.getPath("root/a/b.txt"), "123", StringTools.CHARSET_ASCII);
			FileTools.writeAllText(wd.getPath("root/a/c.txt"), "456", StringTools.CHARSET_ASCII);
			FileTools.writeAllText(wd.getPath("root/a/いろは123漢字abc.txt"), "789abcあああああいいいいい", StringTools.CHARSET_UTF8);
			FileTools.writeAllText(wd.getPath("root/d.txt"), "789", StringTools.CHARSET_ASCII);

			ZipTools.pack(wd.getPath("root"), wd.getPath("root.zip"));

			ZipTools.extract(wd.getPath("root.zip"), wd.getPath("root_"));

			// todo ??? check root_

			test01_a(wd.getPath("root.zip"));
		}
	}

	private static void test01_a(String file) throws Exception {
		ZipTools.extract(file, (enter, reader) -> {
			System.out.println("entry_name: " + enter.getName());
		});
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
		long t1 = System.currentTimeMillis();
		byte[] encData = ZipTools.compress(data);
		long t2 = System.currentTimeMillis();
		byte[] decData = ZipTools.decompress(encData);
		long t3 = System.currentTimeMillis();

		System.out.println("t1-2: " + (t2 - t1));
		System.out.println("t2-3: " + (t3 - t2));

		System.out.println("data: " + data.length);
		System.out.println("encData: " + encData.length + ", rate: " + (encData.length * 1.0 / data.length));
		System.out.println("decData: " + decData.length);

		if(BinTools.comp_array.compare(data, decData) != 0) {
			throw null; // bugged !!!
		}
	}

	private static byte[] test_random_getData(int maxSize) throws Exception {
		return SecurityTools.makePassword(StringTools.DECIMAL, SecurityTools.cRandom.getInt(maxSize)).getBytes(StringTools.CHARSET_ASCII);
	}
}
