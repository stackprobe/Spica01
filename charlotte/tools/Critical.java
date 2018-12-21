package charlotte.tools;

public class Critical {
	private Object SYNCROOT = new Object();
	private int _entry = 0;

	public void enter() {
		synchronized(SYNCROOT) {
			enter2();
		}
	}

	private synchronized void enter2() {
		_entry++;

		if(_entry == 2) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				// noop
			}
		}
	}

	public synchronized void leave() {
		if(_entry == 0) {
			return; // XXX never
		}
		_entry--;

		if(_entry == 1) {
			notify();
		}
	}

	public AutoCloseable section() {
		enter();

		return new AutoCloseable() {
			@Override
			public void close() throws Exception {
				leave();
			}
		};
	}

	public AutoCloseable unsection() {
		leave();

		return new AutoCloseable() {
			@Override
			public void close() throws Exception {
				enter();
			}
		};
	}
}
