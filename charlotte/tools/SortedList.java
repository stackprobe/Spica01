package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class SortedList<T> {
	private List<T> _innerList;
	private Comparator<T> _comp;
	private boolean _sortedFlag;

	public SortedList(List<T> bindingList, Comparator<T> comp) {
		this(bindingList, comp, false);
	}

	public SortedList(List<T> bindingList, Comparator<T> comp, boolean sortedFlag) {
		_innerList = bindingList;
		_comp = comp;
		_sortedFlag = sortedFlag;
	}

	public SortedList(Comparator<T> comp) {
		_innerList = new ArrayList<T>();
		_comp = comp;
		_sortedFlag = true;
	}

	public void clear() {
		_innerList.clear();
		_sortedFlag = true;
	}

	public void add(T element) {
		_innerList.add(element);
		_sortedFlag = false;
	}

	public void addAll(T[] elements) {
		addAll(ArrayTools.iterable(elements));
	}

	public void addAll(Iterable<T> elements) {
		for(T element : elements) {
			_innerList.add(element);
		}
		_sortedFlag = false;
	}

	public int size() {
		return _innerList.size();
	}

	private void beforeAccessElement() {
		if(_sortedFlag == false) {
			_innerList.sort(_comp);
			_sortedFlag = true;
		}
	}

	public T get(int index) {
		beforeAccessElement();
		return _innerList.get(index);
	}

	public List<T> subList() {
		return subList(0);
	}

	public List<T> subList(int index) {
		return subList(index, size());
	}

	public List<T> subList(int fromIndex, int toIndex) {
		beforeAccessElement();
		return _innerList.subList(fromIndex, toIndex);
	}

	public boolean contains(Function<T, Integer> ferret) {
		return indexOf(ferret) != -1;
	}

	public int indexOf(Function<T, Integer> ferret) {
		beforeAccessElement();

		int l = -1;
		int r = _innerList.size();

		while(l + 1 < r) {
			int m =(l + r) / 2;
			int ret = ferret.apply(_innerList.get(m)); // as _comp.compare(_innerList.get(m), target);

			if(ret < 0) {
				l = m;
			}
			else if(0 < ret) {
				r = m;
			}
			else {
				return m;
			}
		}
		return -1; // not found
	}

	public int leftIndexOf(Function<T, Integer> ferret) { // ret: ターゲット以上になる最初の位置、無ければ要素数
		beforeAccessElement();

		int l = 0;
		int r = _innerList.size();

		while(l < r) {
			int m =(l + r) / 2;
			int ret = ferret.apply(_innerList.get(m)); // as _comp.compare(_innerList.get(m), target);

			if(ret < 0) {
				l = m + 1;
			}
			else {
				r = m;
			}
		}
		return l;
	}

	public int rightIndexOf(Function<T, Integer> ferret) { // ret: ターゲット以下になる最後の位置、無ければ(-1)
		return rightIndexOf(ferret, -1);
	}

	public int rightIndexOf(Function<T, Integer> ferret, int l) {
		beforeAccessElement();

		//int l = -1;
		int r = _innerList.size() - 1;

		while(l < r) {
			int m =(l + r + 1) / 2;
			int ret = ferret.apply(_innerList.get(m)); // as _comp.compare(_innerList.get(m), target);

			if(0 < ret) {
				r = m - 1;
			}
			else {
				l = m;
			}
		}
		return r;
	}

	public List<T> getMatch(Function<T, Integer> ferret) {
		int l = leftIndexOf(ferret);
		int r = rightIndexOf(ferret, l - 1) + 1;

		return subList(l, r);
	}

	public Function<T, Integer> getFerret(T target) {
		return value -> _comp.compare(value, target);
	}

	public Function<T, Integer> getFerret(T mintrg, T maxtrg) {
		return value -> {
			if(_comp.compare(value, mintrg) < 0) {
				return -1;
			}
			if(_comp.compare(value, maxtrg) > 0) {
				return 1;
			}
			return 0;
		};
	}
}
