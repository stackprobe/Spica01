package wb.t20190228;

import charlotte.tools.CSemaphore;

public class ThreadCSemaphore extends CSemaphore {
	public ThreadCSemaphore(int permit) {
		super(permit);
	}

	private ThreadLocal<Integer> _enteredCounts = new ThreadLocal<Integer>() {
		@Override
		public Integer initialValue() {
			return 0;
		}
	};

	@Override
	public void enter() {
		int enteredCount = _enteredCounts.get().intValue();

		if(enteredCount == 0) {
			super.enter();
		}
		_enteredCounts.set(enteredCount + 1);
	}

	@Override
	public void leave() {
		int enteredCount = _enteredCounts.get().intValue();

		if(enteredCount == 0) {
			throw null; // never
		}
		if(enteredCount == 1) {
			super.leave();
		}
		_enteredCounts.set(enteredCount - 1);
	}
}
