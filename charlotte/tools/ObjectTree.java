package charlotte.tools;

import java.util.Iterator;
import java.util.List;

public class ObjectTree implements Iterable<ObjectTree> {
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

	public String getString() {
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
}