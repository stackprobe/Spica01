package charlotte.tools;

import java.util.Iterator;

public class IteratorCartridge<T> implements Iterator<T>, Iterable<T> {
	private Iterator<T> _inner;
	private T _defval;
	private T _current;
	private int _remaining;

	public IteratorCartridge(Iterator<T> inner) {
		this(inner, null);
	}

	public IteratorCartridge(Iterator<T> inner, T defval) {
		_inner = inner;
		_defval = defval;
		_current = defval;
		_remaining = inner.hasNext() ? 2 : 1;
	}

	private void forward() {
		if(_remaining == 2) {
			_current = _inner.next();

			if(_inner.hasNext() == false) {
				_remaining = 1;
			}
		}
		else if(_remaining == 1) {
			_current = _defval;
			_remaining = 0;
		}
	}

	@Override
	public boolean hasNext() {
		return 2 <= _remaining;
	}

	public boolean hasCurrent() {
		return 1 <= _remaining;
	}

	@Override
	public T next() {
		forward();
		return _current;
	}

	public T current() {
		return _current;
	}

	public boolean moveNext() {
		forward();
		return hasCurrent();
	}

	public IteratorCartridge<T> seek() {
		return seek(1);
	}

	public IteratorCartridge<T> seek(int count) {
		for(; 1 <= count; count--) {
			forward();
		}
		return this;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}
}
