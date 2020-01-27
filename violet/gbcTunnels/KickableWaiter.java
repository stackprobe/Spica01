package violet.gbcTunnels;

import charlotte.tools.SockChannel;

public class KickableWaiter {
	public void kick() {
		if(_waitingThread == null) {
			_kicked = true;
		}
		else {
			_waitingThread.interrupt();
		}
	}

	public void reset() {
		_millis = 0;
	}

	private static final int MILLIS_MAX = 2000;
	private static final int MILLIS_ADD = 100;

	private boolean _kicked = false;
	private Thread _waitingThread = null;
	private int _millis = MILLIS_MAX;

	public void waitForMoment() throws Exception {
		if(_kicked) {
			_kicked = false;
			return;
		}
		_waitingThread = Thread.currentThread();

		SockChannel.critical.unsection_a(() -> {
			try {
				System.out.println("*w1 " + Thread.currentThread().getId() + ", " +  _millis); // test
				Thread.sleep(_millis);
				System.out.println("*w2 " + Thread.currentThread().getId() + ", " +  _millis); // test
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
