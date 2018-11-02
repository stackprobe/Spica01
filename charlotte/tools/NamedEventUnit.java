package charlotte.tools;

public class NamedEventUnit implements AutoCloseable {
	String _evName;
	String _timeoutEvName;
	Process _evProc;

	public NamedEventUnit(String ident) {
		this(ident, false);
	}

	public NamedEventUnit(String ident, boolean global) {
		try {
			_evName = SecurityTools.toFairIdent(ident);

			if(global) {
				_evName = "Global\\Global_" + _evName;
			}
			String enterEvName = SecurityTools.makePassword_9A();
			_timeoutEvName = SecurityTools.makePassword_9A();

			_evProc = SpicaToolkit.exec("/NAMED-EVENT " + _evName + " " + enterEvName + " " + _timeoutEvName + " " + ExtraTools.PID);

			SpicaToolkit.exec("/NAMED-EVENT-WAIT " + enterEvName + " -1 " + ExtraTools.PID).waitFor();
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}

	public void set() {
		SpicaToolkit.exec("/NAMED-EVENT-SET " + _evName);
	}

	public void waitOne() {
		waitOne(-1);
	}

	public void waitOne(int millis) {
		try {
			if(millis < -1 || IntTools.IMAX < millis) {
				throw new RTError();
			}
			SpicaToolkit.exec("/NAMED-EVENT-WAIT " + _evName + " " + millis + " " + ExtraTools.PID).waitFor();
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}

	@Override
	public void close() throws Exception {
		if(_evName != null) {
			SpicaToolkit.exec("/NAMED-EVENT-SET " + _timeoutEvName);
			_evName = null;
		}
	}
}
