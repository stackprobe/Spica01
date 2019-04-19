package wb.t20190314;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

import charlotte.tools.IEnumerator;
import charlotte.tools.RTError;

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
			private int _ready = 2;

			@Override
			public boolean hasNext() {
				if(_ready == 2) {
					_ready = src.moveNext() ? 1 : 0;
				}
				return _ready == 1;
			}

			@Override
			public T next() {
				if(hasNext() == false) {
					throw new RTError("No more elements.");
				}
				_ready = 2;
				return src.current();
			}
		};
	}

	// old
	/*
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
	*/

	public static <T> Iterable<T> linearize_v2(Iterable<Iterable<T>> src) { // old
		return () -> new Iterator<T>() {
			private Iterator<T> _vehicle = new ArrayList<T>(0).iterator();
			private Iterator<Iterable<T>> _train = src.iterator();

			{
				moveNext();
			}

			private boolean _hasNext = true;
			private T _next;

			private void moveNext() {
				while(_vehicle.hasNext() == false) {
					if(_train.hasNext() == false) {
						_hasNext = false;
						_next = null;
						return;
					}
					_vehicle = _train.next().iterator();
				}
				_next = _vehicle.next();
			}

			@Override
			public boolean hasNext() {
				return _hasNext;
			}

			@Override
			public T next() {
				T ret = _next;
				moveNext();
				return ret;
			}
		};
	}

	public static <T> Iterator<T> where(Iterator<T> src, Predicate<T> match) { // old
		return new Iterator<T>() {
			private boolean _hasNext = true;
			private T _next;

			{
				moveNext();
			}

			private void moveNext() {
				while(src.hasNext()) {
					_next = src.next();

					if(match.test(_next)) {
						return;
					}
				}
				_hasNext = false;
			}

			@Override
			public boolean hasNext() {
				return _hasNext;
			}

			@Override
			public T next() {
				T ret = _next;
				moveNext();
				return ret;
			}
		};
	}

	public static abstract class Enumerable<T> implements Iterator<T> { // old
		private int _status = 2;
		private T _next;

		protected void setNext(T element) {
			if(_status == 1) {
				throw null; // never
			}
			_status = 1;
			_next = element;
		}

		protected abstract void moveNext();

		@Override
		public boolean hasNext() {
			if(_status == 2) {
				_status = 0;
				moveNext();
			}
			return _status == 1;
		}

		@Override
		public T next() {
			if(hasNext() == false) {
				throw new RTError("No more elements.");
			}
			T ret = _next;
			_status = 2;
			_next = null;
			return ret;
		}
	}
}
