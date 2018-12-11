package charlotte.tools;

public class Wrapper<T> {
	public static <T> Wrapper<T> create(T value) {
		return new Wrapper<T>(value);
	}

	private T _value;

	public Wrapper(T value) {
		_value = value;
	}

	public T get() {
		return _value;
	}

	public Wrapper<T> accept(ConsumerEx<T> rtn) {
		try {
			rtn.accept(_value);
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
		return this;
	}

	public <R> Wrapper<R> change(FunctionEx<T, R> rtn) {
		try {
			return new Wrapper<R>(rtn.apply(_value));
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}
}
