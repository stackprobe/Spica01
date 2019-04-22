package wb.t20190422;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.LimitedInputStream;
import charlotte.tools.RTError;
import charlotte.tools.SecurityTools;

public class Test0001 {
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
		test01_a("C:/var/A83Map_01.bmp");
	}

	private static void test01_a(String file) throws Exception {
		String file2 = "C:/temp/2.tmp";
		String file3 = "C:/temp/3.tmp";

		FileTools.copyFile(file, file2);

		addHash(file2);
		checkHash(file2);
		unaddHash(file2, file3);

		System.out.println(BinTools.Hex.toString(SecurityTools.getMD5File(file)));
		System.out.println(BinTools.Hex.toString(SecurityTools.getMD5File(file2)));
		System.out.println(BinTools.Hex.toString(SecurityTools.getMD5File(file3)));
	}

	private static void addHash(String file) throws Exception {
		byte[] hash = SecurityTools.getSHA512File(file);

		try(OutputStream writer = new FileOutputStream(file, true)) {
			writer.write(hash);
		}
	}

	private static void checkHash(String file) throws Exception {
		long fileSize = new File(file).length();

		if(fileSize < 64L) {
			throw new RTError();
		}

		try(
				InputStream reader = new FileInputStream(file);
				InputStream reader2 = new LimitedInputStream(reader, fileSize - 64L);
				) {
			byte[] hash = SecurityTools.getSHA512(reader2);
			byte[] hash2 = FileTools.read(reader, 64);

			if(BinTools.comp_array.compare(hash, hash2) != 0) {
				throw new RTError();
			}
		}
	}

	private static void unaddHash(String rFile, String wFile) throws Exception {
		long fileSize = new File(rFile).length();

		if(fileSize < 64L) {
			throw new RTError();
		}

		try(
				InputStream reader = new FileInputStream(rFile);
				InputStream reader2 = new LimitedInputStream(reader, fileSize - 64L);
				OutputStream writer = new FileOutputStream(wFile);
				) {
			FileTools.readToEnd(reader2, writer);
		}
	}
}
