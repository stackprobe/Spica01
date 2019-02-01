package wb.t20190201;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import charlotte.tools.ZipTools;

public class ZipUtils {
	public static byte[] compress(byte[] data) throws Exception {
		return ZipTools.compress(data);
	}

	public static byte[] decompress(byte[] data) throws Exception {
		try(
				ByteArrayInputStream reader = new ByteArrayInputStream(data);
				ByteArrayOutputStream mem = new ByteArrayOutputStream();
				LimitedOutputStream writer = new LimitedOutputStream(mem, 1000000L); // 1 MB
				) {
			ZipTools.decompress(reader, writer);
			return mem.toByteArray();
		}
	}
}
