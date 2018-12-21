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
			throw null; // never
		}
		_entry--;

		if(_entry == 1) {
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

	public void section(RunnableEx routine) throws Exception {
		enter();
		try {
			routine.run();
		}
		finally {
			leave();
		}
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

	public void unsection(RunnableEx routine) throws Exception {
		leave();
		try {
			routine.run();
		}
		finally {
			enter();
		}
	}
}
