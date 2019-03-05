package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class HandleDam {
	public static void section(ConsumerEx<HandleDam> rtn) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			rtn.accept(hDam);
		}
		catch(Throwable e) {
			hDam.burst();
			throw RTError.re(e);
		}
	}

	private List<AutoCloseable> _handles = new ArrayList<AutoCloseable>();

	public <T extends AutoCloseable> T add(T handle) {
		_handles.add(handle);
		return handle;
	}

	public void burst() throws Exception {
		while(1 <= _handles.size()) {
			_handles.remove(_handles.size() - 1).close();
		}
	}
}
