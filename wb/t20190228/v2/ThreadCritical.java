package wb.t20190228.v2;

import charlotte.tools.Critical;

public class ThreadCritical extends Critical {
	private Object SYNCROOT = new Object();
	private Thread _enteredTh = null;
	private int _reenteredCount = 0;

	@Override
	public void enter() {
		Thread currentTh = Thread.currentThread();

		synchronized(SYNCROOT) {
			if(_enteredTh == currentTh) {
				_reenteredCount++;
				return;
			}
		}
		super.enter();

		synchronized(SYNCROOT) {
			_enteredTh = currentTh;
		}
	}

	@Override
	public void leave() {
		Thread currentTh = Thread.currentThread();

		synchronized(SYNCROOT) {
			if(_enteredTh != currentTh) {
				throw null; // never
			}
			if(1 <= _reenteredCount) {
				_reenteredCount--;
				return;
			}
			_enteredTh = null;
		}
		super.leave();
	}
}
