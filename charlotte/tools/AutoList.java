package charlotte.tools;

import java.util.ArrayList;

public class AutoList<T> {
	private ArrayList<T> _buffer;

	public AutoList(int capacity) {
		_buffer = new ArrayList<T>(capacity);
	}

	public void ensureCapacity(int capacity) {
		while(_buffer.size() < capacity) {
			_buffer.add(null);
		}
		//_buffer.ensureCapacity(capacity); // 使えなくなったっぽい。@ 2020.5.28 // orig: //_buffer.ensureCapacity(capacity); // \u4f7f\u3048\u306a\u304f\u306a\u3063\u305f\u3063\u307d\u3044\u3002@ 2020.5.28
	}

	public int getCapacity() {
		return _buffer.size();
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
