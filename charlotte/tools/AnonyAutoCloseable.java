package charlotte.tools;

public class AnonyAutoCloseable {
	private static class Closer implements AutoCloseable {
		private AutoCloseable _inner;

		public Closer(AutoCloseable inner) {
			_inner = inner;
		}

		@Override
		public void close() throws Exception {
			if(_inner != null) {
				_inner.close();
				_inner = null;
			}
		}
	}

	public static AutoCloseable create(AutoCloseable closer) {
		return new Closer(closer);
	}
}
