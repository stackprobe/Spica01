package violet.gbcTunnels;

import charlotte.tools.SockChannel;

public class KickableWaiter {
	public void kick() {
		if(_waitingThread != null) {
			_waitingThread.interrupt();
		}
	}

	public void reset() {
		_millis = 0;
	}

	private static final int MILLIS_MAX = 2000;
	private static final int MILLIS_ADD = 100;

	private Thread _waitingThread = null;
	private int _millis = MILLIS_MAX;

	public void waitForMoment() throws Exception {
		_waitingThread = Thread.currentThread();

		SockChannel.critical.unsection_a(() -> {
			try {
				Thread.sleep(_millis);
			}
			catch(InterruptedException e) {
				// noop
			}
		});

		_waitingThread = null;

		if(_millis < MILLIS_MAX) {
			_millis += MILLIS_ADD;
		}
	}
}
