package charlotte.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubArrayList<T> implements Iterable<T> {
	private List<IArray<T>> _arrs = new ArrayList<IArray<T>>();

	public SubArrayList<T> addOne(T element) {
		List<T> list = new ArrayList<T>();
		list.add(element);
		return add(IArrays.wrap(list));
	}

	public SubArrayList<T> add(IArray<T> arr) {
		_arrs.add(arr);
		return this;
	}

	public IArray<T> toArray() {
		return new IArray<T>() {

			/**
			 * TODO
			 */
			@Override
			public int length() {
				int size = 0;

				for(IArray<T> arr : _arrs) {
					size += arr.length();
				}
				return size;
			}

			/**
			 * TODO
			 */
			@Override
			public T get(int index) {
				for(IArray<T> arr : _arrs) {
					if(index < arr.length()) {
						return arr.get(index);
					}
					index -= arr.length();
				}
				throw new RTError();
			}

			/**
			 * TODO
			 */
			@Override
			public void set(int index, T element) {
				for(IArray<T> arr : _arrs) {
					if(index < arr.length()) {
						arr.set(index, element);
					}
					index -= arr.length();
				}
			}
		};
	}

	@Override
	public Iterator<T> iterator() {
		return IArrays.asList(toArray()).iterator();
	}
}
