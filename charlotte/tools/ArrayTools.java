package charlotte.tools;

import java.util.Comparator;

public class ArrayTools {
	public static <T> int comp(T[] a, T[] b, Comparator<T> comp) {
		int minlen = Math.min(a.length, b.length);

		for (int index = 0; index < minlen; index++) {
			int ret = comp.compare(a[index], b[index]);

			if (ret != 0)
				return ret;
		}
		return IntTools.comp.compare(a.length, b.length);
	}
}
