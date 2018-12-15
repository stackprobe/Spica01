package charlotte.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterableTrain<T> implements Iterable<T> {
	private List<Iterable<T>> _iterables = new ArrayList<Iterable<T>>();

	public IterableTrain<T> addOne(T element) {
		return add(ListTools.one(element));
	}

	public IterableTrain<T> add(Iterable<T> iterable) {
		_iterables.add(iterable);
		return this;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Iterator<T> _curr = new ArrayList<T>(0).iterator();
			private Iterator<Iterable<T>> _trails = _iterables.iterator();

			@Override
			public boolean hasNext() {
				return _curr.hasNext() || _trails.hasNext();
			}

			@Override
			public T next() {
				while(_curr.hasNext() == false) {
					_curr = _trails.next().iterator();
				}
				return _curr.next();
			}
		};
	}
}