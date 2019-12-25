package violet.gbcTunnels;

public class ClientConnectionWaiter implements IWaitTimer {
	@Override
	public void reset() {
		// noop
	}

	@Override
	public int getWaitTimeMillis() {
		return 2000;
	}
}
