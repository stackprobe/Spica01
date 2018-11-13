package charlotte.tools;

public interface IArray<T> {
	int length();
	T get(int index);
	void set(int index, T element);
}
