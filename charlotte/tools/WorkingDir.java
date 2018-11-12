package charlotte.tools;

public class WorkingDir implements AutoCloseable {
	private static String _root = null;

	private static String getRoot() throws Exception {
		if(_root == null) {
			_root = FileTools.combine(System.getenv("TMP"), "{8703e57a-d2b5-44d6-a85d-39ae214500cb}_" + ExtraTools.PID); // UUID v4, only in here

			FileTools.delete(_root);
			FileTools.createDir(_root);
		}
		return _root;
	}

	private static long _ctorCounter = 0L;

	private String _dir;

	public WorkingDir() throws Exception {
		_dir = FileTools.combine(getRoot(), "$" + (_ctorCounter++));

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
}
