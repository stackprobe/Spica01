package charlotte.tools;

import java.util.Comparator;

public interface IArray<T> {
	int length();
	T get(int index);
	void set(int index, T element);

	default void sort(Comparator<T> comp) {
		for(int span = length(); ; ) {
			span = (int)((span * 10L) / 13); //(int)(span / 1.3);

			if(span < 2) {
				break;
			}
			else if(span == 9 || span == 10) {
				span = 11;
			}
			for(int left = 0, right = span; right < length(); left++ ,right++) {
				if(0 < comp.compare(get(left), get(right))) {
					swap(left, right);
				}
			}
		}
		for(int right = 1; right < length(); right++) {
			for(int left = right; 0 < left && 0 < comp.compare(get(left - 1), get(left)); left--) {
				swap(left - 1, left);
			}
		}
	}

	default void swap(int a, int b) {
		T tmp = get(a);
		set(a, get(b));
		set(b, tmp);
	}
}
