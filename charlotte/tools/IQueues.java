package charlotte.tools;

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

	public static <T> IQueue<T> wrap(Supplier<T> src) {
		return wrap(iterable(src));
	}
}
