package charlotte.tools;

import java.util.Iterator;

public class IteratorCartridge<T> implements Iterator<T>, Iterable<T> {
	private Iterator<T> _inner;
	private T _defval;
	private T _next;
	private int _remaining;
	private T _current;

	public IteratorCartridge(Iterator<T> inner) {
		this(inner, null);
	}

	public IteratorCartridge(Iterator<T> inner, T defval) {
		_inner = inner;
		_defval = defval;

		if(inner.hasNext()) {
			_next = inner.next();
			_remaining = inner.hasNext() ? 3 : 2;
		}
		else {
			_next = defval;
			_remaining = 1;
		}
		_current = defval;
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
		_current = _next;

		if(_remaining == 3) {
			_next = _inner.next();
			_remaining = _inner.hasNext() ? 3 : 2;
		}
		else if(_remaining == 2) {
			_next = _defval;
			_remaining = 1;
		}
		else if(_remaining == 1) {
			_remaining = 0;
		}
		return _current;
	}

	public T current() {
		return _current;
	}

	public boolean moveNext() {
		if(hasNext()) {
			next();
			return true;
		}
		return false;
	}

	public IteratorCartridge<T> seekNext() {
		moveNext();
		return this;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}
}
