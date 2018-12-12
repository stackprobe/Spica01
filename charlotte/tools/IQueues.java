package charlotte.tools;

import java.util.Iterator;

public class IQueues {
	public static <T> IQueue<T> wrap(Iterable<T> src) {
		final Iterator<T> iterator = src.iterator();

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

	/*
	public static <T> T merge(IQueue<T> queue, BinaryOperator<T> conv) {
		for(; ; ) {
			T element = queue.dequeue();

			if(queue.hasElements() == false) {
				return element;
			}
			queue.enqueue(conv.apply(element, queue.dequeue()));
		}
	}
	*/

	/*
	public static <T> IQueue<T> wrapHasElementsForbidden(IQueue<T> inner, Predicate<T> isFairElement) {
		return new IQueue<T>() {
			private T _nextElement = inner.dequeue();

			@Override
			public boolean hasElements() {
				return isFairElement.test(_nextElement);
			}

			@Override
			public void enqueue(T element) {
				inner.enqueue(element);
			}

			@Override
			public T dequeue() {
				T ret = _nextElement;
				_nextElement = inner.dequeue();
				return ret;
			}
		};
	}
	*/

	/*
	public static <T> void mergeSort(IQueue<T> src1, IQueue<T> src2, IQueue<T> dest, Comparator<T> comp) {
		merge(src1, src2, dest, dest, dest, dest, comp);
	}
	*/

	/*
	public static <T> void merge(IQueue<T> src1, IQueue<T> src2, IQueue<T> destOnly1, IQueue<T> destBoth1, IQueue<T> destBoth2, IQueue<T> destOnly2, Comparator<T> comp) {
		int r = 3;
		T element1 = null;
		T element2 = null;

		while(src1.hasElements() && src2.hasElements()) {
			if((r & 1) != 0) {
				element1 = src1.dequeue();
			}
			if((r & 2) != 0) {
				element2 = src2.dequeue();
			}
			int ret = comp.compare(element1, element2);

			if(ret < 0) {
				destOnly1.enqueue(element1);
				r = 1;
			}
			else if(0 < ret) {
				destOnly2.enqueue(element2);
				r = 2;
			}
			else {
				destBoth1.enqueue(element1);
				destBoth2.enqueue(element2);
				r = 3;
			}
		}
		while(src1.hasElements()) {
			destOnly1.enqueue(src1.dequeue());
		}
		while(src2.hasElements()) {
			destOnly2.enqueue(src2.dequeue());
		}
	}
	*/
}
