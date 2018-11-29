package charlotte.tools;

public interface ConsumerEx<T> {
	void accept(T t) throws Exception;
}
