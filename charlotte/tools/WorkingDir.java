package charlotte.tools;

public class WorkingDir implements AutoCloseable {
	private String _dir;

	public WorkingDir() throws Exception {
		_dir = FileTools.combine(System.getenv("TMP"), "{8703e57a-d2b5-44d6-a85d-39ae214500cb}_" + ExtraTools.PID);
		//_dir = FileTools.combine(System.getenv("TMP"), SecurityTools.makePassword_9a());

		FileTools.delete(_dir);
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
