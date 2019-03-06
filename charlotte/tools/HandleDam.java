package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class HandleDam {
	public static void section(ConsumerEx<HandleDam> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			routine.accept(hDam);
		}
		catch(Throwable e) {
			hDam.burst(e);
		}
	}

	private List<AutoCloseable> _handles = new ArrayList<AutoCloseable>();

	public <T extends AutoCloseable> T add(T handle) {
		_handles.add(handle);
		return handle;
	}

	public void burst(Throwable e) throws Exception {
		ExceptionDam.section(eDam -> {
			eDam.add(e);

			while(1 <= _handles.size()) {
				eDam.invoke(() -> _handles.remove(_handles.size() - 1).close());
			}
		});
	}
}
