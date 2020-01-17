package violet.gbcTunnels;

public class KickableWaiter {
	private IWaitTimer _waitTimer;

	public KickableWaiter(IWaitTimer waitTimer) {
		_waitTimer = waitTimer;
	}

	public void kick() {
		throw null; // TODO -- kick & reset
	}

	public void reset() {
		throw null; // TODO
	}

	public void waitForMoment() {
		throw null; // TODO
	}
}
