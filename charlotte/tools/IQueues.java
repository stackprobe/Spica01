package charlotte.tools;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * IQueue など、一度しか列挙できないものは Iterator // orig: * IQueue \u306a\u3069\u3001\u4e00\u5ea6\u3057\u304b\u5217\u6319\u3067\u304d\u306a\u3044\u3082\u306e\u306f Iterator
 * List など、何度も列挙できるものは Iterable // orig: * List \u306a\u3069\u3001\u4f55\u5ea6\u3082\u5217\u6319\u3067\u304d\u308b\u3082\u306e\u306f Iterable
 *
 * Iterator を Iterable にしたい時は IteratorTools.once() // orig: * Iterator \u3092 Iterable \u306b\u3057\u305f\u3044\u6642\u306f IteratorTools.once()
 *
 */
public class IQueues {
	public static <T> IQueue<T> wrap(Iterator<T> iterator) {
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

	public static <T> Iterator<T> iterator(IQueue<T> src) {
		return new Iterator<T>() {
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

	public static <T> Iterator<T> iterator(IStack<T> src) {
		return new Iterator<T>() {
			@Override
			public boolean hasNext() {
				return src.hasElements();
			}

			@Override
			public T next() {
				return src.pop();
			}
		};
	}

	public static <T> Iterator<T> iterator(Enumeration<T> src) {
		return new Iterator<T>() {
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

	public static <T> Iterator<T> iterator(Supplier<T> src) {
		return IEnumerators.iterator(new IEnumerator<T>() {
			private T _current;

			@Override
			public boolean moveNext() {
				return (_current = src.get()) != null;
			}

			@Override
			public T current() {
				return _current;
			}
		});
	}

	public static <T> Iterator<T> endless(Supplier<T> src) {
		return new Iterator<T>() {
			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public T next() {
				return src.get();
			}
		};
	}

	public static <T> Iterator<T> endless(T element) {
		return endless(() -> element);
	}

	public static <T> IQueue<T> wrap(Enumeration<T> src) {
		return wrap(iterator(src));
	}

	public static <T> IQueue<T> wrap(Supplier<T> src) {
		return wrap(iterator(src));
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

	// HACK
	/*
	public static <T> Supplier<T> supplier(Iterator<T> src) {
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
	 * @param destOnly1 null ok
	 * @param destBoth1 null ok
	 * @param destBoth2 null ok
	 * @param destOnly2 null ok
	 * @param comp
	 */
	public static <T> void merge(IQueue<T> queue1, IQueue<T> queue2, IQueue<T> destOnly1, IQueue<T> destBoth1, IQueue<T> destBoth2, IQueue<T> destOnly2, Comparator<T> comp) {
		IEnumerators.Cartridge<T> reader1 = IEnumerators.getCartridge(IQueues.iterator(queue1));
		IEnumerators.Cartridge<T> reader2 = IEnumerators.getCartridge(IQueues.iterator(queue2));

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

		reader1.moveNext();
		reader2.moveNext();

		for(; ; ) {
			int ret;

			if(reader1.hasCurrent() == false) {
				if(reader2.hasCurrent() == false) {
					break;
				}
				ret = 1;
			}
			else if(reader2.hasCurrent() == false) {
				ret = -1;
			}
			else {
				ret = comp.compare(reader1.current(), reader2.current());
			}

			if(ret < 0) {
				destOnly1.enqueue(reader1.current());
				reader1.moveNext();
			}
			else if(0 < ret) {
				destOnly2.enqueue(reader2.current());
				reader2.moveNext();
			}
			else {
				destBoth1.enqueue(reader1.current());
				destBoth2.enqueue(reader2.current());
				reader1.moveNext();
				reader2.moveNext();
			}
		}
	}

	public static <T> void collectMergedPairs(IQueue<T> queue1, IQueue<T> queue2, IQueue<PairUnit<T, T>> dest, T defval, Comparator<T> comp) {
		IEnumerators.Cartridge<T> reader1 = IEnumerators.getCartridge(IQueues.iterator(queue1));
		IEnumerators.Cartridge<T> reader2 = IEnumerators.getCartridge(IQueues.iterator(queue2));

		reader1.moveNext();
		reader2.moveNext();

		for(; ; ) {
			int ret;

			if(reader1.hasCurrent() == false) {
				if(reader2.hasCurrent() == false) {
					break;
				}
				ret = 1;
			}
			else if(reader2.hasCurrent() == false) {
				ret = -1;
			}
			else {
				ret = comp.compare(reader1.current(), reader2.current());
			}

			if(ret < 0) {
				dest.enqueue(new PairUnit<T, T>(reader1.current(), defval));
				reader1.moveNext();
			}
			else if(0 < ret) {
				dest.enqueue(new PairUnit<T, T>(defval, reader2.current()));
				reader2.moveNext();
			}
			else {
				dest.enqueue(new PairUnit<T, T>(reader1.current(), reader2.current()));
				reader1.moveNext();
				reader2.moveNext();
			}
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
