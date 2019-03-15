package charlotte.tools;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Consumer;
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

	/**
	 *
	 * @param queue1
	 * @param queue2
	 * @param destOnly1 null可
	 * @param destBoth1 null可
	 * @param destBoth2 null可
	 * @param destOnly2 null可
	 * @param comp
	 */
	public static <T> void merge(IQueue<T> queue1, IQueue<T> queue2, IQueue<T> destOnly1, IQueue<T> destBoth1, IQueue<T> destBoth2, IQueue<T> destOnly2, Comparator<T> comp) {
		IteratorCartridge<T> reader1 = new IteratorCartridge<T>(IQueues.iterable(queue1).iterator()).seek();
		IteratorCartridge<T> reader2 = new IteratorCartridge<T>(IQueues.iterable(queue2).iterator()).seek();

		if(destOnly1 == null) {
			destOnly1 = IQueues.wrap(v -> { });
		}
		if(destBoth1 == null) {
			destBoth1 = IQueues.wrap(v -> { });
		}
		if(destBoth2 == null) {
			destBoth2 = IQueues.wrap(v -> { });
		}
		if(destOnly2 == null) {
			destOnly2 = IQueues.wrap(v -> { });
		}

		while(reader1.hasCurrent() && reader2.hasCurrent()) {
			int ret = comp.compare(reader1.current(), reader2.current());

			if(ret < 0) {
				destOnly1.enqueue(reader1.current());
				reader1.next();
			}
			else if(0 < ret) {
				destOnly2.enqueue(reader2.current());
				reader2.next();
			}
			else {
				destBoth1.enqueue(reader1.current());
				destBoth2.enqueue(reader2.current());
				reader1.next();
				reader2.next();
			}
		}
		while(reader1.hasCurrent()) {
			destOnly1.enqueue(reader1.current());
			reader1.next();
		}
		while(reader2.hasCurrent()) {
			destOnly2.enqueue(reader2.current());
			reader2.next();
		}
	}

	public static <T> void collectMergedPairs(IQueue<T> queue1, IQueue<T> queue2, IQueue<PairUnit<T, T>> dest, T defval, Comparator<T> comp) {
		IteratorCartridge<T> reader1 = new IteratorCartridge<T>(IQueues.iterable(queue1).iterator()).seek();
		IteratorCartridge<T> reader2 = new IteratorCartridge<T>(IQueues.iterable(queue2).iterator()).seek();

		while(reader1.hasCurrent() && reader2.hasCurrent()) {
			int ret = comp.compare(reader1.current(), reader2.current());

			if(ret < 0) {
				dest.enqueue(new PairUnit<T, T>(reader1.current(), defval));
				reader1.next();
			}
			else if(0 < ret) {
				dest.enqueue(new PairUnit<T, T>(defval, reader2.current()));
				reader2.next();
			}
			else {
				dest.enqueue(new PairUnit<T, T>(reader1.current(), reader2.current()));
				reader1.next();
				reader2.next();
			}
		}
		while(reader1.hasCurrent()) {
			dest.enqueue(new PairUnit<T, T>(reader1.current(), defval));
			reader1.next();
		}
		while(reader2.hasCurrent()) {
			dest.enqueue(new PairUnit<T, T>(defval, reader2.current()));
			reader2.next();
		}
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
}
