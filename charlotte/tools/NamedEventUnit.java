package charlotte.tools;

public class NamedEventUnit implements AutoCloseable {
	String _evName;
	String _timeoutEvName;
	Process _evProc;

	public NamedEventUnit(String ident) throws Exception {
		this(ident, false);
	}

	public NamedEventUnit(String ident, boolean global) throws Exception {
		_evName = SecurityTools.toFairIdent(ident);

		if(global) {
			_evName = "Global\\Global_" + _evName;
		}
		String enterEvName = SecurityTools.makePassword_9a();
		_timeoutEvName = SecurityTools.makePassword_9a();

		_evProc = SpicaToolkit.exec("/NAMED-EVENT " + _evName + " " + enterEvName + " " + _timeoutEvName + " " + KernelTools.PID);

		SpicaToolkit.exec("/NAMED-EVENT-WAIT " + enterEvName + " -1 " + KernelTools.PID).waitFor();
	}

	public void set() throws Exception {
		SpicaToolkit.exec("/NAMED-EVENT-SET " + _evName).waitFor();
	}

	public void waitOne() throws Exception {
		waitOne(-1);
	}

	public void waitOne(int millis) throws Exception {
		if(millis < -1 || IntTools.IMAX < millis) {
			throw new RTError("bad millis: " + millis);
		}
		SpicaToolkit.exec("/NAMED-EVENT-WAIT " + _evName + " " + millis + " " + KernelTools.PID).waitFor();
	}

	@Override
	public void close() throws Exception {
		if(_evProc != null) {
			SpicaToolkit.exec("/NAMED-EVENT-SET " + _timeoutEvName).waitFor();

			_evProc.waitFor();
			_evProc = null;
		}
	}
}
