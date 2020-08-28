package charlotte.tools;

public class AnonyAutoCloseable {
	private static class Closer implements AutoCloseable {
		private RunnableEx _inner;

		public Closer(RunnableEx inner) {
			_inner = inner;
		}

		@Override
		public void close() throws Exception {
			if(_inner != null) {
				_inner.run();
				_inner = null;
			}
		}
	}

	public static AutoCloseable create(RunnableEx closer) {
		return new Closer(closer);
	}
}
