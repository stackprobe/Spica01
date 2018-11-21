package charlotte.tools;

public class SubArray<T> implements IArray<T> {
	private IArray<T> _arr;
	private int _start;
	private int _end;

	public SubArray(IArray<T> arr) {
		this(arr, 0);
	}

	public SubArray(IArray<T> arr, int start) {
		this(arr, start, arr.length() - start);
	}

	public SubArray(IArray<T> arr, int start, int end) {
		if(start < 0 || end < start || arr.length() < end) {
			throw new IndexOutOfBoundsException("(0, " + arr.length() + ") -> (" + start + ", " + end + ")");
		}
		_arr = arr;
		_start = start;
		_end = end;
	}

	@Override
	public int length() {
		return _end - _start;
	}

	@Override
	public T get(int index) {
		return _arr.get(_start + index);
	}

	@Override
	public void set(int index, T element) {
		_arr.set(_start + index, element);
	}
}