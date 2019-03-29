package violet.labo.module.fatcalc_v1;

import java.util.Arrays;
import java.util.Iterator;

import charlotte.tools.IArray;
import charlotte.tools.IArrays;
import charlotte.tools.RTError;

public class FatIntList implements Iterable<Integer> {
	private int[] _arr;
	private int _size;

	public FatIntList() {
		this(new int[0]);
	}

	public FatIntList(int[] arr) {
		_arr = Arrays.copyOf(arr, arr.length);
		_size = arr.length;
	}

	public void clear() {
		_size = 0;
	}

	public int size() {
		return _size;
	}

	public void resizeCapacity(int size) {
		if(size < _size) {
			throw null; // never
		}
		_arr = Arrays.copyOf(_arr, size);
	}

	public void resize(int sizeNew) {
		if(_arr.length < sizeNew) {
			resizeCapacity(sizeNew);
		}
		_size = sizeNew;
	}

	public void add(int value) {
		if(_arr.length <= _size) {
			resizeCapacity(_arr.length < 1024 ? 1024 : _arr.length * 2);
		}
		_arr[_size++] = value;
	}

	public int get(int index) {
		return _arr[index];
	}

	public void set(int index, int value) {
		_arr[index] = value;
	}

	public void makeMeSubList(int start, int end) {
		int[] arrNew = new int[end - start];
		System.arraycopy(_arr, start, arrNew, 0, end - start);
		_arr = arrNew;
		_size = end - start;
	}

	public void reverse() {
		for(int i = 0, j = _size - 1; i < j; i++, j--) {
			int tmp = _arr[i];
			_arr[i] = _arr[j];
			_arr[j] = tmp;
		}
	}

	public void shift(int count) {
		int[] arrNew = new int[_arr.length + count];
		System.arraycopy(_arr, 0, arrNew, count, _arr.length);
		_arr = arrNew;
	}

	@Override
	public Iterator<Integer> iterator() {
		return IArrays.asList(new IArray<Integer>() {
			@Override
			public int length() {
				return _size;
			}

			@Override
			public Integer get(int index) {
				return _arr[index];
			}

			@Override
			public void set(int index, Integer element) {
				throw new RTError("forbidden");
			}
		})
		.iterator();
	}
}
