package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class ObjectList {
	public static ObjectList create(Object[] arr) {
		ObjectList ret = new ObjectList();
		ret.addRange(arr);
		return ret;
	}

	private List<Object> _inner = new ArrayList<Object>();

	public ObjectList() {
		// noop
	}

	public ObjectList(Object... arr) {
		addRange(arr);
	}

	public void addRange(Object[] arr) {
		_inner.add(arr);
	}

	public void add(Object element) {
		_inner.add(element);
	}

	public int size() {
		return _inner.size();
	}

	public Object get(int index) {
		return _inner.get(index);
	}

	public void set(int index, Object element) {
		_inner.set(index, element);
	}

	public List<Object> direct() {
		return _inner;
	}
}
