package charlotte.tools;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * like a LinkedHashMap ???
 *
 */
public class OrderedMap<K, V> implements Map<K, V> {
	public class ValueInfo {
		public V value;
		public long index;
		public K key;
	}

	private V toValue(ValueInfo info) {
		if(info == null) {
			return null;
		}
		return info.value;
	}

	private Map<K, ValueInfo> _inner;
	private long _counter = 0L;

	public OrderedMap(Comparator<K> comp) {
		 _inner = new TreeMap<K, ValueInfo>(comp);
	}

	@Override
	public int size() {
		return _inner.size();
	}

	@Override
	public boolean isEmpty() {
		return _inner.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return _inner.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return ListTools.any(_inner.values(), info -> VariantTools.equals(toValue(info), value));
	}

	@Override
	public V get(Object key) {
		return toValue(_inner.get(key));
	}

	@Override
	public V put(K key, V value) {
		ValueInfo info = new ValueInfo();

		info.value = value;
		info.index = _counter++;
		info.key = key;

		return toValue(_inner.put(info.key, info));
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(Map.Entry<? extends K, ? extends V> pair : m.entrySet()) {
			put(pair.getKey(), pair.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		return toValue(_inner.remove(key));
	}

	@Override
	public void clear() {
		_inner.clear();
	}

	@Override
	public Set<K> keySet() {
		return _inner.keySet();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return new HashSet<Entry<K, V>>(ListTools.select(_inner.values(), info -> MapTools.toEntry(info.key, info.value)));
	}

	private List<ValueInfo> infos() {
		List<ValueInfo> infos = ListTools.toList(_inner.values());

		infos.sort((a, b) -> LongTools.comp.compare(a.index, b.index));

		return infos;
	}

	public List<PairUnit<K, V>> pairs() {
		return ListTools.select(infos(), info -> new PairUnit<K, V>(info.key, info.value));
	}

	public List<Entry<K, V>> entries() {
		return ListTools.select(infos(), info -> MapTools.toEntry(info.key, info.value));
	}

	public List<K> keys() {
		return ListTools.select(infos(), info -> info.key);
	}

	@Override
	public Collection<V> values() {
		return ListTools.select(infos(), info -> info.value);
	}
}
