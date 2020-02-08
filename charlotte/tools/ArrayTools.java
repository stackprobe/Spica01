package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayTools {
	private static <T> List<T> asList(T[] arr) {
		return IArrays.asList(arr);
	}

	public static <T> Iterable<T> iterable(T[] inner) {
		return () -> new Iterator<T>() {
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

	public static <T> int comp(T[] a, T[] b, Comparator<T> comp) {
		return ListTools.comp(asList(a), asList(b), comp);
	}

	public static <T> void swap(T[] arr, int a, int b) {
		T tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}

	public static <T> int indexOf(T[] arr, T target, Comparator<T> comp) {
		return ListTools.indexOf(asList(arr), target, comp);
	}

	public static <T> int indexOf(T[] arr, T target, Comparator<T> comp, int defval) {
		return ListTools.indexOf(asList(arr), target, comp, defval);
	}

	public static <T> int indexOf(T[] arr, Predicate<T> match) {
		return ListTools.indexOf(asList(arr), match);
	}

	public static <T> int indexOf(T[] arr, Predicate<T> match, int defval) {
		return ListTools.indexOf(asList(arr), match, defval);
	}

	public static <T> int lastIndexOf(T[] arr, T target, Comparator<T> comp) {
		return ListTools.lastIndexOf(asList(arr), target, comp);
	}

	public static <T> int lastIndexOf(T[] arr, T target, Comparator<T> comp, int defval) {
		return ListTools.lastIndexOf(asList(arr), target, comp, defval);
	}

	public static <T> int lastIndexOf(T[] arr, Predicate<T> match) {
		return ListTools.lastIndexOf(asList(arr), match);
	}

	public static <T> int lastIndexOf(T[] arr, Predicate<T> match, int defval) {
		return ListTools.lastIndexOf(asList(arr), match, defval);
	}

	public static <T> boolean contains(T[] arr, T target, Comparator<T> comp) {
		return ListTools.contains(asList(arr), target, comp);
	}

	public static <T> boolean contains(T[] arr, Predicate<T> match) {
		return ListTools.contains(asList(arr), match);
	}

	public static <T> List<T> toList(T[] src) {
		List<T> dest = new ArrayList<T>(src.length);

		for(int index = 0; index < src.length; index++) {
			dest.add(src[index]);
		}
		return dest;

		//return new ArrayList<T>(Arrays.asList(src));
	}

	public static <T> void merge(T[] arr1, T[] arr2, List<T> destOnly1, List<T> destBoth1, List<T> destBoth2,
			List<T> destOnly2, Comparator<T> comp) {
		ListTools.merge(asList(arr1), asList(arr2), destOnly1, destBoth1, destBoth2, destOnly2, comp);
	}

	public static <T> List<PairUnit<T, T>> getMergedPairs(T[] arr1, T[] arr2, T defval, Comparator<T> comp) {
		return ListTools.getMergedPairs(asList(arr1), asList(arr2), defval, comp);
	}

	public static <T> List<T> distinct(T[] src, Comparator<T> comp) {
		return ListTools.distinct(iterable(src), comp);
	}

	public static <T> T lightest(T[] src, Function<T, Double> toWeight) {
		return ListTools.lightest(iterable(src), toWeight);
	}

	public static <T> T heaviest(T[] src, Function<T, Double> toWeight) {
		return ListTools.heaviest(iterable(src), toWeight);
	}

	public static <T> T smallest(T[] src, Comparator<T> comp) {
		return ListTools.smallest(iterable(src), comp);
	}

	public static <T> T largest(T[] src, Comparator<T> comp) {
		return ListTools.largest(iterable(src), comp);
	}

	public static <T, R> List<R> select(T[] src, Function<T, R> conv) {
		return ListTools.select(iterable(src), conv);
	}

	public static <T> List<T> where(T[] src, Predicate<T> match) {
		return ListTools.where(iterable(src), match);
	}

	public static <T> boolean any(T[] src, Predicate<T> match) {
		return ListTools.any(iterable(src), match);
	}

	public static <T> void reverse(T[] arr) {
		ListTools.reverse(asList(arr));
	}

	public static <T> List<T> copy(T[] src) {
		return ListTools.copy(iterable(src));
	}

	public static <T> List<T> copyOfRange(T[] src, int start) {
		return ListTools.copyOfRange(asList(src), start);
	}

	public static <T> List<T> copyOfRange(T[] src, int start, int end) {
		return ListTools.copyOfRange(asList(src), start, end);
	}

	public static <T> List<T> range(T[] src, int start) {
		return ListTools.range(asList(src), start);
	}

	public static <T> List<T> range(T[] src, int start, int end) {
		return ListTools.range(asList(src), start, end);
	}
}
