package charlotte.tools;

public class ThreadEx implements AutoCloseable {
	private Thread _th;
	private Throwable _ex = null;

	public ThreadEx(RunnableEx routine) {
		_th = new Thread(() -> {
			try {
				routine.run();
			}
			catch(Throwable e) {
				_ex = e;
			}
		});

		_th.start();
	}

	public boolean isEnded() throws Exception {
		return isEnded(0);
	}

	public boolean isEnded(int millis) throws Exception {
		if(_th != null) {
			if(1 <= millis) {
				_th.join(millis);
			}
			if(_th.isAlive() == false) {
				_th = null;
			}
		}
		return _th == null;
	}

	public void waitToEnd() throws Exception {
		if(_th != null) {
			_th.join();
			_th = null;
		}
	}

	public void waitToEnd(Critical critical) throws Exception {
		if(_th != null) {
			critical.unsection_a(() -> _th.join());
			_th = null;
		}
	}

	public void relayThrow() throws Exception {
		waitToEnd();

		if(_ex != null) {
			throw RTError.re(_ex);
		}
	}

	public Throwable getException() throws Exception {
		waitToEnd();
		return _ex;
	}

	@Deprecated
	public Thread getThread_UNSAFE() {
		return _th;
	}

	@Override
	public void close() throws Exception {
		waitToEnd();
	}
}
