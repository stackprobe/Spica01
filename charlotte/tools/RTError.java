package charlotte.tools;

public class RTError extends RuntimeException {
	public RTError() {
		super();
	}

	public RTError(String message) {
		super(message);
	}

	public RTError(String message, Throwable e) {
		super(message, e);
	}

	public RTError(Throwable e) {
		super(e);
	}

	public static RuntimeException re(Throwable e) {
		if(e instanceof Error) {
			throw (Error)e;
		}
		if(e instanceof RuntimeException) {
			return (RuntimeException)e;
		}
		return new RTError(e);
	}

	public static RuntimeException re(Throwable[] es) {
		return re(IArrays.asList(es));
	}

	public static RuntimeException re(Iterable<Throwable> es) {
		RTError e = new RTError("Has some suppressed exceptions.");

		for(Throwable t : es) {
			e.addSuppressed(t);
		}
		return e;
	}

	public static void run(RunnableEx routine) {
		try {
			routine.run();
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}

	public static <T> T get(SupplierEx<T> routine) {
		try {
			return routine.get();
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}
}
