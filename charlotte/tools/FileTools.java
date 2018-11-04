package charlotte.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class FileTools {
	public static byte[] readAllBytes(File f) throws Exception {
		long lFileSize = f.length();

		if(Integer.MAX_VALUE < lFileSize) {
			throw new RTError("ファイルが大き過ぎます。" + lFileSize);
		}
		int fileSize = (int)lFileSize;
		byte[] fileData = new byte[fileSize];

		try(FileInputStream reader = new FileInputStream(f)) {
			reader.read(fileData);
		}
		return fileData;
	}

	public static byte[] readAllBytes(String file) throws Exception {
		return readAllBytes(new File(file));
	}

	public static String readAllText(String file, String charset) throws Exception {
		return new String(readAllBytes(file), charset);
	}

	public static String[] readAllLines(String file, String charset) throws Exception {
		String text = readAllText(file, charset);

		text = text.replace("\r", "");

		List<String> lines = ArrayTools.toList(StringTools.tokenize(text, "\n"));

		if(1 <= lines.size() && lines.get(lines.size() - 1).length() == 0) {
			lines.remove(lines.size() - 1);
		}
		return lines.toArray(new String[lines.size()]);
	}
}
