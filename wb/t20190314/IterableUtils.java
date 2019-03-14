package wb.t20190314;

import java.util.ArrayList;
import java.util.Iterator;

public class IterableUtils {
	public static <T> Iterable<T> linearize(Iterable<Iterable<T>> src) {
		return iterable(new IEnumerator<T>() {
			private Iterator<T> _vehicle = new ArrayList<T>(0).iterator();
			private Iterator<Iterable<T>> _train = src.iterator();
			private T _current;

			@Override
			public boolean moveNext() {
				while(_vehicle.hasNext() == false) {
					if(_train.hasNext() == false) {
						_current = null;
						return false;
					}
					_vehicle = _train.next().iterator();
				}
				_current = _vehicle.next();
				return true;
			}

			@Override
			public T current() {
				return _current;
			}
		});
	}

	public static <T> Iterable<T> iterable(IEnumerator<T> src) {
		return () -> new Iterator<T>() {
			private boolean _hasCurrent = src.moveNext();

			@Override
			public boolean hasNext() {
				return _hasCurrent;
			}

			@Override
			public T next() {
				T ret = src.current();
				_hasCurrent = src.moveNext();
				return ret;
			}
		};
	}
}
