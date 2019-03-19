package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class FloatTools {
	public static Comparator<Float> comp = new Comparator<Float>() {
		@Override
		public int compare(Float a, Float b) {
			if(a < b) {
				return -1;
			}
			if(a > b) {
				return 1;
			}
			return 0;
		}
	};

	public static Comparator<float[]> comp_array = new Comparator<float[]>() {
		@Override
		public int compare(float[] a, float[] b) {
			return ListTools.comp(asList(a), asList(b), comp);
		}
	};

	public static List<Float> asList(float[] inner) {
		return IArrays.asList(wrap(inner));
	}

	public static IArray<Float> wrap(float[] inner) {
		return new IArray<Float>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Float get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Float element) {
				inner[index] = element;
			}
		};
	}

	public static float range(float value, float minval, float maxval) {
		return Math.max(minval, Math.min(maxval,  value));
	}

	public static float toFloat(String str, float minval, float maxval, float defval) {
		try {
			float value = Float.parseFloat(str);

			if(value < minval || maxval < value) {
				throw null;
			}
			return value;
		}
		catch(Throwable e) {
			return defval;
		}
	}

	public static float[] toArray(List<Float> src) {
		int size = src.size();
		float[] dest = new float[size];

		for(int index = 0; index < size; index++) {
			dest[index] = src.get(index);
		}
		return dest;
	}

	public static float antiNull(Float value) {
		return antiNull(value, 0F);
	}

	public static float antiNull(Float value, float defval) {
		return value == null ? defval : value.floatValue();
	}
}
