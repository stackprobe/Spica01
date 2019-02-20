package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class LongTools {
	public static final long IMAX_64 = 1000000000000000000L; // 10^18

	public static Comparator<Long> comp = new Comparator<Long>() {
		@Override
		public int compare(Long a, Long b) {
			if(a < b) {
				return -1;
			}
			if(a > b) {
				return 1;
			}
			return 0;
		}
	};

	public static Comparator<long[]> comp_array = new Comparator<long[]>() {
		@Override
		public int compare(long[] a, long[] b) {
			return ListTools.comp(asList(a), asList(b), comp);
		}
	};

	public static List<Long> asList(long[] inner) {
		return IArrays.asList(wrap(inner));
	}

	public static IArray<Long> wrap(long[] inner) {
		return new IArray<Long>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Long get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Long element) {
				inner[index] = element;
			}
		};
	}

	public static long range(long value, long minval, long maxval) {
		return Math.max(minval, Math.min(maxval,  value));
	}

	public static long toLong(String str, long minval, long maxval, long defval) {
		try {
			long value = Long.parseLong(str);

			if(value < minval || maxval < value) {
				throw null;
			}
			return value;
		}
		catch(Throwable e) {
			return defval;
		}
	}

	public static long[] toArray(List<Long> src) {
		int size = src.size();
		long[] dest = new long[size];

		for(int index = 0; index < size; index++) {
			dest[index] = src.get(index);
		}
		return dest;
	}

	public static long antiNull(Long value) {
		return antiNull(value, 0L);
	}

	public static long antiNull(Long value, long defval) {
		return value == null ? defval : value.longValue();
	}
}
