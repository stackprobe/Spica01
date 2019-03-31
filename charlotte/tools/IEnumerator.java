package charlotte.tools;

public interface IEnumerator<T> {
	boolean moveNext();
	T current();
}
