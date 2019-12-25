package violet.gbcTunnels;

public class PumpWaiter implements IWaitTimer {
	private int _millis = 0;

	@Override
	public void reset() {
		_millis = 0;
	}

	@Override
	public int getWaitTimeMillis() {
		if(_millis < 100) {
			_millis++;
		}
		return _millis;
	}
}
