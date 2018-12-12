package charlotte.tools;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ObjectMap {
	public static ObjectMap create() {
		return new ObjectMap(MapTools.<Object>createOrdered());
	}

	public static ObjectMap createIgnoreCase() {
		return new ObjectMap(MapTools.<Object>createOrderedIgnoreCase());
	}

	private OrderedMap<String, Object> _inner;

	private ObjectMap(OrderedMap<String, Object> inner) {
		_inner = inner;
	}

	public void putAll(Map<Object, Object> map) {
		for(Map.Entry<Object, Object> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public void put(Object key, Object value) {
		_inner.put("" + key, value);
	}

	public int size() {
		return _inner.size();
	}

	public Object get(Object key) {
		return _inner.get("" + key);
	}

	public List<String> keys() {
		return _inner.keys();
	}

	public Collection<Object> values() {
		return _inner.values();
	}

	public OrderedMap<String, Object> direct() {
		return _inner;
	}
}
