package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class DoubleTools {
	public static Comparator<Double> comp = new Comparator<Double>() {
		@Override
		public int compare(Double a, Double b) {
			if(a < b) {
				return -1;
			}
			if(a > b) {
				return 1;
			}
			return 0;
		}
	};

	public static Comparator<double[]> comp_array = new Comparator<double[]>() {
		@Override
		public int compare(double[] a, double[] b) {
			return ListTools.comp(asList(a), asList(b), comp);
		}
	};

	public static List<Double> asList(double[] inner) {
		return IArrayTools.asList(wrap(inner));
	}

	public static IArray<Double> wrap(double[] inner) {
		return new IArray<Double>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Double get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Double element) {
				inner[index] = element;
			}
		};
	}

	public static double range(double value, double minval, double maxval) {
		return Math.max(minval, Math.min(maxval,  value));
	}

	public static double toDouble(String str, double minval, double maxval, double defval) {
		try {
			double value = Double.parseDouble(str);

			if(value < minval || maxval < value) {
				throw null;
			}
			return value;
		}
		catch(Throwable e) {
			return defval;
		}
	}
}
