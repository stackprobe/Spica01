package charlotte.tools;

public class MutexUnit implements AutoCloseable {
	private String _mtxName;
	private String _leaveEvName;
	private Process _mtxProc = null;

	public MutexUnit(String ident) {
		this(ident, false);
	}

	public MutexUnit(String ident, boolean global) {
		_mtxName = SecurityTools.toFairIdent(ident);

		if(global) {
			_mtxName = "Global\\Global_" + _mtxName;
		}
		_leaveEvName = SecurityTools.makePassword_9A();
	}

	public boolean waitOne() {
		return waitOne(-1);
	}

	public boolean waitOne(int millis) {
		try {
			if(millis < -1 || IntTools.IMAX < millis) {
				throw new RTError();
			}
			if(_mtxProc != null) {
				throw new RTError();
			}
			String enterEvName = SecurityTools.makePassword_9A();
			String timeoutEvName = SecurityTools.makePassword_9A();

			try(
					NamedEventUnit enterEv = new NamedEventUnit(enterEvName);
					NamedEventUnit timeoutEv = new NamedEventUnit(timeoutEvName);
					) {
				_mtxProc = SpicaToolkit.exec("/MUTEX " + _mtxName + " " + millis + " " + enterEvName + " " + _leaveEvName + " " + timeoutEvName + " " + ExtraTools.PID);

				Thread enterEvTh = new Thread(() -> enterEv.waitOne());
				Thread timeoutEvTh = new Thread(() -> timeoutEv.waitOne());

				enterEvTh.start();
				timeoutEvTh.start();

				enterEvTh.join(millis);

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
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}

	public void releaseMutex() {
		try {
			if(_mtxProc == null) {
				throw new RTError();
			}
			SpicaToolkit.exec("/NAMED-EVENT-SET " + _leaveEvName);

			_mtxProc.waitFor();
			_mtxProc = null;
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}

	@Override
	public void close() throws Exception {
		if(_mtxProc != null) {
			throw new RTError();
		}
	}
}
