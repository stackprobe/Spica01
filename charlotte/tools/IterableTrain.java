package charlotte.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *	TODO
 *	廃止予定　廃止予定　廃止予定　廃止予定　廃止予定
 *	廃止予定　廃止予定　廃止予定　廃止予定　廃止予定
 *	廃止予定　廃止予定　廃止予定　廃止予定　廃止予定
 *	廃止予定　廃止予定　廃止予定　廃止予定　廃止予定
 *	廃止予定　廃止予定　廃止予定　廃止予定　廃止予定
 *
 */
public class IterableTrain<T> implements Iterable<T> {
	private List<Iterable<T>> _iterables = new ArrayList<Iterable<T>>();

	public IterableTrain<T> addOne(T element) {
		return add(ListTools.one(element));
	}

	/*
	@SuppressWarnings("unchecked")
	public IterableTrain<T> addLot(T... elements) {
		return add(IArrays.asList(elements));
	}
	*/

	public IterableTrain<T> add(Iterable<T> iterable) {
		_iterables.add(iterable);
		return this;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Iterator<T> _vehicle = new ArrayList<T>(0).iterator();
			private Iterator<Iterable<T>> _train = _iterables.iterator();

			public Iterator<T> init() {
				moveNext();
				return this;
			}

			private boolean _hasCurrent = true;
			private T _current;

			private void moveNext() {
				while(_vehicle.hasNext() == false) {
					if(_train.hasNext() == false) {
						_hasCurrent = false;
						_current = null;
						return;
					}
					_vehicle = _train.next().iterator();
				}
				_current = _vehicle.next();
			}

			@Override
			public boolean hasNext() {
				return _hasCurrent;
			}

			@Override
			public T next() {
				T ret = _current;
				moveNext();
				return ret;
			}
		}
		.init();
	}
}
