package wb.t20190228.v2;

import charlotte.tools.CSemaphore;

public class ThreadCSemaphore {
	private CSemaphore _inner;

	public ThreadCSemaphore(int permit) {
		_inner = new CSemaphore(permit);
	}

	private ThreadLocal<Integer> _enteredCounts = new ThreadLocal<Integer>() {
		@Override
		public Integer initialValue() {
			return 0;
		}
	};

	public void enter() {
		int enteredCount = _enteredCounts.get().intValue();

		if(enteredCount == 0) {
			_inner.enter();
		}
		_enteredCounts.set(enteredCount + 1);
	}

	public void leave() {
		int enteredCount = _enteredCounts.get().intValue();

		if(enteredCount == 0) {
			throw null; // never
		}
		if(enteredCount == 1) {
			_inner.leave();
		}
		_enteredCounts.set(enteredCount - 1);
	}

	public CSemaphore inner() {
		return _inner;
	}
}
