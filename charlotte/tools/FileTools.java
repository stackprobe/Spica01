package charlotte.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.List;

public class FileTools {
	public static void delete(String path) throws Exception {
		if(StringTools.isNullOrEmpty(path)) {
			throw new RTError("Cannot delete path null or empty");
		}
		if(new File(path).exists()) {
			for(int c = 1; ; c++) {
				try {
					deleteFileOrDirectory(path);
				}
				catch(Throwable e) {
					e.printStackTrace(System.out);
				}
				if(new File(path).exists() == false) {
					break;
				}
				if(10 < c) {
					throw new RTError("Cannot delete path: " + path);
				}
				System.out.println("Retry delete path: " + path);
				Thread.sleep(c * 100);
			}
		}
	}

	private static void deleteFileOrDirectory(String path) throws Exception {
		{
			File d = new File(path);

			if(d.isDirectory()) {
				for(File subF : d.listFiles()) {
					deleteFileOrDirectory(subF.getCanonicalPath());
				}
			}
		}

		if(new File(path).delete() == false) {
			throw new RTError("Failed delete path: " + path);
		}
	}

	public static void createDir(String dir) throws Exception {
		if(StringTools.isNullOrEmpty(dir)) {
			throw new RTError("Cannot create dir null or empty");
		}
		for(int c = 1; ; c++) {
			try {
				new File(dir).mkdirs(); // dir が存在する時は何もしない。 // orig: new File(dir).mkdirs(); // dir \u304c\u5b58\u5728\u3059\u308b\u6642\u306f\u4f55\u3082\u3057\u306a\u3044\u3002
			}
			catch(Throwable e) {
				e.printStackTrace(System.out);
			}
			if(new File(dir).isDirectory()) {
				break;
			}
			if(10 < c) {
				throw new RTError("Cannot creaet dir: " + dir);
			}
			System.out.println("Retry create dir: " + dir);
			Thread.sleep(c * 100);
		}
	}

	public static void cleanupDir(String dir) throws Exception {
		for(File f : new File(dir).listFiles()) {
			delete(f.getCanonicalPath());
		}
	}

	public static void copyDir(String rDir, String wDir) throws Exception {
		createDir(wDir);

		for(File f : new File(rDir).listFiles()) {
			String rPath = combine(rDir, f.getName());
			String wPath = combine(wDir, f.getName());

			if(new File(rPath).isDirectory()) {
				copyDir(rPath, wPath);
			}
			else {
				copyFile(rPath, wPath);
			}
		}
	}

	public static void copyFile(String rFile, String wFile) throws Exception {
		try(
				FileInputStream reader = new FileInputStream(rFile);
				FileOutputStream writer = new FileOutputStream(wFile);
				) {
			readToEnd(reader, writer);
		}
	}

	public static void moveDir(String rDir, String wDir) throws Exception {
		createDir(wDir);

		for(File f : new File(rDir).listFiles()) {
			String rPath = combine(rDir, f.getName());
			String wPath = combine(wDir, f.getName());

			if(new File(rPath).isDirectory()) {
				moveDir(rPath, wPath);
			}
			else {
				moveFile(rPath, wPath);
			}
		}
	}

	public static void moveFile(String rFile, String wFile) throws Exception {
		for(int c = 1; ; c++) {
			try {
				new File(rFile).renameTo(new File(wFile));

				if(new File(rFile).exists() == false && new File(wFile).isFile()) {
					break;
				}
			}
			catch(Throwable e) {
				e.printStackTrace(System.out);
			}
			if(10 < c) {
				throw new RTError("Cannot move file: " + rFile + " -> " + wFile);
			}
			System.out.println("Retry move file: " + rFile + " -> " + wFile);

			Thread.sleep(c * 100);
		}
	}

	public static String combine(String path1, String path2) {
		String path;

		if(path1.isEmpty()) {
			path = path2;
		}
		else if(path2.isEmpty()) {
			path = path1;
		}
		else {
			path = path1 + "/" + path2;
		}

		boolean windowsNetworkPath = path.startsWith("\\\\");

		if(windowsNetworkPath) {
			path = path.substring(1);
		}

		path = path.replace("\\", "/");
		path = StringTools.replaceLoop(path, "//", "/");

		if(windowsNetworkPath) {
			path = path.replace("/", "\\");
			path = "\\" + path;
		}
		return path;
	}

	public static String changeRoot(String path, String root, String rootNew) {
		return putSlash(rootNew) + eraseRoot(path, root);
	}

	public static String eraseRoot(String path, String root) {
		root = putSlash(root);

		if(StringTools.startsWithIgnoreCase(path.replace('\\', '/'), root) == false) {
			throw new RTError("Bad path hierarchy: " + root + " -> " + path);
		}
		return path.substring(root.length());
	}

	public static String putSlash(String path) {
		path = path.replace('\\', '/');

		if(path.endsWith("/") == false) {
			path += '/';
		}
		return path;
	}

	/*
	public static String makeFullPath(String path) throws Exception {
		return new File(path).getCanonicalPath();
	}
	*/

	/*
	public static String toFullPath(String path) throws Exception {
		return new File(path).getCanonicalPath();
	}
	*/

	public static byte[] readAllBytes(File f) throws Exception {
		long lFileSize = f.length();

		if(Integer.MAX_VALUE < lFileSize) {
			throw new RTError("Too large lFileSize: " + lFileSize);
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

	public static List<String> readAllLines(String file, String charset) throws Exception {
		return textToLines(readAllText(file, charset));
	}

	/**
	 *
	 * @param url AAA.class.getResource("res/BBB.dat")
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static List<String> readAllLines(URL url, String charset) throws Exception {
		return textToLines(readAllText(url, charset));
	}

	/**
	 *
	 * @param url AAA.class.getResource("res/BBB.dat")
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String readAllText(URL url, String charset) throws Exception {
		return new String(readToEnd(url), charset);
	}

	/**
	 *
	 * @param url AAA.class.getResource("res/BBB.dat")
	 * @return
	 * @throws Exception
	 */
	public static byte[] readToEnd(URL url) throws Exception {
		return readToEnd(url.openStream());
	}

	public static byte[] readToEnd(InputStream reader) throws Exception {
		try(ByteArrayOutputStream mem = new ByteArrayOutputStream()) {
			readToEnd(reader, mem);
			return mem.toByteArray();
		}
	}

	public static void readToEnd(InputStream reader, OutputStream writer) throws Exception {
		readToEnd(reader, (buff, offset, length) -> writer.write(buff, offset, length));
	}

	public static void readToEnd(InputStream reader, IWriter writer) throws Exception {
		readToEnd(reader, writer, new byte[4 * 1024 * 1024]);
	}

	public static void readToEnd(InputStream reader, IWriter writer, byte[] buff) throws Exception {
		for(; ; ) {
			int readSize = reader.read(buff);

			//System.out.println("readSize: " + readSize); // test

			if(readSize == -1) {
				break;
			}
			if(readSize <= 0 || buff.length < readSize) {
				throw new RTError("Bad readSize, buff.length: " + readSize + ", " + buff.length);
			}
			writer.write(buff, 0, readSize);
		}
	}

	public interface IWriter {
		void write(byte[] buff, int offset, int length) throws Exception;
	}

	public static void writeAllBytes(String file, byte[] fileData, boolean append) throws Exception {
		try(FileOutputStream writer = new FileOutputStream(file, append)) {
			writer.write(fileData);
		}
	}

	public static void writeAllBytes(String file, byte[] fileData) throws Exception {
		writeAllBytes(file, fileData, false);
	}

	public static void writeAllText(String file, String text, String charset) throws Exception {
		writeAllBytes(file, text.getBytes(charset));
	}

	public static void writeAllLines(String file, String[] lines, String charset) throws Exception {
		writeAllLines(file, IArrays.asList(lines), charset);
	}

	public static void writeAllLines(String file, List<String> lines, String charset) throws Exception {
		writeAllText(file, linesToText(lines), charset);
	}

	public static void appendAllBytes(String file, byte[] fileData) throws Exception {
		writeAllBytes(file, fileData, true);
	}

	public static void appendAllText(String file, String text, String charset) throws Exception {
		appendAllBytes(file, text.getBytes(charset));
	}

	public static void appendAllLines(String file, String[] lines, String charset) throws Exception {
		appendAllLines(file, IArrays.asList(lines), charset);
	}

	public static void appendAllLines(String file, List<String> lines, String charset) throws Exception {
		appendAllText(file, linesToText(lines), charset);
	}

	public static String linesToText(String[] lines) {
		return linesToText(IArrays.asList(lines));
	}

	public static String linesToText(List<String> lines) {
		return lines.size() == 0 ? "" : String.join("\r\n", lines) + "\r\n";
	}

	public static List<String> textToLines(String text) {
		text = text.replace("\r", "");

		List<String> lines = StringTools.tokenize(text, "\n");

		if(1 <= lines.size() && lines.get(lines.size() - 1).isEmpty()) {
			lines.remove(lines.size() - 1);
		}
		return lines;
	}

	public static int lastIndexOfPathDelimiter(String path) {
		return Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
	}

	public static int indexOfExtension(String path) {
		int index = path.lastIndexOf('.');

		if(index <= lastIndexOfPathDelimiter(path) + 1) { // .gitignore などを除外する。 // orig: if(index <= lastIndexOfPathDelimiter(path) + 1) { // .gitignore \u306a\u3069\u3092\u9664\u5916\u3059\u308b\u3002
			index = path.length();
		}
		return index;
	}

	public static String getDirectoryName(String path) {
		String dir = path.substring(0, lastIndexOfPathDelimiter(path));

		if(dir.length() == 2 && dir.charAt(1) == ':') {
			dir += "/";
		}
		return dir;
	}

	public static String getFileName(String path) {
		return path.substring(lastIndexOfPathDelimiter(path) + 1);
	}

	public static String getFileNameWithoutExtension(String path) {
		return path.substring(lastIndexOfPathDelimiter(path) + 1, indexOfExtension(path));
	}

	public static String getExtension(String path) {
		return path.substring(indexOfExtension(path));
	}

	public static String readLine(Reader reader) throws Exception {
		StringBuffer buff = new StringBuffer();

		for(; ; ) {
			int chr = reader.read();

			if(chr == -1) {
				if(buff.length() == 0) {
					return null;
				}
				break;
			}
			if(chr == '\r') {
				continue;
			}
			if(chr == '\n') {
				break;
			}
			buff.append((char)chr);
		}
		return buff.toString();
	}

	public static Reader openReader(String file, String charset) throws Exception {
		return HandleDam.transaction_get(hDam -> {
			return hDam.add(new BufferedReader(
					hDam.add(new InputStreamReader(
							hDam.add(new FileInputStream(
									file
									)),
							charset
							))
					));
		});
	}

	public static Writer openWriter(String file, String charset) throws Exception {
		return openWriter(file, charset, false);
	}

	public static Writer openWriter(String file, String charset, boolean append) throws Exception {
		return HandleDam.transaction_get(hDam -> {
			return hDam.add(new OutputStreamWriter(
					hDam.add(new FileTools.CrLfStream(
							hDam.add(new BufferedOutputStream(
									hDam.add(new FileOutputStream(
											file,
											append
											))
									))
							)),
					charset
					));
		});
	}

	/**
	 * US-ASCII, SJIS, UTF-8 用 LF -> CR_LF 置き換えストリーム // orig: * US-ASCII, SJIS, UTF-8 \u7528 LF -> CR_LF \u7f6e\u304d\u63db\u3048\u30b9\u30c8\u30ea\u30fc\u30e0
	 *
	 */
	public static class CrLfStream extends OutputStream {
		private OutputStream _inner;

		public CrLfStream(OutputStream inner) {
			_inner = inner;
		}

		@Override
		public void write(int b) throws IOException {
			if(b == 0x0d) { // ? CR
				return;
			}
			if(b == 0x0a) { // ? LF
				_inner.write(0x0d); // CR
			}
			_inner.write(b);
		}

		@Override
		public void close() throws IOException {
			_inner.close();
		}
	}

	public static byte[] read(InputStream reader, int size) throws Exception {
		byte[] buff = new byte[size];
		read(reader, buff);
		return buff;
	}

	public static void read(InputStream reader, byte[] buff) throws Exception {
		read(reader, buff, 0);
	}

	public static void read(InputStream reader, byte[] buff, int offset) throws Exception {
		read(reader, buff, offset, buff.length - offset);
	}

	public static void read(InputStream reader, byte[] buff, int offset, int length) throws Exception {
		if(reader.read(buff, offset, length) != length) {
			throw new IOException("Stream is cut off");
		}
	}
}
