package charlotte.tools;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 構造化オブジェクトをラップするクラス<br> // orig: * \u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u30e9\u30c3\u30d7\u3059\u308b\u30af\u30e9\u30b9<br>
 * 構造化オブジェクトは、以下のいずれかであるものを指す。<br> // orig: * \u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306f\u3001\u4ee5\u4e0b\u306e\u3044\u305a\u308c\u304b\u3067\u3042\u308b\u3082\u306e\u3092\u6307\u3059\u3002<br>
 * 1. 全ての要素が構造化オブジェクトである ObjectList<br> // orig: * 1. \u5168\u3066\u306e\u8981\u7d20\u304c\u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3067\u3042\u308b ObjectList<br>
 * 2. 全ての要素の値が構造化オブジェクトである ObjectMap<br> // orig: * 2. \u5168\u3066\u306e\u8981\u7d20\u306e\u5024\u304c\u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3067\u3042\u308b ObjectMap<br>
 * 3. object<br>
 * 4. null
 *
 */
public class ObjectTree implements Iterable<ObjectTree> {
	/**
	 * 色々なオブジェクトを構造化オブジェクトに変換する。<br> // orig: * \u8272\u3005\u306a\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306b\u5909\u63db\u3059\u308b\u3002<br>
	 * 本メソッドの用途は決まっていない。呼び出し側は処理が変更されることを想定すること。<br> // orig: * \u672c\u30e1\u30bd\u30c3\u30c9\u306e\u7528\u9014\u306f\u6c7a\u307e\u3063\u3066\u3044\u306a\u3044\u3002\u547c\u3073\u51fa\u3057\u5074\u306f\u51e6\u7406\u304c\u5909\u66f4\u3055\u308c\u308b\u3053\u3068\u3092\u60f3\u5b9a\u3059\u308b\u3053\u3068\u3002<br>
	 * 現在のところ、自力で作ったオブジェクトを JsonTools.Encode() に渡せるような構造化オブジェクトに変換することを用途としている。 // orig: * \u73fe\u5728\u306e\u3068\u3053\u308d\u3001\u81ea\u529b\u3067\u4f5c\u3063\u305f\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092 JsonTools.Encode() \u306b\u6e21\u305b\u308b\u3088\u3046\u306a\u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306b\u5909\u63db\u3059\u308b\u3053\u3068\u3092\u7528\u9014\u3068\u3057\u3066\u3044\u308b\u3002
	 * @param root 色々なオブジェクト // orig: * @param root \u8272\u3005\u306a\u30aa\u30d6\u30b8\u30a7\u30af\u30c8
	 * @return 構造化オブジェクト // orig: * @return \u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8
	 */
	public static Object conv(Object root) {
		if(root == null) {
			return null;
		}

		// for JsonTools.encode()
		if(
				ReflectTools.equals(root.getClass(), Boolean.class) ||
				ReflectTools.equals(root.getClass(), Integer.class) ||
				ReflectTools.equals(root.getClass(), Short.class) ||
				ReflectTools.equals(root.getClass(), Long.class) ||
				ReflectTools.equals(root.getClass(), Double.class) ||
				ReflectTools.equals(root.getClass(), Float.class)
				) {
			return new JsonTools.Word(root.toString());
		}

		if(root.getClass().isArray()) {
			ObjectList ol = new ObjectList(Array.getLength(root));

			for(int index = 0; index < Array.getLength(root); index++) {
				ol.add(conv(Array.get(root, index)));
			}
			return ol;
		}
		if(root instanceof List<?>) {
			ObjectList ol = new ObjectList(((List<?>)root).size());

			for(Object element : ((List<?>)root)) {
				ol.add(conv(element));
			}
			return ol;
		}
		if(root instanceof Map<?, ?>) {
			ObjectMap om = ObjectMap.create();

			for(Map.Entry<?, ?> entry : ((Map<?, ?>)root).entrySet()) {
				om.put(entry.getKey(), conv(entry.getValue()));
			}
			return om;
		}
		return root;
	}

	/**
	 * 色々なオブジェクトからインスタンスを生成する。 // orig: * \u8272\u3005\u306a\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u304b\u3089\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3092\u751f\u6210\u3059\u308b\u3002
	 * @param root 色々なオブジェクト // orig: * @param root \u8272\u3005\u306a\u30aa\u30d6\u30b8\u30a7\u30af\u30c8
	 * @return インスタンス // orig: * @return \u30a4\u30f3\u30b9\u30bf\u30f3\u30b9
	 */
	public static ObjectTree convert(Object root) {
		return new ObjectTree(conv(root));
	}

	private Object _root;

	/**
	 * 構造化オブジェクトからインスタンスを生成する。 // orig: * \u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u304b\u3089\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3092\u751f\u6210\u3059\u308b\u3002
	 * @param root 構造化オブジェクト // orig: * @param root \u69cb\u9020\u5316\u30aa\u30d6\u30b8\u30a7\u30af\u30c8
	 */
	public ObjectTree(Object root) {
		_root = root;
	}

	public ObjectTree get(int index) {
		return new ObjectTree(((ObjectList)_root).get(index));
	}

	public ObjectTree get(String path) {
		return get(StringTools.tokenize(path, "/"));
	}

	/*
	public ObjectTree get(Iterator<String> pTkns) {
		return get(ListTools.toList(pTkns));
	}
	*/

	public ObjectTree get(Iterable<String> pTkns) {
		Object node = _root;

		for(String pTkn : pTkns) {
			if(node instanceof ObjectList) {
				node = ((ObjectList)node).get(Integer.parseInt(pTkn));
			}
			else if(node instanceof ObjectMap) {
				node = ((ObjectMap)node).get(pTkn);
			}
			else {
				throw new RTError("pTkn: " + pTkn);
			}
		}
		return new ObjectTree(node);
	}

	public int size() {
		if(_root instanceof ObjectList) {
			return ((ObjectList)_root).size();
		}
		if(_root instanceof ObjectMap) {
			return ((ObjectMap)_root).size();
		}
		throw new RTError("_root: " + _root);
	}

	public List<String> keys() {
		if(_root instanceof ObjectList) {
			return ListTools.select(IntTools.asList(IntTools.sequence(((ObjectList)_root).size())), index -> index.toString());
		}
		if(_root instanceof ObjectMap) {
			return ((ObjectMap)_root).keys();
		}
		throw new RTError("_root: " + _root);
	}

	public String stringValue() {
		return _root.toString();
	}

	@Override
	public String toString() {
		System.out.println("[DEBUG] _root: " + _root); // test

		if(_root == null) {
			return "_root == null";
		}
		return "[DEBUG] " + JsonTools.encode(_root);
	}

	public IArray<ObjectTree> toArray() {
		if(_root instanceof ObjectList) {
			return new IArray<ObjectTree>() {
				@Override
				public int length() {
					return ((ObjectList)_root).size();
				}

				@Override
				public ObjectTree get(int index) {
					return new ObjectTree(((ObjectList)_root).get(index));
				}

				@Override
				public void set(int index, ObjectTree element) {
					throw new RTError("forbidden");
				}
			};
		}

		if(_root instanceof ObjectMap) {
			List<String> keys = ((ObjectMap)_root).keys();

			return new IArray<ObjectTree>() {
				@Override
				public int length() {
					return keys.size();
				}

				@Override
				public ObjectTree get(int index) {
					String key = keys.get(index);

					return new ObjectTree(ObjectList.create(new Object[] { key, ((ObjectMap)_root).get(key) }));
				}

				@Override
				public void set(int index, ObjectTree element) {
					throw new RTError("forbidden");
				}
			};
		}

		throw new RTError("_root: " + _root);
	}

	@Override
	public Iterator<ObjectTree> iterator() {
		return IArrays.asList(toArray()).iterator();
	}

	public boolean isList() {
		return _root instanceof ObjectList;
	}

	public boolean isMap() {
		return _root instanceof ObjectMap;
	}

	public Object direct() {
		return _root;
	}
}
