package charlotte.tools;

import java.util.Comparator;

public interface IArray<T> {
	int length();
	T get(int index);
	void set(int index, T element);

	default void sort(Comparator<T> comp) {
		int span = length();
		boolean swapped;

		do {
			span = (int)((span * 10L) / 13); //(int)(span / 1.3);
			swapped = false;

			if(span < 1) {
				span = 1;
			}
			else if(span == 9 || span == 10) {
				span = 11;
			}
			for(int left = 0, right = span; right < length(); left++ ,right++) {
				if(0 < comp.compare(get(left), get(right))) {
					swap(left, right);
					swapped = true;
				}
			}
		}
		while(1 < span || swapped);
	}

	default void swap(int a, int b) {
		T tmp = get(a);
		set(a, get(b));
		set(b, tmp);
	}
}
