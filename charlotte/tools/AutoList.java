package charlotte.tools;

import java.util.ArrayList;

public class AutoList<T> {
	private ArrayList<T> _buffer;

	public AutoList(int capacity) {
		_buffer = new ArrayList<T>(capacity);
	}

	public void ensureCapacity(int capacity) {
		_buffer.ensureCapacity(capacity);
	}

	public T get(int index) {
		return get(index, null);
	}

	public T get(int index, T defval) {
		return index < _buffer.size() ? _buffer.get(index) : defval;
	}

	public void set(int index, T value) {
		ensureCapacity(index + 1);
		_buffer.set(index, value);
	}
}
