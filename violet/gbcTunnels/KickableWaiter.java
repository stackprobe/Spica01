package violet.gbcTunnels;

import charlotte.tools.SockChannel;

public class KickableWaiter {
	private boolean _resetFlag;

	public KickableWaiter(boolean resetFlag) {
		_resetFlag = resetFlag;
	}

	public void kick() {
		if(_waitingThread != null) {
			_waitingThread.interrupt();
		}
		if(_resetFlag) {
			_millis = 0;
		}
	}

	private static final int MILLIS_MAX = 2000;

	private Thread _waitingThread = null;
	private int _millis = MILLIS_MAX;

	public void waitForMoment() throws Exception {
		_waitingThread = Thread.currentThread();

		if(_millis < MILLIS_MAX) {
			_millis++;
		}
		int millis = _millis;

		SockChannel.critical.unsection_a(() -> {
			try {
				Thread.sleep(millis);
			}
			catch(InterruptedException e) {
				// noop
			}
		});

		_waitingThread = null;
	}
}
