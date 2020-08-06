package charlotte.tools;

import java.util.List;

public class FilingCase3 implements AutoCloseable {
	private String _domain;
	private int _portNo;
	private String _basePath;

	private ThreadEx _th;
	private boolean _stopFlag = false;
	private Object SYNCROOT = new Object();
	private FilingCase3Client _client = null;
	private int _clientAliveCount = -1;

	public FilingCase3() {
		this("localhost");
	}

	public FilingCase3(String domain) {
		this(domain, 65123);
	}

	public FilingCase3(String domain, int portNo) {
		this(domain, portNo, "Common");
	}

	public FilingCase3(String domain, int portNo, String basePath) {
		_domain = domain;
		_portNo = portNo;
		_basePath = basePath;

		_th = new ThreadEx(() -> {
			while(_stopFlag == false) {
				synchronized(SYNCROOT) {
					if(_client != null) {
						if(6 / 2 < ++_clientAliveCount) { // 6 sec
							_client.close();
							_client = null;
						}
					}
				}
				Thread.sleep(2000);
			}
		});
	}

	private <T> T perform(SupplierEx<T> rtn) throws Exception {
		synchronized(SYNCROOT) {
			try {
				if(_client == null) {
					_client = new FilingCase3Client(_domain, _portNo, _basePath);
				}
				_clientAliveCount = 0;

				return rtn.get();
			}
			catch(Throwable e) {
				if(_client != null) {
					_client.close();
					_client = null;
				}
				throw RTError.re(e);
			}
		}
	}

	public byte[] get(String path) throws Exception {
		return perform(() -> _client.get(path));
	}

	public int post(String path, byte[] data) throws Exception {
		return perform(() -> _client.post(path, data));
	}

	public byte[] getPost(String path, byte[] data) throws Exception {
		return perform(() -> _client.getPost(path, data));
	}

	public List<String> list(String path) throws Exception {
		return perform(() -> _client.list(path));
	}

	public int delete(String path) throws Exception {
		return perform(() -> _client.delete(path));
	}

	@Override
	public void close() throws Exception {
		if(_th != null) { // once
			_stopFlag = true;

			_th.waitToEnd();
			_th = null;

			if(_client != null) {
				_client.close();
				_client = null;
			}
		}
	}
}
