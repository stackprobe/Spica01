package charlotte.tools;

import java.io.File;

public class WorkingDir implements AutoCloseable {
	private static final String ROOTDIR_IDENT = "{8703e57a-d2b5-44d6-a85d-39ae214500cb}"; // UUID v4, only in here

	private static String _rootDir = null;

	private static String getRootDir() throws Exception {
		if(_rootDir == null) {
			_rootDir = FileTools.combine(System.getenv("TMP"), ROOTDIR_IDENT + "_" + ExtraTools.PID);

			FileTools.delete(_rootDir);
			FileTools.createDir(_rootDir);
		}
		return _rootDir;
	}

	private static long _ctorCounter = 0L;

	private String _dir;

	public WorkingDir() throws Exception {
		_dir = FileTools.combine(getRootDir(), "$" + (_ctorCounter++));

		FileTools.createDir(_dir);
	}

	private long _pathCounter = 0L;

	public String makePath() {
		return this.getPath("$" + (_pathCounter++));
	}

	private String getPath(String localName) {
		return FileTools.combine(_dir, localName);
	}

	@Override
	public void close() throws Exception {
		FileTools.delete(_dir);
	}

	public static void main(String[] args) {
		try {
			cleanup();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void cleanup() throws Exception {
		for(File p : new File(System.getenv("TMP")).listFiles()) {
			String path = p.getCanonicalPath();

			if(StringTools.startsWithIgnoreCase(FileTools.getFileName(path), ROOTDIR_IDENT)) {
				FileTools.delete(path);
			}
		}
	}
}
