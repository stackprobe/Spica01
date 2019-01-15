package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class ObjectList {
	//public final static ObjectList EMPTY = new ObjectList();

	/*
	public static ObjectList one(Object element) {
		ObjectList ret = new ObjectList();
		ret.add(element);
		return ret;
	}
	*/

	/*
	public static ObjectList lot(Object... elements) {
		ObjectList ret = new ObjectList();
		ret.addAll(elements);
		return ret;
	}
	*/

	public static ObjectList create(Object[] arr) {
		ObjectList ret = new ObjectList();
		ret.addAll(arr);
		return ret;
	}

	public static ObjectList create(List<?> list) {
		ObjectList ret = new ObjectList();
		ret.addAll(list);
		return ret;
	}

	private List<Object> _inner = new ArrayList<Object>();

	public void addAll(Object[] arr) {
		for(Object element : arr) {
			add(element);
		}
	}

	public void addAll(List<?> list) {
		for(Object element : list) {
			add(element);
		}
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
