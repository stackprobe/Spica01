package charlotte.tools;

public class MutexUnit implements AutoCloseable {
	private String _mtxName;
	private String _leaveEvName;
	private Process _mtxProc = null;

	public MutexUnit(String ident) throws Exception {
		this(ident, false);
	}

	public MutexUnit(String ident, boolean global) throws Exception {
		_mtxName = SecurityTools.toFairIdent(ident);

		if(global) {
			_mtxName = "Global\\Global_" + _mtxName;
		}
		_leaveEvName = SecurityTools.makePassword_9a();
	}

	public boolean waitOne() throws Exception {
		return waitOne(-1);
	}

	public boolean waitOne(int millis) throws Exception {
		if(millis < -1 || IntTools.IMAX < millis) {
			throw new RTError("bad millis: " + millis);
		}
		if(_mtxProc != null) {
			throw new RTError("already locked");
		}
		String enterEvName = SecurityTools.makePassword_9a();
		String timeoutEvName = SecurityTools.makePassword_9a();

		try(
				NamedEventUnit enterEv = new NamedEventUnit(enterEvName);
				NamedEventUnit timeoutEv = new NamedEventUnit(timeoutEvName);
				) {
			_mtxProc = SpicaToolkit.exec("/MUTEX " + _mtxName + " " + millis + " " + enterEvName + " " + timeoutEvName + " " + _leaveEvName + " " + ExtraTools.PID);

			Thread enterEvTh = new Thread(() -> enterEv.waitOne_rt());
			Thread timeoutEvTh = new Thread(() -> timeoutEv.waitOne_rt());

			enterEvTh.start();
			timeoutEvTh.start();

			enterEvTh.join(millis == -1 ? 0 : Math.max(1, millis));

			millis = 0;

			for(; ; ) {
				if(enterEvTh.isAlive() == false) {
					timeoutEv.set();
					timeoutEvTh.join();
					return true;
				}
				if(timeoutEvTh.isAlive() == false) {
					enterEv.set();
					enterEvTh.join();
					_mtxProc = null;
					return false;
				}
				if(millis < 100) {
					millis++;
				}
				timeoutEvTh.join(millis);
			}
		}
	}

	public void releaseMutex() throws Exception {
		if(_mtxProc != null) {
			SpicaToolkit.exec("/NAMED-EVENT-SET " + _leaveEvName).waitFor();

			_mtxProc.waitFor();
			_mtxProc = null;
		}
	}

	@Override
	public void close() throws Exception {
		releaseMutex();
	}
}
