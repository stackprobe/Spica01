package wb.t20181219;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IntTools;

public class Dim<T> {
	private List<T> _linear;
	private int[] _sizes;

	public Dim(T defval, int... sizes) {
		if(sizes.length < 1 || IntTools.IMAX < sizes.length) {
			throw new IllegalArgumentException();
		}
		int linearSize = 1;

		for(int size : sizes) {
			if(size < 1 || IntTools.IMAX / linearSize < size) {
				throw new IllegalArgumentException();
			}
			linearSize *= size;
		}
		_linear = new ArrayList<T>(linearSize);

		for(int index = 0; index < linearSize; index++) {
			_linear.add(defval);
		}
		_sizes = sizes;
	}

	private int getLinearIndex(int[] indexes) {
		if(_sizes.length != indexes.length) {
			throw new IllegalArgumentException();
		}
		int linearIndex = 0;

		for(int i = 0; i < indexes.length; i++) {
			if(indexes[i] < 0 || _sizes[i] <= indexes[i]) {
				throw new IllegalArgumentException();
			}
			linearIndex *= _sizes[i];
			linearIndex += indexes[i];
		}
		return linearIndex;
	}

	public T get(int... indexes) {
		return _linear.get(getLinearIndex(indexes));
	}

	public T set(T value, int... indexes) {
		return _linear.set(getLinearIndex(indexes), value);
	}
}
