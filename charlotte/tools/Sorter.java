package charlotte.tools;

import java.util.Comparator;

public class Sorter {
	public static <T> void sort(IArray<T> arr, Comparator<T> comp) {
		int[] ranges = new int[100];
		int rc = 0;

		ranges[rc++] = 0;
		ranges[rc++] = arr.length();

		while(0 < rc) {
			int end = ranges[--rc];
			int begin = ranges[--rc];

			if(end - begin < 20) {
				insertSort(arr, comp, begin, end);
				continue;
			}
			if(ranges.length - 2 <= rc) {
				combSort(arr, comp);
				break;
			}
			int left = begin;
			int pivot = (begin + end) / 2;
			int right = end - 1;

			for(; ; ) {
				while(comp.compare(arr.get(left), arr.get(pivot)) < 0) {
					left++;
				}
				while(comp.compare(arr.get(pivot), arr.get(right)) < 0) {
					right--;
				}
				if(left == right) {
					break;
				}
				arr.swap(left, right);

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
			ranges[rc++] = begin;
			ranges[rc++] = pivot;
			ranges[rc++] = pivot + 1;
			ranges[rc++] = end;
		}
	}

	public static <T> void insertSort(IArray<T> arr, Comparator<T> comp) {
		insertSort(arr, comp, 0, arr.length());
	}

	public static <T> void insertSort(IArray<T> arr, Comparator<T> comp, int begin, int end) {
		for(int left = begin; left + 1 < end; left++) {
			int smallest = left;

			for(int right = left + 1; right < end; right++) {
				if(comp.compare(arr.get(right), arr.get(smallest)) < 0) {
					smallest = right;
				}
			}
			if(smallest != left) {
				arr.swap(left, smallest);
			}
		}
	}

	public static <T> void combSort(IArray<T> arr, Comparator<T> comp) {
		int span = arr.length();

		for(; ; ) {
			span = (int)(span / 1.3);

			if(span < 2) {
				break;
			}
			if(span == 9 || span == 10) {
				span = 11;
			}
			for(int left = 0, right = span; right < arr.length(); left++ ,right++) {
				if(0 < comp.compare(arr.get(left), arr.get(right))) {
					arr.swap(left, right);
				}
			}
		}
		gnomeSort(arr, comp);
	}

	public static <T> void gnomeSort(IArray<T> arr, Comparator<T> comp) {
		for(int r = 1; r < arr.length(); r++) {
			for(int l = r; 0 < l; l--) {
				if(comp.compare(arr.get(l - 1), arr.get(l)) <= 0) {
					break;
				}
				arr.swap(l - 1, l);
			}
		}
	}
}
