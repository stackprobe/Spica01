package charlotte.tools;

import java.util.Map;
import java.util.Set;

public class CrossMap<K, V> {
	private Map<K, V> _key_value;
	private Map<V, K> _value_key;

	public CrossMap(Map<K, V> key_value_binding, Map<V, K> value_key_binding) {
		_key_value = key_value_binding;
		_value_key = value_key_binding;
	}

	public void clear() {
		_key_value.clear();
		_value_key.clear();
	}

	public void add(K key, V value) {
		if(_key_value.containsKey(key)) {
			throw new RTError("key duplication");
		}
		if(_value_key.containsKey(value)) {
			throw new RTError("value duplication");
		}
		_key_value.put(key, value);
		_value_key.put(value, key);
	}

	public V get(K key) {
		return _key_value.get(key);
	}

	public K getKey(V value) {
		return _value_key.get(value);
	}

	public Set<K> keys() {
		return _key_value.keySet();
	}

	public Set<V> values() {
		return _value_key.keySet();
	}
}
