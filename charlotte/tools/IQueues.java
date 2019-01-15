package charlotte.tools;

import java.util.Iterator;

public class IQueues {
	public static <T> IQueue<T> create(Iterable<T> src) {
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
}
