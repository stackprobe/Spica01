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

	public static <T> int comp(T[] a, T[] b, Comparator<T> comp) {
		return comp(wrap(a), wrap(b), comp);
	}

	public static <T> int comp(List<T> a, List<T> b, Comparator<T> comp) {
		return comp(wrap(a), wrap(b), comp);
	}

	public static <T> int comp(IArray<T> a, IArray<T> b, Comparator<T> comp) {
		int minlen = Math.min(a.length(), b.length());

		for (int index = 0; index < minlen; index++) {
			int ret = comp.compare(a.get(index), b.get(index));

			if (ret != 0)
				return ret;
		}
		return IntTools.comp.compare(a.length(), b.length());
	}

	public static <T> void swap(T[] arr, int a, int b) {
		T tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}

	public static <T> void swap(List<T> list, int a, int b) {
		T tmp = list.get(a);
		list.set(a, list.get(b));
		list.set(b, tmp);
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

	public static <T> List<T> toList(IArray<T> src) {
		List<T> dest = new ArrayList<T>(src.length());

		for(int index = 0; index < src.length(); index++) {
			dest.add(src.get(index));
		}
		return dest;
	}

	public static <T> T[] toArray(IArray<T> src, T[] store) {
		return toList(src).toArray(store); // XXX
	}

	public static <T> List<T> repeat(T element, int count) {
		List<T> dest = new ArrayList<T>(count);

		for(int index = 0; index < count; index++) {
			dest.add(element);
		}
		return dest;
	}

	public static <T> void merge(T[] arr1, T[] arr2, List<T> destOnly1, List<T> destBoth1, List<T> destBoth2, List<T> destOnly2, Comparator<T> comp) {
		Arrays.sort(arr1, comp);
		Arrays.sort(arr2, comp);

		int index1 = 0;
		int index2 = 0;

		for(; ; ) {
			int ret;

			if(arr1.length <= index1) {
				if(arr2.length <= index2) {
					break;
				}
				ret = 1;
			}
			else if(arr2.length <= index2) {
				ret = -1;
			}
			else {
				ret = comp.compare(arr1[index1], arr2[index2]);
			}

			if(ret < 0) {
				if(destOnly1 != null) {
					destOnly1.add(arr1[index1]);
				}
				index1++;
			}
			else if(0 < ret) {
				if(destOnly2 != null) {
					destOnly2.add(arr2[index2]);
				}
				index2++;
			}
			else {
				if(destBoth1 != null) {
					destBoth1.add(arr1[index1]);
				}
				if(destBoth2 != null) {
					destBoth2.add(arr2[index2]);
				}
				index1++;
				index2++;
			}
		}
	}

	public static <T> List<PairUnit<T, T>> getMergedPairs(T[] arr1, T[] arr2, T defval, Comparator<T> comp) {
		Arrays.sort(arr1, comp);
		Arrays.sort(arr2, comp);

		int index1 = 0;
		int index2 = 0;

		List<PairUnit<T, T>> dest = new ArrayList<PairUnit<T, T>>();

		for(; ; ) {
			int ret;

			if(arr1.length <= index1) {
				if(arr2.length <= index2) {
					break;
				}
				ret = 1;
			}
			else if(arr2.length <= index2) {
				ret = -1;
			}
			else {
				ret = comp.compare(arr1[index1], arr2[index2]);
			}

			if(ret < 0) {
				dest.add(new PairUnit<T, T>(arr1[index1++], defval));
			}
			else if(0 < ret) {
				dest.add(new PairUnit<T, T>(defval, arr2[index2++]));
			}
			else {
				dest.add(new PairUnit<T, T>(arr1[index1++], arr2[index2++]));
			}
		}
		return dest;
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

	public static <T, R> List<R> select(Iterable<T> src, Function<T, R> conv) {
		List<R> dest = new ArrayList<R>();

		for(T element : src) {
			dest.add(conv.apply(element));
		}
		return dest;
	}

	public static <T> List<T> where(Iterable<T> src, Predicate<T> match) {
		List<T> dest = new ArrayList<T>();

		for(T element : src) {
			if(match.test(element)) {
				dest.add(element);
			}
		}
		return dest;
	}

	public static <T> boolean any(Iterable<T> src, Predicate<T> match) {
		for(T element : src) {
			if(match.test(element)) {
				return true;
			}
		}
		return false;
	}
}
