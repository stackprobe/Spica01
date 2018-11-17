package charlotte.tools;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IArrayTools {
	public static <T> List<T> asList(IArray<T> arr) {
		return new List<T>() {
			@Override
			public int size() {
				return arr.length();
			}

			@Override
			public T get(int index) {
				return arr.get(index);
			}

			@Override
			public T set(int index, T element) {
				T ret = arr.get(index);
				arr.set(index, element);
				return ret;
			}

			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {
					private int _index = 0;

					@Override
					public boolean hasNext() {
						return _index < size();
					}

					@Override
					public T next() {
						return get(_index++);
					}
				};
			}

			@Override
			public void sort(Comparator<? super T> comp) {
				arr.sort((a, b) -> comp.compare(a, b));
			}

			@Override
			public boolean isEmpty() {
				throw new RTError("forbidden");
			}

			@Override
			public boolean contains(Object target) {
				throw new RTError("forbidden");
			}

			@Override
			public Object[] toArray() {
				throw new RTError("forbidden");
			}

			@Override
			public <E> E[] toArray(E[] store) {
				throw new RTError("forbidden");
			}

			@Override
			public boolean add(T e) {
				throw new RTError("forbidden");
			}

			@Override
			public boolean remove(Object target) {
				throw new RTError("forbidden");
			}

			@Override
			public boolean containsAll(Collection<?> targets) {
				throw new RTError("forbidden");
			}

			@Override
			public boolean addAll(Collection<? extends T> elements) {
				throw new RTError("forbidden");
			}

			@Override
			public boolean addAll(int index, Collection<? extends T> elements) {
				throw new RTError("forbidden");
			}

			@Override
			public boolean removeAll(Collection<?> targets) {
				throw new RTError("forbidden");
			}

			@Override
			public boolean retainAll(Collection<?> targets) {
				throw new RTError("forbidden");
			}

			@Override
			public void clear() {
				throw new RTError("forbidden");
			}

			@Override
			public void add(int index, T element) {
				throw new RTError("forbidden");
			}

			@Override
			public T remove(int index) {
				throw new RTError("forbidden");
			}

			@Override
			public int indexOf(Object o) {
				throw new RTError("forbidden");
			}

			@Override
			public int lastIndexOf(Object o) {
				throw new RTError("forbidden");
			}

			@Override
			public ListIterator<T> listIterator() {
				throw new RTError("forbidden");
			}

			@Override
			public ListIterator<T> listIterator(int index) {
				throw new RTError("forbidden");
			}

			@Override
			public List<T> subList(int fromIndex, int toIndex) {
				throw new RTError("forbidden");
			}
		};
	}

	public static <T> List<T> asList(T[] inner) {
		return asList(wrap(inner));
	}

	public static <T> List<T> asList(List<T> inner) {
		return asList(wrap(inner));
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

			@Override
			public void sort(Comparator<T> comp) {
				Arrays.sort(inner, comp);
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

			@Override
			public void sort(Comparator<T> comp) {
				inner.sort(comp);
			}
		};
	}
}
