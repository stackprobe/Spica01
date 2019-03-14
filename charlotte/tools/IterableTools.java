package charlotte.tools;

import java.util.ArrayList;
import java.util.Iterator;

public class IterableTools {
	public static <T> Iterable<T> linearize(Iterable<Iterable<T>> src) {
		return () -> new Iterator<T>() {
			private Iterator<T> _vehicle = new ArrayList<T>(0).iterator();
			private Iterator<Iterable<T>> _train = src.iterator();

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
