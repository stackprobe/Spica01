package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class BooleanTools {
	public static Comparator<Boolean> comp = new Comparator<Boolean>() {
		@Override
		public int compare(Boolean a, Boolean b) {
			return VariantTools.comp(a, b, v -> v ? 1 : 0);
		}
	};

	public static Comparator<boolean[]> comp_array = new Comparator<boolean[]>() {
		@Override
		public int compare(boolean[] a, boolean[] b) {
			return ListTools.comp(asList(a), asList(b), comp);
		}
	};

	public static List<Boolean> asList(boolean[] inner) {
		return IArrays.asList(wrap(inner));
	}

	public static IArray<Boolean> wrap(boolean[] inner) {
		return new IArray<Boolean>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Boolean get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Boolean element) {
				inner[index] = element;
			}
		};
	}

	public static boolean[] toArray(List<Boolean> src) {
		int size = src.size();
		boolean[] dest = new boolean[size];

		for(int index = 0; index < size; index++) {
			dest[index] = src.get(index);
		}
		return dest;
	}

	public static boolean antiNull(Boolean value) {
		return antiNull(value, false);
	}

	public static boolean antiNull(Boolean value, boolean defval) {
		return value == null ? defval : value.booleanValue();
	}
}
