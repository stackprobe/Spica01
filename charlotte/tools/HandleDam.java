package charlotte.tools;

public class HandleDam {
	public static <T> T section_get(FunctionEx<HandleDam, T> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			return routine.apply(hDam);
		}
		finally {
			hDam.burst();
		}
	}

	public static void section(ConsumerEx<HandleDam> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			routine.accept(hDam);
		}
		finally {
			hDam.burst();
		}
	}

	public static <T> T transaction_get(FunctionEx<HandleDam, T> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			return routine.apply(hDam);
		}
		catch(Throwable e) {
			hDam.burst(e);
			throw null; // never
		}
	}

	public static void transaction(ConsumerEx<HandleDam> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			routine.accept(hDam);
		}
		catch(Throwable e) {
			hDam.burst(e);
			//throw null; // never
		}
	}

	private IStack<AutoCloseable> _handles = new StackUnit<AutoCloseable>();

	public <T extends AutoCloseable> T add(T handle) {
		_handles.push(handle);
		return handle;
	}

	public void burst() throws Exception {
		ExceptionDam.section(eDam -> {
			burst(eDam);
		});
	}

	public void burst(Throwable e) throws Exception {
		ExceptionDam.section(eDam -> {
			eDam.add(e);
			burst(eDam);
		});
	}

	public void burst(ExceptionDam eDam) {
		while(_handles.hasElements()) {
			AutoCloseable handle = _handles.pop();
			eDam.invoke(() -> handle.close());
		}
	}
}
