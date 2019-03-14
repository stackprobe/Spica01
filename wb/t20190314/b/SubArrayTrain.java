package wb.t20190314.b;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import charlotte.tools.IArray;
import charlotte.tools.IArrays;
import charlotte.tools.IntTools;
import charlotte.tools.SortedList;

public class SubArrayTrain<T> implements Iterable<T> {
	private List<IArray<T>> _arrs = new ArrayList<IArray<T>>();

	public SubArrayTrain<T> addOne(T element) {
		return add(IArrays.one(element));
	}

	public SubArrayTrain<T> add(IArray<T> arr) {
		_arrs.add(arr);
		return this;
	}

	public IArray<T> toArray() {
		List<IArray<T>> arrs = new ArrayList<IArray<T>>(_arrs);
		SortedList<Integer> starts = new SortedList<Integer>(IntTools.comp);
		int count = 0;

		for(int index = 0; index < arrs.size(); index++) {
			starts.addLargestEver(count);
			count += arrs.get(index).length();
		}
		final int f_count = count;

		return new IArray<T>() {
			@Override
			public int length() {
				return f_count;
			}

			@Override
			public T get(int index) {
				int arrIndex = starts.rightIndexOf(starts.getFerret(index));
				return arrs.get(arrIndex).get(index - starts.get(arrIndex));
			}

			@Override
			public void set(int index, T element) {
				int arrIndex = starts.rightIndexOf(starts.getFerret(index));
				arrs.get(arrIndex).set(index - starts.get(arrIndex), element);
			}
		};
	}

	@Override
	public Iterator<T> iterator() {
		return IArrays.asList(toArray()).iterator();
	}
}
