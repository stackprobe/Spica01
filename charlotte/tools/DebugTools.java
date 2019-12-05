package charlotte.tools;

public class DebugTools {
	public static void mustThrow(Runnable routine) {
		try {
			routine.run();
		}
		catch(Throwable e) {
			e.printStackTrace(System.out);
			return;
		}
		throw new RTError();
	}
}
