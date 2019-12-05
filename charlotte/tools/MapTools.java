package charlotte.tools;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

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

					return VariantTools.equals(key, p.getKey()) && VariantTools.equals(value, p.getValue());
				}
				return false;
			}

			@Override
			public int hashCode() {
				return VariantTools.hashCode(key) ^ VariantTools.hashCode(value);
			}
		};
	}

	public static <V> CacheMap<String, V> createCache(Function<String, V> createValue) {
		return new CacheMap<String, V>(create(), createValue);
	}

	public static <V> CacheMap<String, V> createCacheIgnoreCase(Function<String, V> createValue) {
		return new CacheMap<String, V>(createIgnoreCase(), createValue);
	}

	public static CrossMap<String, String> createCross() {
		return new CrossMap<String, String>(
				create(),
				create()
				);
	}

	public static CrossMap<String, String> createCrossIgnoreCase() {
		return new CrossMap<String, String>(
				createIgnoreCase(),
				createIgnoreCase()
				);
	}
}
