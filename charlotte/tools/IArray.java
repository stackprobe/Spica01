package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class IArray<T> {
	public static <T> IArray<T> wrap(T[] inner) {
		return new IArray<T>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public T get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, T element) {
				inner[index] = element;
			}
		};
	}

	public static <T> IArray<T> wrap(List<T> inner) {
		return new IArray<T>() {
			@Override
			public int length() {
				return inner.size();
			}

			@Override
			public T get(int index) {
				return inner.get(index);
			}

			@Override
			public void set(int index, T element) {
				inner.set(index, element);
			}
		};
	}

	public abstract int length();
	public abstract T get(int index);
	public abstract void set(int index, T element);

	public Iterable<T> iterable() {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {
					private int _index = 0;

					@Override
					public boolean hasNext() {
						return _index < length();
					}

					@Override
					public T next() {
						return get(_index);
					}
				};
			}
		};
	}

	public static <T> int comp(IArray<T> a, IArray<T> b, Comparator<T> comp) {
		int minlen = Math.min(a.length(), b.length());

		for(int index = 0; index < minlen; index++) {
			int ret = comp.compare(a.get(index), b.get(index));

			if(ret != 0) {
				return ret;
			}
		}
		return IntTools.comp.compare(a.length(), b.length());
	}

	public void swap(int a, int b) {
		T tmp = get(a);
		set(a, get(b));
		set(b, tmp);
	}

	public int indexOf(T target, Comparator<T> comp) {
		return indexOf(target, comp, -1);
	}

	public int indexOf(T target, Comparator<T> comp, int defval) {
		for(int index = 0; index < length(); index++) {
			if(comp.compare(get(index), target) == 0) {
				return index;
			}
		}
		return defval;
	}

	public int indexOf(Predicate<T> match) {
		return indexOf(match, -1);
	}

	public int indexOf(Predicate<T> match, int defval) {
		for(int index = 0; index < length(); index++) {
			if(match.test(get(index))) {
				return index;
			}
		}
		return defval;
	}

	public boolean contains(T target, Comparator<T> comp) {
		return indexOf(target, comp) != -1;
	}

	public boolean contains(Predicate<T> match) {
		return indexOf(match) != -1;
	}

	public List<T> toList() {
		List<T> dest = new ArrayList<T>(length());

		for(int index = 0; index < length(); index++) {
			dest.add(get(index));
		}
		return dest;
	}

	public void sort(Comparator<T> comp) {
		Sorter.sort(this, comp);
	}

	public List<T> asList() {
		return IArray2.asList(this);
	}

	public static <T> void merge(IArray<T> arr1, IArray<T> arr2, List<T> destOnly1, List<T> destBoth1, List<T> destBoth2, List<T> destOnly2, Comparator<T> comp) {
		ListTools.merge(arr1.asList(), arr2.asList(), destOnly1, destBoth1, destBoth2, destOnly2, comp);
	}

	public static <T> List<PairUnit<T, T>> getMergedPairs(IArray<T> arr1, IArray<T> arr2, T defval, Comparator<T> comp) {
		return ListTools.getMergedPairs(arr1.asList(), arr2.asList(), defval, comp);
	}

	public static <T> List<T> distinct(IArray<T> arr, Comparator<T> comp) {
		return ListTools.distinct(arr.iterable(), comp);
	}

	public static <T> T lightest(IArray<T> arr, Function<T, Double> toWeight) {
		return ListTools.lightest(arr.iterable(), toWeight);
	}

	public static <T> T heaviest(IArray<T> arr, Function<T, Double> toWeight) {
		return lightest(arr, element -> toWeight.apply(element) * -1);
	}

	public static <T> T smallest(IArray<T> arr, Comparator<T> comp) {
		return ListTools.smallest(arr.iterable(), comp);
	}

	public static <T> T largest(IArray<T> arr, Comparator<T> comp) {
		return smallest(arr, (a, b) -> comp.compare(a, b) * -1);
	}

	public static <T, R> List<R> select(IArray<T> arr, Function<T, R> conv) {
		return ListTools.select(arr.iterable(), conv);
	}

	public static <T> List<T> where(IArray<T> arr, Predicate<T> match) {
		return ListTools.where(arr.iterable(), match);
	}

	public static <T> boolean any(IArray<T> arr, Predicate<T> match) {
		return ListTools.any(arr.iterable(), match);
	}
}
