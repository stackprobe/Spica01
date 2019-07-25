package charlotte.tools;

import java.util.Map;
import java.util.function.Function;

public class CacheMap<K, V> {
	private Map<K, V> _inner;
	private Function<K, V> _createValue;

	public CacheMap(Map<K, V> inner, Function<K, V> createValue) {
		_inner = inner;
		_createValue = createValue;
	}

	public V get(K key) {
		V value = _inner.get(key);

		if(value == null) {
			value = _createValue.apply(key);
			_inner.put(key, value);
		}
		return value;
	}

	public int size() {
		return _inner.size();
	}

	public void clear() {
		_inner.clear();
	}

	public void remove(K key) {
		_inner.remove(key);
	}

	public Iterable<K> keys() {
		return _inner.keySet();
	}

	public Iterable<V> values() {
		return _inner.values();
	}
}
