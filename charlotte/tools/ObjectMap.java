package charlotte.tools;

import java.util.Map;
import java.util.Set;

public class ObjectMap {
	private Map<String, Object> _inner;

	public static ObjectMap create() {
		return new ObjectMap(MapTools.create());
	}

	public static ObjectMap CreateIgnoreCase() {
		return new ObjectMap(MapTools.createIgnoreCase());
	}

	private ObjectMap(Map<String, Object> bindingMap) {
		_inner = bindingMap;
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

	public Set<String> getKeys() {
		return _inner.keySet();
	}

	public Map<String, Object> direct() {
		return _inner;
	}
}
