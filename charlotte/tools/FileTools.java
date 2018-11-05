package charlotte.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

	private static String combine(String path1, String path2) {
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

	public static int compBinFile(String file1, String file2) throws Exception {
		try(
				FileInputStream nb_reader1 = new FileInputStream(file1);
				FileInputStream nb_reader2 = new FileInputStream(file2);
				BufferedInputStream reader1 = new BufferedInputStream(nb_reader1);
				BufferedInputStream reader2 = new BufferedInputStream(nb_reader2);
				) {
			for(; ; ) {
				int chr1 = reader1.read();
				int chr2 = reader2.read();

				if(chr1 != chr2) {
					return chr1 - chr2;
				}
				if(chr1 == -1) {
					return 0;
				}
			}
		}
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

	public static String[] readAllLines(String file, String charset) throws Exception {
		String text = readAllText(file, charset);

		text = text.replace("\r", "");

		List<String> lines = ArrayTools.toList(StringTools.tokenize(text, "\n"));

		if(1 <= lines.size() && lines.get(lines.size() - 1).length() == 0) {
			lines.remove(lines.size() - 1);
		}
		return lines.toArray(new String[lines.size()]);
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
		writeAllText(file, lines.length == 0 ? "" : String.join("\r\n", lines) + "\r\n", charset);
	}
}
