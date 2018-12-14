package charlotte.tools;

import java.util.Comparator;

public interface IArray<T> {
	int length();
	T get(int index);
	void set(int index, T element);

	default void sort(Comparator<T> comp) {
		new SortableIArray<T>(this, comp).sort();
	}

	default void swap(int a, int b) {
		T tmp = get(a);
		set(a, get(b));
		set(b, tmp);
	}
}
