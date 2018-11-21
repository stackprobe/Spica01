package charlotte.tools;

import java.util.List;
import java.util.Map;

public class ObjectMap {
	public static class ValueInfo {
		public Object value;
		public long index;
		public String key;
	}

	private Map<String, ValueInfo> _inner;
	private long _counter = 0L;

	public static ObjectMap create() {
		return new ObjectMap(MapTools.<ValueInfo>create());
	}

	public static ObjectMap createIgnoreCase() {
		return new ObjectMap(MapTools.<ValueInfo>createIgnoreCase());
	}

	private ObjectMap(Map<String, ValueInfo> inner) {
		_inner = inner;
	}

	public void putAll(Map<Object, Object> map) {
		for(Map.Entry<Object, Object> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public void put(Object key, Object value) {
		ValueInfo info = new ValueInfo();

		info.value = value;
		info.index = _counter++;
		info.key = "" + key;

		_inner.put(info.key, info);
	}

	public int size() {
		return _inner.size();
	}

	public Object get(Object key) {
		ValueInfo info = _inner.get("" + key);

		if(info == null) {
			return null;
		}
		return info.value;
	}

	public List<String> keys() {
		List<ValueInfo> values = ListTools.toList(_inner.values());

		values.sort((a, b) -> LongTools.comp.compare(a.index, b.index));

		return ListTools.select(values, value -> value.key);
	}

	public List<Object> values() {
		return ListTools.select(_inner.values(), value -> value.value);
	}

	public Map<String, ValueInfo> direct() {
		return _inner;
	}
}
