package charlotte.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class FileTools {
	public static void delete(String path) throws Exception {
		File f = new File(path);

		if(f.exists()) {
			for(int c = 1; ; c++) {
				try {
					deleteFileOrDirectory(path);

					if(f.exists() == false) {
						break;
					}
				}
				catch(Throwable e) {
					e.printStackTrace(System.out);
				}
				if(10 < c) {
					throw new RTError("ファイルの削除に失敗しました。" + path);
				}
				System.out.println("ファイルの削除をリトライします。" + path);

				Thread.sleep(c * 100);
			}
		}
	}

	private static void deleteFileOrDirectory(String path) throws Exception {
		File f = new File(path);

		if(f.isDirectory()) {
			for(String childLocalName : f.list()) {
				deleteFileOrDirectory(combine(path, childLocalName));
			}
		}
		if(f.delete() == false) {
			throw new RTError("ファイル又はディレクトリの削除に失敗しました。" + path);
		}
	}

	public static String combine(String path1, String path2) {
		String path = path1 + "/" + path2;

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

	public static void createDir(String dir) {
		File d = new File(dir);

		if(d.isDirectory() == false && new File(dir).mkdirs() == false) {
			throw new RTError("ディレクトリの作成に失敗しました。" + dir);
		}
	}

	public static void cleanupDir(String dir) throws Exception {
		for(String childLocalName : new File(dir).list()) {
			delete(combine(dir, childLocalName));
		}
	}

	public static void copyDir(String rDir, String wDir) throws Exception {
		createDir(wDir);

		for(String childLocalName : new File(rDir).list()) {
			String rPath = combine(rDir, childLocalName);
			String wPath = combine(wDir, childLocalName);

			if(new File(rPath).isDirectory()) {
				copyDir(rPath, wPath);
			}
			else {
				copyFile(rPath, wPath);
			}
		}
	}

	public static void copyFile(String rFile, String wFile) throws Exception {
		byte[] buff = new byte[4 * 1024 * 1024];

		try(
				FileInputStream reader = new FileInputStream(rFile);
				FileOutputStream writer = new FileOutputStream(wFile);
				) {
			for(; ; ) {
				int readSize = reader.read(buff);

				if(readSize <= 0) {
					break;
				}
				writer.write(buff, 0, readSize);
			}
		}
	}

	public static void moveDir(String rDir, String wDir) {
		createDir(wDir);

		for(String childLocalName : new File(rDir).list()) {
			String rPath = combine(rDir, childLocalName);
			String wPath = combine(wDir, childLocalName);

			if(new File(rPath).isDirectory()) {
				moveDir(rPath, wPath);
			}
			else {
				moveFile(rPath, wPath);
			}
		}
	}

	public static void moveFile(String rFile, String wFile) {
		if(new File(rFile).renameTo(new File(wFile)) == false) {
			throw new RTError("ファイル又はディレクトリの移動に失敗しました。" + rFile + " -> " + wFile);
		}
	}

	public static String changeRoot(String path, String oldRoot, String rootNew) {
		oldRoot = putSlash(oldRoot);
		rootNew = putSlash(rootNew);

		if(StringTools.startsWithIgnoreCase(path, oldRoot) == false) {
			throw new RTError("パスの配下ではありません。" + oldRoot + " -> " + path);
		}
		return rootNew + path.substring(oldRoot.length());
	}

	public static String putSlash(String path) {
		if(path.endsWith("/") == false || path.endsWith("\\") == false) {
			path += "/";
		}
		return path;
	}

	public static String makeFullPath(String path) throws Exception {
		return new File(path).getCanonicalPath();
	}

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

	public static List<String> readAllLines(String file, String charset) throws Exception {
		return textToLines(readAllText(file, charset));
	}

	public static List<String> textToLines(String text) {
		text = text.replace("\r", "");

		List<String> lines = StringTools.tokenize(text, "\n");

		if(1 <= lines.size() && lines.get(lines.size() - 1).length() == 0) {
			lines.remove(lines.size() - 1);
		}
		return lines;
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
			byte[] buff = new byte[4 * 1024 * 1024];

			for(; ; ) {
				int readSize = reader.read(buff);

				if(readSize == -1) {
					break;
				}
				if(readSize <= 0 || buff.length < readSize) {
					System.out.println("想定外の読み込みサイズです。" + readSize + ", " + buff.length);
				}
				mem.write(buff, 0, readSize);
			}
			return mem.toByteArray();
		}
	}

	public static void writeAllBytes(String file, byte[] fileData) throws Exception {
		try(FileOutputStream writer = new FileOutputStream(file)) {
			writer.write(fileData);
		}
	}

	public static void writeAllText(String file, String text, String charset) throws Exception {
		writeAllBytes(file, text.getBytes(charset));
	}

	public static void writeAllLines(String file, String[] lines, String charset) throws Exception {
		writeAllLines(file, IArrays.asList(lines), charset);
	}

	public static void writeAllLines(String file, List<String> lines, String charset) throws Exception {
		writeAllText(file, lines.size() == 0 ? "" : String.join("\r\n", lines) + "\r\n", charset);
	}

	public static int lastIndexOfPathDelimiter(String path) {
		return Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
	}

	public static int indexOfExtension(String path) {
		int index = path.lastIndexOf('.');

		if(index < lastIndexOfPathDelimiter(path)) {
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
}
