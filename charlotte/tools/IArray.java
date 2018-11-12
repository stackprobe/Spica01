package charlotte.tools;

public abstract class IArray<T> {
	public abstract int length();
	public abstract T get(int index);
	public abstract void set(int index, T element);

	public void swap(int a, int b) {
		T tmp = get(a);
		set(a, get(b));
		set(b, tmp);
	}
}
