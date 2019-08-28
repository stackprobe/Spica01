package charlotte.tools;

public class CSemaphore {
	private int _permit;

	public CSemaphore(int permit) {
		if(permit < 1 || IntTools.IMAX < permit) {
			throw new IllegalArgumentException();
		}
		_permit = permit;
	}

	private Object SYNCROOT = new Object();
	private int _entry = 0;

	public void enter() {
		synchronized(SYNCROOT) {
			enter2();
		}
	}

	private synchronized void enter2() {
		_entry++;

		if(_entry == _permit + 1) {
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
			throw null; // never
		}
		_entry--;

		if(_entry == _permit) {
			notify();
		}
	}

	public <T> T section_get(SupplierEx<T> routine) throws Exception {
		enter();
		try {
			return routine.get();
		}
		finally {
			leave();
		}
	}

	public void section_a(RunnableEx routine) throws Exception {
		enter();
		try {
			routine.run();
		}
		finally {
			leave();
		}
	}

	public AutoCloseable section() {
		enter();
		return () -> leave();
	}

	public <T> T unsection_get(SupplierEx<T> routine) throws Exception {
		leave();
		try {
			return routine.get();
		}
		finally {
			enter();
		}
	}

	public void unsection_a(RunnableEx routine) throws Exception {
		leave();
		try {
			routine.run();
		}
		finally {
			enter();
		}
	}

	public AutoCloseable unsection() {
		leave();
		return () -> enter();
	}

	public void contextSwitching() throws Exception {
		leave();
		enter();
	}
}
