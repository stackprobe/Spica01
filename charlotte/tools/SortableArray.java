package charlotte.tools;

import java.util.Comparator;

public class SortableArray<T> {
	private IArray<T> _arr;
	private Comparator<T> _comp;

	public SortableArray(IArray<T> arr, Comparator<T> comp) {
		_arr = arr;
		_comp = comp;
	}

	public void sort() {
		sort(0, _arr.length(), 0);
	}

	private void sort(int start, int end, int depth) {
		if(end - start < 9) {
			selectionSort(start, end);
			return;
		}
		if(30 < depth) {
			combSort(start, end);
			return;
		}
		int left = start;
		int pivot = (start + end) / 2;
		int right = end - 1;

		for(; ; ) {
			while(left < pivot && _comp.compare(_arr.get(left), _arr.get(pivot)) <= 0) {
				left++;
			}
			while(pivot < right && _comp.compare(_arr.get(pivot), _arr.get(right)) <= 0) {
				right--;
			}
			if(left == right) {
				break;
			}
			_arr.swap(left, right);

			if(left == pivot) {
				pivot = right;
				left++;
			}
			else if(right == pivot) {
				pivot = left;
				right--;
			}
			else {
				left++;
				right--;
			}
		}
		sort(start, pivot, depth + 1);
		sort(pivot + 1, end, depth + 1);
	}

	public void selectionSort() {
		selectionSort(0, _arr.length());
	}

	private void selectionSort(int start, int end) {
		for(int left = start; left + 1 < end; left++) {
			int smallest = left;

			for(int right = left + 1; right < end; right++) {
				if(0 < _comp.compare(_arr.get(smallest), _arr.get(right))) {
					smallest = right;
				}
			}
			if(smallest != left) {
				_arr.swap(left, smallest);
			}
		}
	}

	public void combSort() {
		combSort(0, _arr.length());
	}

	private void combSort(int start, int end) {
		for(int span = end - start; ; ) {
			span = (int)((span * 10L) / 13); //(int)(span / 1.3);

			if(span < 2) {
				break;
			}
			else if(span == 9 || span == 10) {
				span = 11;
			}
			for(int left = start, right = start + span; right < end; left++ ,right++) {
				if(0 < _comp.compare(_arr.get(left), _arr.get(right))) {
					_arr.swap(left, right);
				}
			}
		}
		gnomeSort(start, end);
	}

	public void gnomeSort() {
		gnomeSort(0, _arr.length());
	}

	private void gnomeSort(int start, int end) {
		for(int right = start + 1; right < end; right++) {
			for(int left = right; start < left && 0 < _comp.compare(_arr.get(left - 1), _arr.get(left)); left--) {
				_arr.swap(left - 1, left);
			}
		}
	}
}
