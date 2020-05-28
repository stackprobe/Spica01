package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class FilingCase3Client implements AutoCloseable {
	private SockClient _client;
	private String _basePath;

	public FilingCase3Client() throws Exception {
		this("localhost");
	}

	public FilingCase3Client(String domain) throws Exception {
		this(domain, 65123);
	}

	public FilingCase3Client(String domain, int portNo) throws Exception {
		this(domain, portNo, "Common");
	}

	public FilingCase3Client(String domain, int portNo, String basePath) throws Exception {
		_basePath = basePath;
		connect(domain, portNo);
	}

	private void connect(String domain, int portNo) throws Exception {
		for(int c = 1; ; c++) {
			if(tryConnect(domain, portNo)) {
				return;
			}
			if(3 <= c) {
				throw new RTError("Can not connect to " + domain + ":" + portNo);
			}
			Thread.sleep(5000);
		}
	}

	private boolean tryConnect(String domain, int portNo) throws Exception {
		try {
			_client = new SockClient();
			_client.connect(domain, portNo, 5000);

			hello();

			return true;
		}
		catch(Throwable e) {
			e.printStackTrace(System.out);
		}

		if(_client != null) {
			_client.close();
			_client = null;
		}
		return false;
	}

	public byte[] get(String path) throws Exception {
		send("GET", path);
		byte[] ret = read64();
		readLineCheck("/GET/e");
		return ret;
	}

	public int post(String path, byte[] data) throws Exception {
		send("POST", path, data);
		readLineCheck("/POST/e");
		return 1;
	}

	public byte[] getPost(String path, byte[] data) throws Exception {
		send("GET-POST", path, data);
		byte[] ret = read64();
		readLineCheck("/GET/e");
		readLineCheck("/GET-POST/e");
		return ret;
	}

	public List<String> list(String path) throws Exception {
		send("LIST", path);

		{
			List<String> dest = new ArrayList<String>();

			for(; ; ) {
				String line = readLine();

				if(line.equals("/LIST/e")) {
					break;
				}
				dest.add(line);
			}
			return dest;
		}
	}

	public int delete(String path) throws Exception {
		send("DELETE", path);
		readLineCheck("/DELETE/e");
		return 1;
	}

	public int hello() throws Exception {
		_client.idleTimeoutMillis = 5000;

		send("HELLO", "$");
		readLineCheck("/HELLO/e");

		_client.idleTimeoutMillis = -1; // タイムアウトはサーバー側に任せる。 // orig: _client.idleTimeoutMillis = -1; // \u30bf\u30a4\u30e0\u30a2\u30a6\u30c8\u306f\u30b5\u30fc\u30d0\u30fc\u5074\u306b\u4efb\u305b\u308b\u3002

		return 1;
	}

	private void send(String command, String path) throws Exception {
		send(command, path, new byte[0]);
	}

	private void send(String command, String path, byte[] data) throws Exception {
		writeLine(command);
		writeLine(FileTools.combine(_basePath, path).replace('/', '\\'));
		writeLine("" + data.length);
		_client.send(data);
		writeLine("/SEND/e");
	}

	private void writeLine(String line) throws Exception {
		_client.send((line + "\r\n").getBytes(StringTools.CHARSET_SJIS));
	}

	private void readLineCheck(String line) throws Exception {
		if(readLine().equals(line) == false) {
			throw new Exception("READ_LINE_ERROR: " + line);
		}
	}

	private String readLine() throws Exception {
		return new String(read(), StringTools.CHARSET_SJIS);
	}

	private byte[] read() throws Exception {
		return read(toInt(read(4)));
	}

	private byte[] read64() throws Exception {
		return read(toInt(read(8)));
	}

	private byte[] read(int size) throws Exception {
		return _client.recv(size);
	}

	private int toInt(byte[] src) {
		return
				((int)src[0] << 0) |
				((int)src[1] << 8) |
				((int)src[2] << 16) |
				((int)src[3] << 24);
	}

	/**
	 * Never throw exception from the method.
	 *
	 */
	@Override
	public void close() throws Exception {
		if(_client != null) {
			_client.close();
			_client = null;
		}
	}
}
