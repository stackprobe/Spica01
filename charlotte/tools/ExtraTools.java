package charlotte.tools;

public class ExtraTools {
	public static RuntimeException re(Throwable e) {
		if(e instanceof Error) {
			throw (Error)e;
		}
		if(e instanceof RuntimeException) {
			return (RuntimeException)e;
		}
		return new RuntimeException(e);
	}
}
