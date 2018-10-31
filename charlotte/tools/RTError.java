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
}
