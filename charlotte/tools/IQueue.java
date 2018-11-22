package charlotte.tools;

public interface IQueue<T> {
	boolean hasElements();
	void enqueue(T element);
	T dequeue();
}
