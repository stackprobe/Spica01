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

	public void waitOne() throws Exception {
		waitOne(-1);
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
				ThreadEx enterEvTh = new ThreadEx(() -> enterEv.waitOne());
				ThreadEx timeoutEvTh = new ThreadEx(() -> timeoutEv.waitOne());
				) {
			_mtxProc = SpicaToolkit.exec("/MUTEX " + _mtxName + " " + millis + " " + enterEvName + " " + timeoutEvName + " " + _leaveEvName + " " + KernelTools.PID);

			if(millis == -1) {
				enterEvTh.waitToEnd();
				timeoutEv.set();
				return true;
			}
			else {
				if(enterEvTh.isEnded(millis)) {
					timeoutEv.set();
					return true;
				}

				millis = 0;

				for(; ; ) {
					if(millis < 100) {
						millis++;
					}

					if(timeoutEvTh.isEnded(millis) == false) {
						enterEv.set();
						_mtxProc = null;
						return false;
					}
					if(enterEvTh.isEnded() == false) {
						timeoutEv.set();
						return true;
					}
				}
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
