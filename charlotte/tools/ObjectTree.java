package charlotte.tools;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjectTree implements Iterable<ObjectTree> {
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

	public static ObjectTree convert(Object root) {
		return new ObjectTree(conv(root));
	}

	private Object _root;

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
