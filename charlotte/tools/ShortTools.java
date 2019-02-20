package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class ShortTools {
	public static Comparator<Short> comp = new Comparator<Short>() {
		@Override
		public int compare(Short a, Short b) {
			return (int)a - (int)b;
		}
	};

	public static Comparator<short[]> comp_array = new Comparator<short[]>() {
		@Override
		public int compare(short[] a, short[] b) {
			return ListTools.comp(asList(a), asList(b), comp);
		}
	};

	public static List<Short> asList(short[] inner) {
		return IArrays.asList(wrap(inner));
	}

	public static IArray<Short> wrap(short[] inner) {
		return new IArray<Short>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Short get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Short element) {
				inner[index] = element;
			}
		};
	}

	public static short[] toArray(List<Short> src) {
		int size = src.size();
		short[] dest = new short[size];

		for(int index = 0; index < size; index++) {
			dest[index] = src.get(index);
		}
		return dest;
	}

	public static short antiNull(Short value) {
		return antiNull(value, (short)0);
	}

	public static short antiNull(Short value, short defval) {
		return value == null ? defval : value.shortValue();
	}
}
