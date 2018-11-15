package charlotte.tools;

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
				sort2((a, b) -> comp.compare(a, b));
			}

			/**
			 * TODO ---> TimSort etc.
			 *
			 */
			private void sort2(Comparator<T> comp) {
				int span = size();
				boolean swapped;

				do {
					span = (int)(span / 1.3);
					swapped = false;

					if(span < 1) {
						span = 1;
					}
					else if(span == 9 || span == 10) {
						span = 11;
					}
					for(int left = 0, right = span; right < size(); left++ ,right++) {
						if(0 < comp.compare(get(left), get(right))) {
							swap(left, right);
							swapped = true;
						}
					}
				}
				while(1 < span || swapped);
			}

			private void swap(int a, int b) {
				set(a, set(b, get(a)));

				// old same
				/*
				T tmp = get(a);
				set(a, get(b));
				set(b, tmp);
				*/
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
}
