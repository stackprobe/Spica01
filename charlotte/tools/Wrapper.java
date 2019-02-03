package charlotte.tools;

public class Wrapper {
	public static <T> Unit<T> create(T value) {
		return new Unit<T>(value);
	}

	public static class Unit<T> {
		private T _value;

		public Unit(T value) {
			_value = value;
		}

		public T get() {
			return _value;
		}

		public Unit<T> accept(ConsumerEx<T> rtn) {
			try {
				rtn.accept(_value);
			}
			catch(Throwable e) {
				throw RTError.re(e);
			}
			return this;
		}

		public <R> Unit<R> change(FunctionEx<T, R> rtn) {
			try {
				return new Unit<R>(rtn.apply(_value));
			}
			catch(Throwable e) {
				throw RTError.re(e);
			}
		}
	}
}
