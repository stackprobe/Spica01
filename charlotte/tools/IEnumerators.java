package charlotte.tools;

import java.util.Iterator;

public class IEnumerators {
	public static <T> Iterable<T> iterable(IEnumerator<T> src) {
		return () -> new Iterator<T>() {
			private int _status = 2;

			@Override
			public boolean hasNext() {
				if(_status == 2) {
					_status = src.moveNext() ? 1 : 0;
				}
				return _status == 1;
			}

			@Override
			public T next() {
				if(hasNext() == false) {
					throw new RTError("No more elements.");
				}
				_status = 2;
				return src.current();
			}
		};
	}

	public static <T> IEnumerator<T> enumerator(Iterator<T> src) {
		return new IEnumerator<T>() {
			private int _status = 2;
			private T _current;

			@Override
			public boolean moveNext() {
				if(_status == 0) {
					throw new RTError("No more elements.");
				}
				if(src.hasNext()) {
					_status = 1;
					_current = src.next();
				}
				else {
					_status = 0;
					_current = null;
				}
				return _status != 0;
			}

			@Override
			public T current() {
				if(_status != 1) {
					throw new RTError("Bad status: " + _status);
				}
				return _current;
			}
		};
	}

	public static class Cartridge<T> implements IEnumerator<T> {
		private IEnumerator<T> _inner;
		private boolean _hasCurrent = false;

		public Cartridge(IEnumerator<T> inner) {
			_inner = inner;
		}

		@Override
		public boolean moveNext() {
			return _hasCurrent = _inner.moveNext();
		}

		public boolean hasCurrent() {
			return _hasCurrent;
		}

		@Override
		public T current() {
			return _inner.current();
		}
	}

	public static <T> Cartridge<T> getCartridge(IEnumerator<T> enumerator) {
		return new Cartridge<T>(enumerator);
	}

	public static <T> Cartridge<T> getCartridge(Iterator<T> iterator) {
		return getCartridge(enumerator(iterator));
	}
}
