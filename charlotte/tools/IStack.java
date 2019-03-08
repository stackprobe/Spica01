package charlotte.tools;

public interface IStack<T> {
	boolean hasElements();
	void push(T element);
	T pop();
}
