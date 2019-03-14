package wb.t20190314;

public interface IEnumerator<T> {
	boolean moveNext();
	T current();
}
