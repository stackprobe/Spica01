package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadEx implements AutoCloseable {
	private List<ThreadEx> _ths = new ArrayList<ThreadEx>();

	public void add(RunnableEx routine) {
		_ths.add(new ThreadEx(routine));
	}

	public boolean isEnded() throws Exception {
		return isEnded(0);
	}

	public boolean isEnded(int millis) throws Exception {
		for(ThreadEx th : _ths) {
			if(th.isEnded(millis) == false) {
				return false;
			}
		}
		return true;
	}

	public void waitToEnd() throws Exception {
		for(ThreadEx th : _ths) {
			th.waitToEnd();
		}
	}

	public void relayThrow() throws Exception {
		List<Throwable> es = getExceptions();

		if(1 <= es.size()) {
			throw RTError.re(es);
		}
	}

	public List<Throwable> getExceptions() throws Exception {
		waitToEnd();

		List<Throwable> es = ListTools.select(_ths, th -> RTError.get(() -> th.getException()));
		es.removeIf(e -> e == null);
		return es;
	}

	@Override
	public void close() throws Exception {
		waitToEnd();
	}
}
