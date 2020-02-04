package wb.t20200130_GBCTCollectors.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class Clusterizer {
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
			wr(new byte[] { 0x44 }, 0, 1); // 'D' == Directory

			for(File sf : f.listFiles()) {
				write2(sf);
			}
			wr(new byte[] { 0x00 }, 0, 1);
		}
		else {
			wr(new byte[] { 0x46 }, 0, 1); // 'F' == File
			wr(BinTools.toLongBytes(f.length()), 0, 8);

			try(InputStream reader = new FileInputStream(f)) {
				FileTools.readToEnd(reader, (buff, offset, length) -> wr(buff, offset, length));
			}
		}
	}
}
