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
			//test01();
			//test_random();
			test02();

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

	private static void test02() throws Exception {
		test02_a(BinTools.EMPTY, 0, false);
		test02_a(BinTools.EMPTY, 1, false);

		for(int c = 0; c < 10; c++) {
			byte[] src = StringTools.repeat("A", SecurityTools.cRandom.getInt(1, 1000000)).getBytes(StringTools.CHARSET_ASCII);

			test02_a(src, 0, true);
			test02_a(src, src.length - 1, true);
			test02_a(src, src.length, false);
			test02_a(src, src.length + 1, false);
		}

		for(int c = 0; c < 100; c++) {
			byte[] src = StringTools.repeat("A", SecurityTools.cRandom.getInt(0, 1000000)).getBytes(StringTools.CHARSET_ASCII);
			int limit = SecurityTools.cRandom.getInt(0, 1000000);
			boolean willError = limit < src.length;

			test02_a(src, limit, willError);
		}
	}

	private static void test02_a(byte[] src, int limit, boolean willError) throws Exception {
		System.out.println(src.length + ", " + limit + ", " + willError); // test

		test02_a_mem(src, limit, willError);
		test02_a_file(src, limit, willError);
	}

	private static void test02_a_mem(byte[] src, int limit, boolean willError) throws Exception {
		byte[] mid = ZipTools.compress(src);
		byte[] dest = null;
		boolean errorOccurred = false;

		try {
			dest = ZipTools.decompress(mid, limit);
		}
		catch(Throwable e) {
			errorOccurred = true;
		}

		if(errorOccurred != willError) {
			throw null; // bugged !!!
		}
		if(errorOccurred == false && BinTools.comp_array.compare(src, dest) != 0) {
			throw null; // bugged !!!
		}
	}

	private static void test02_a_file(byte[] src, int limit, boolean willError) throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			String srcFile = wd.makePath();
			String midFile = wd.makePath();
			String destFile = wd.makePath();
			boolean errorOccurred = false;

			FileTools.writeAllBytes(srcFile, src);

			ZipTools.compress(srcFile, midFile);

			try {
				ZipTools.decompress(midFile, destFile, limit);
			}
			catch(Throwable e) {
				errorOccurred = true;
			}

			if(errorOccurred != willError) {
				throw null; // bugged !!!
			}
			if(errorOccurred == false && BinTools.compFile(srcFile, destFile) != 0) {
				throw null; // bugged !!!
			}
		}
	}
}
