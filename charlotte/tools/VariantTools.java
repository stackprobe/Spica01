package charlotte.tools;

import java.util.function.Function;

public class VariantTools {
	public static boolean equals(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

	public static int hashCode(Object o) {
		return hashCode(o, 0);
	}

	public static int hashCode(Object o, int defval) {
		return o == null ? defval : o.hashCode();
	}

	public static <T> int comp(T a, T b, Function<T, Integer> getWeight) {
		return IntTools.comp.compare(getWeight.apply(a), getWeight.apply(b));
	}
}
