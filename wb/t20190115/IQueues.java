package wb.t20190115;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import charlotte.tools.IQueue;
import charlotte.tools.RTError;

public class IQueues {
	public static <T> IQueue<T> wrap(Iterable<T> src) {
		return new IQueue<T>() {
			Iterator<T> _iterator = null;

			private Iterator<T> iterator() {
				if(_iterator == null) {
					_iterator = src.iterator();
				}
				return _iterator;
			}

			@Override
			public boolean hasElements() {
				return iterator().hasNext();
			}

			@Override
			public void enqueue(T element) {
				throw new RTError("forbidden");
			}

			@Override
			public T dequeue() {
				return iterator().next();
			}
		};
	}

	public static <T> IQueue<T> wrap(Supplier<T> src) {
		return wrap(iterable(src));
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

	public static <T> Supplier<T> supplier(IQueue<T> src) {
		return () -> src.hasElements() ? src.dequeue() : null;
	}

	public static <T> Supplier<T> supplier(Iterable<T> src) {
		return supplier(wrap(src));
	}

	public static <T> Consumer<T> consumer(IQueue<T> dest) {
		return element -> dest.enqueue(element);
	}

	public static <T> IQueue<T> wrap(Consumer<T> dest) {
		return new IQueue<T>() {
			@Override
			public boolean hasElements() {
				throw new RTError("forbidden");
			}

			@Override
			public void enqueue(T element) {
				dest.accept(element);
			}

			@Override
			public T dequeue() {
				throw new RTError("forbidden");
			}
		};
	}

	public static <T> IQueue<T> combine(IQueue<T> src, IQueue<T> dest) {
		return new IQueue<T>() {
			@Override
			public boolean hasElements() {
				return src.hasElements();
			}

			@Override
			public void enqueue(T element) {
				dest.enqueue(element);
			}

			@Override
			public T dequeue() {
				return src.dequeue();
			}
		};
	}

	public static <T> IQueue<T> combine(Iterable<T> src, Consumer<T> dest) {
		return combine(wrap(src), wrap(dest));
	}

	public static <T> IQueue<T> combine(Supplier<T> src, Consumer<T> dest) {
		return combine(wrap(src), wrap(dest));
	}
}
