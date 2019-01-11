package charlotte.tools;

import java.io.FileOutputStream;

/**
 *	大量のメモリを確保しない。
 *	ファイルハンドルを開きっぱなしにしない。
 *
 */
public class ByteArrayLowCostBuffer implements AutoCloseable {
	private WorkingDir _wd = null;
	private String _file = null;
	private int _size = 0;

	private String getFile() throws Exception {
		if(_wd == null) {
			_wd = new WorkingDir();
			_file = _wd.makePath();
		}
		return _file;
	}

	public void write(byte data[]) throws Exception {
		write(data, 0);
	}

	public void write(byte data[], int offset) throws Exception {
		write(data, offset, data.length - offset);
	}

	public void write(byte data[], int offset, int length) throws Exception {
		try(FileOutputStream writer = new FileOutputStream(getFile(), true)) {
			writer.write(data, offset, length);
		}
		_size += length;
	}

	public int size() {
		return _size;
	}

	public byte[] toByteArray() throws Exception {
		return _size == 0 ? BinTools.EMPTY : FileTools.readAllBytes(getFile());
	}

	@Override
	public void close() throws Exception {
		if(_wd != null) {
			_wd.close();
			_wd = null;
		}
	}
}
