package charlotte.tools;

public class ObjectTools {
	public static boolean equals(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

	public static int hashCode(Object o) {
		return hashCode(o, 0);
	}

	public static int hashCode(Object o, int defval) {
		return o == null ? defval : o.hashCode();
	}
}
