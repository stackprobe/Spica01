package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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

	public void addAll(Iterator<T> elements) {
		addAll(IteratorTools.once(elements));
	}

	public void addAll(Iterable<T> elements) {
		for(T element : elements) {
			_innerList.add(element);
		}
		_sortedFlag = false;
	}

	public void addLargestEver(T element) {
		_innerList.add(element);
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

	public int leftIndexOf(Function<T, Integer> ferret) { // ret: target 以上になる最初の位置。無ければ要素数 // orig: public int leftIndexOf(Function<T, Integer> ferret) { // ret: target \u4ee5\u4e0a\u306b\u306a\u308b\u6700\u521d\u306e\u4f4d\u7f6e\u3002\u7121\u3051\u308c\u3070\u8981\u7d20\u6570
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

	public int rightIndexOf(Function<T, Integer> ferret) { // ret: target 以下になる最後の位置。無ければ -1 // orig: public int rightIndexOf(Function<T, Integer> ferret) { // ret: target \u4ee5\u4e0b\u306b\u306a\u308b\u6700\u5f8c\u306e\u4f4d\u7f6e\u3002\u7121\u3051\u308c\u3070 -1
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

	public List<T> getMatchWithEdge(Function<T, Integer> ferret) {
		int l = leftIndexOf(ferret);
		int r = rightIndexOf(ferret, l - 1) + 1;
		boolean ld = false;
		boolean rd = false;

		if (1 <= l) {
			l--;
		}
		else {
			ld = true;
		}
		if(r < this.size()) {
			r++;
		}
		else {
			rd = true;
		}
		List<T> dest = new ArrayList<T>();

		if(ld) {
			dest.add(null);
		}
		dest.addAll(subList(l, r));

		if(rd) {
			dest.add(null);
		}
		return dest;
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
