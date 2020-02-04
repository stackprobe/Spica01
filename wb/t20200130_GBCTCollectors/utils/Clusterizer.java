package wb.t20200130_GBCTCollectors.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class Clusterizer {
	private static final byte FLAG_DIR  = 0x44;
	private static final byte FLAG_FILE = 0x46;
	private static final byte FLAG_INFO = 0x49;
	private static final byte FLAG_ENDDIR = 0x00;

	private static FileTools.IWriter _checkSumCounter;
	private static FileTools.IWriter _writer;

	private static void wr(byte[] buff, int offset, int length) throws Exception {
		_checkSumCounter.write(buff, offset, length);
		_writer.write(buff, offset, length);
	}

	private static void wr(byte[] buff, int offset) throws Exception {
		wr(buff, offset, buff.length - offset);
	}

	private static void wr(byte[] buff) throws Exception {
		wr(buff, 0);
	}

	public static void write(File f, FileTools.IWriter writer) throws Exception {
		_writer = writer;

		byte[] checkSum = SecurityTools.getMD5(checkSumCounter -> {
			_checkSumCounter = checkSumCounter;

			RTError.run(() -> write2(f));

			_checkSumCounter = null;
		});

		_writer.write(checkSum, 0, checkSum.length);
		_writer = null;
	}

	private static void write2(File f) throws Exception {
		wr(f.getName().getBytes(StringTools.CHARSET_SJIS));

		if(f.isDirectory()) {
			wr(new byte[] { FLAG_DIR }, 0, 1);

			for(File sf : f.listFiles()) {
				write2(sf);
			}
			wr(new byte[] { FLAG_ENDDIR }, 0, 1);
		}
		else {
			wr(new byte[] { FLAG_INFO }, 0, 1);
			wr(new byte[] { (byte)0x80 }, 0, 1); // ATTR_ARCH only

			{
				BasicFileAttributes bfa = Files.readAttributes(f.toPath(), BasicFileAttributes.class);

				wr(BinTools.toLongBytes(bfa.creationTime().toMillis() / 1000L));
				wr(BinTools.toLongBytes(bfa.lastAccessTime().toMillis() / 1000L));
				wr(BinTools.toLongBytes(bfa.lastModifiedTime().toMillis() / 1000L));
			}

			wr(new byte[] { FLAG_FILE }, 0, 1);
			wr(BinTools.toLongBytes(f.length()), 0, 8);

			try(InputStream reader = new FileInputStream(f)) {
				FileTools.readToEnd(reader, (buff, offset, length) -> wr(buff, offset, length), new byte[64000]);
			}
		}
	}
}
