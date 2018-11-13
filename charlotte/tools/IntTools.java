package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class IntTools {
	public static int IMAX = 1000000000; // 10^9

	public static Comparator<Integer> comp = new Comparator<Integer>() {
		@Override
		public int compare(Integer a, Integer b) {
			if(a < b) {
				return -1;
			}
			if(a > b) {
				return 1;
			}
			return 0;
		}
	};

	public static Comparator<int[]> comp_array = new Comparator<int[]>() {
		@Override
		public int compare(int[] a, int[] b) {
			return ListTools.comp(asList(a), asList(b), comp);
		}
	};

	public static List<Integer> asList(int[] inner) {
		return IArrayTools.asList(wrap(inner));
	}

	public static IArray<Integer> wrap(int[] inner) {
		return new IArray<Integer>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Integer get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Integer element) {
				inner[index] = element;
			}
		};
	}

	public static int range(int value, int minval, int maxval) {
		return Math.max(minval, Math.min(maxval,  value));
	}

	public static int toInt(String str, int minval, int maxval, int defval) {
		try {
			int value = Integer.parseInt(str);

			if(value < minval || maxval < value) {
				throw null;
			}
			return value;
		}
		catch(Throwable e) {
			return defval;
		}
	}

	public static int[] sequence(int count) {
		return sequence(0, count);
	}

	public static int[] sequence(int firstValue, int count) {
		return sequence(firstValue, count, 1);
	}

	public static int[] sequence(int firstValue, int count, int step) {
		int[] ret = new int[count];

		for(int index = 0; index < count; index++) {
			ret[index] = firstValue + index * step;
		}
		return ret;
	}
}
