package charlotte.tools;

import java.util.Map;
import java.util.TreeMap;

public class MapTools {
	public static <V> Map<String, V> create() {
		return new TreeMap<String, V>(StringTools.comp);
	}

	public static <V> Map<String, V> createIgnoreCase() {
		return new TreeMap<String, V>(StringTools.compIgnoreCase);
	}

	/*
	public static <V> Map<Integer, V> createInt() {
		return new TreeMap<Integer, V>(IntTools.comp);
	}
	*/

	/*
	public static <V> Map<Short, V> createShort() {
		return new TreeMap<Short, V>(ShortTools.comp);
	}
	*/

	/*
	public static <V> Map<Character, V> createChar() {
		return new TreeMap<Character, V>(CharTools.comp);
	}
	*/

	/*
	public static <V> Map<Long, V> createLong() {
		return new TreeMap<Long, V>(LongTools.comp);
	}
	*/

	public static <V> OrderedMap<String, V> createOrdered() {
		return new OrderedMap<String, V>(StringTools.comp);
	}

	public static <V> OrderedMap<String, V> createOrderedIgnoreCase() {
		return new OrderedMap<String, V>(StringTools.compIgnoreCase);
	}

	public static <K, V> Map.Entry<K, V> toEntry(K key, V value) {
		return new Map.Entry<K, V>() {
			@Override
			public K getKey() {
				return key;
			}

			@Override
			public V getValue() {
				return value;
			}

			@Override
			public V setValue(V value) {
				throw new RTError("forbidden");
			}

			@Override
			public boolean equals(Object o) {
				if(o instanceof Map.Entry<?, ?>) {
					Map.Entry<?, ?> p = (Map.Entry<?, ?>)o;

					return ObjectTools.equals(key, p.getKey()) && ObjectTools.equals(value, p.getValue());
				}
				return false;
			}

			@Override
			public int hashCode() {
				return ObjectTools.hashCode(key) ^ ObjectTools.hashCode(value);
			}
		};
	}
}
