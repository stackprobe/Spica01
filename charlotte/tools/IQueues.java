package charlotte.tools;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Supplier;

public class IQueues {
	public static <T> IQueue<T> wrap(Iterable<T> src) {
		Iterator<T> iterator = src.iterator();

		return new IQueue<T>() {
			@Override
			public boolean hasElements() {
				return iterator.hasNext();
			}

			@Override
			public void enqueue(T element) {
				throw new RTError("forbidden");
			}

			@Override
			public T dequeue() {
				return iterator.next();
			}
		};
	}

	public static <T> Iterable<T> iterable(IQueue<T> src) {
		return () -> new Iterator<T>() {
			@Override
			public boolean hasNext() {
				return src.hasElements();
			}

			@Override
			public T next() {
				return src.dequeue();
			}
		};
	}

	public static <T> Iterable<T> iterable(Enumeration<T> src) {
		return () -> new Iterator<T>() {
			@Override
			public boolean hasNext() {
				return src.hasMoreElements();
			}

			@Override
			public T next() {
				return src.nextElement();
			}
		};
	}

	public static <T> Iterable<T> iterable(Supplier<T> src) {
		return () -> new Iterator<T>() {
			private T _nextValue = src.get();

			@Override
			public boolean hasNext() {
				return _nextValue != null;
			}

			@Override
			public T next() {
				T ret = _nextValue;
				_nextValue = src.get();
				return ret;
			}
		};
	}

	public static <T> IQueue<T> wrap(Enumeration<T> src) {
		return wrap(iterable(src));
	}

	public static <T> IQueue<T> wrap(Supplier<T> src) {
		return wrap(iterable(src));
	}

	public static <T> int counter(IQueue<T> src) {
		int count = 0;

		while(src.hasElements()) {
			src.dequeue();
			count++;
		}
		return count;
	}

	public static <T> Supplier<T> supplier(IQueue<T> src) {
		return () -> src.hasElements() ? src.dequeue() : null;
	}

	// XXX
	/*
	public static <T> Supplier<T> supplier(Iterable<T> src) {
		return supplier(wrap(src));
	}
	*/

	public static <T> Supplier<T> supplier(Enumeration<T> src) {
		return supplier(wrap(src));
	}
}
