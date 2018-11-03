package charlotte.tools;

public class MSection implements AutoCloseable {
	private MutexUnit _handle;
	private boolean _binding;

	public MSection(String ident) throws Exception {
		this(new MutexUnit(ident), true);
	}

	public MSection(MutexUnit handle) throws Exception {
		this(handle, false);
	}

	public MSection(MutexUnit handle, boolean binding) throws Exception {
		_handle = handle;
		_handle.waitOne();
		_binding = binding;
	}

	@Override
	public void close() throws Exception {
		if(_handle != null) {
			_handle.releaseMutex();

			if(_binding) {
				_handle.close();
			}
			_handle = null;
		}
	}
}
