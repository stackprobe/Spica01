package charlotte.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayTools {
	public static <T> Iterable<T> iterable(T[] inner) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {
					private int _index = 0;

					@Override
					public boolean hasNext() {
						return _index < inner.length;
					}

					@Override
					public T next() {
						return inner[_index++];
					}
				};
			}
		};
	}

	public static <T> int comp(T[] a, T[] b, Comparator<T> comp) {
		int minlen = Math.min(a.length, b.length);

		for(int index = 0; index < minlen; index++) {
			int ret = comp.compare(a[index], b[index]);

			if(ret != 0) {
				return ret;
			}
		}
		return IntTools.comp.compare(a.length, b.length);
	}

	public static <T> void swap(T[] arr, int a, int b) {
		T tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}

	public static <T> int indexOf(T[] arr, T target, Comparator<T> comp) {
		return indexOf(arr, target, comp, -1);
	}

	public static <T> int indexOf(T[] arr, T target, Comparator<T> comp, int defval) {
		for(int index = 0; index < arr.length; index++) {
			if(comp.compare(arr[index], target) == 0) {
				return index;
			}
		}
		return defval;
	}

	public static <T> int indexOf(T[] arr, Predicate<T> match) {
		return indexOf(arr, match, -1);
	}

	public static <T> int indexOf(T[] arr, Predicate<T> match, int defval) {
		for(int index = 0; index < arr.length; index++) {
			if(match.test(arr[index])) {
				return index;
			}
		}
		return defval;
	}

	public static <T> boolean contains(T[] arr, T target, Comparator<T> comp) {
		return indexOf(arr, target, comp) != -1;
	}

	public static <T> boolean contains(T[] arr, Predicate<T> match) {
		return indexOf(arr, match) != -1;
	}

	public static <T> List<T> toList(T[] src) {
		List<T> dest = new ArrayList<T>(src.length);

		for(int index = 0; index < src.length; index++) {
			dest.add(src[index]);
		}
		return dest;

		//return new ArrayList<T>(Arrays.asList(src));
	}

	public static <T> void merge(T[] arr1, T[] arr2, List<T> destOnly1, List<T> destBoth1, List<T> destBoth2, List<T> destOnly2, Comparator<T> comp) {
		ListTools.merge(Arrays.asList(arr1), Arrays.asList(arr2), destOnly1, destBoth1, destBoth2, destOnly2, comp);
	}

	public static <T> List<PairUnit<T, T>> getMergedPairs(T[] arr1, T[] arr2, T defval, Comparator<T> comp) {
		return ListTools.getMergedPairs(Arrays.asList(arr1), Arrays.asList(arr2), defval, comp);
	}

	public static <T> List<T> distinct(T[] src, Comparator<T> comp) {
		List<T> dest = new ArrayList<T>();

		if(1 <= src.length) {
			T lastElement = src[0];

			dest.add(lastElement);

			for(int index = 1; index < src.length; index++) {
				T element = src[index];

				if(comp.compare(element, lastElement) != 0) {
					dest.add(element);
					lastElement = element;
				}
			}
		}
		return dest;
	}

	public static <T> T lightest(T[] src, Function<T, Double> toWeight) {
		T ret = src[0];
		double ret_weight = toWeight.apply(ret);

		for(int index = 1; index < src.length; index++ ) {
			T element = src[index];
			double weight = toWeight.apply(element);

			if(weight < ret_weight) {
				ret = element;
				ret_weight = weight;
			}
		}
		return ret;
	}

	public static <T> T heaviest(T[] src, Function<T, Double> toWeight) {
		return lightest(src, element -> toWeight.apply(element) * -1);
	}

	public static <T> T smallest(T[] src, Comparator<T> comp) {
		T ret = src[0];

		for(int index = 0; index < src.length; index++) {
			T element = src[index];

			if(comp.compare(element, ret) < 0) {
				ret = element;
			}
		}
		return ret;
	}

	public static <T> T largest(T[] src, Comparator<T> comp) {
		return smallest(src, (a, b) -> comp.compare(a, b) * -1);
	}

	public static <T, R> List<R> select(T[] src, Function<T, R> conv) {
		return ListTools.select(Arrays.asList(src), conv);
	}

	public static <T> List<T> where(T[] src, Predicate<T> match) {
		return ListTools.where(Arrays.asList(src), match);
	}

	public static <T> boolean any(T[] src, Predicate<T> match) {
		return ListTools.any(Arrays.asList(src), match);
	}
}
