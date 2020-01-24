package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class CSemaphore {
	private int _permit;

	public CSemaphore(int permit) {
		if(permit < 1 || IntTools.IMAX < permit) {
			throw new IllegalArgumentException();
		}
		_permit = permit;
	}

	private Object SYNCROOT = new Object();
	private List<Thread> _enteredThs = new ArrayList<Thread>();

	public void enter() {
		synchronized(SYNCROOT) {
			enter2();
		}
	}

	private synchronized void enter2() {
		if(ListTools.any(_enteredThs, th -> th == Thread.currentThread())) {
			throw null; // never
		}
		_enteredThs.add(Thread.currentThread());

		if(_enteredThs.size() == _permit + 1) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				// noop
			}
		}
	}

	public synchronized void leave() {
		int currThIndex = ListTools.indexOf(_enteredThs, th -> th == Thread.currentThread());

		if(currThIndex == -1) {
			throw null; // never
		}
		ExtraTools.fastDesertElement(_enteredThs, currThIndex);

		if(_enteredThs.size() == _permit) {
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
