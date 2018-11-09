package charlotte.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HugeQueue implements AutoCloseable {
	private WorkingDir _wd;
	private String _rFile;
	private String _wFile;
	private FileInputStream _reader;
	private FileOutputStream _writer;
	private QueueUnit<String> _midFiles = new QueueUnit<String>();
	private int _innerCount;

	public HugeQueue() throws Exception {
		_wd = new WorkingDir();
		_rFile = _wd.makePath();
		_wFile = _wd.makePath();

		FileTools.writeAllBytes(_rFile, BinTools.EMPTY);

		_reader = new FileInputStream(_rFile);
		_writer = new FileOutputStream(_wFile);
	}

	public int size() {
		return _innerCount;
	}

	// 0以上であること。
	public long FILE_SIZE_LIMIT = 100000000L; // 100 MB

	public void enqueue(byte[] value) throws Exception {
		if(FILE_SIZE_LIMIT < _writer.getChannel().position()) {
			_writer.close();

			_midFiles.enqueue(_wFile);
			_wFile = _wd.makePath();

			_writer = new FileOutputStream(_wFile);
		}
		_writer.write(BinTools.toBytes(value.length));
		_writer.write(value);
		_innerCount++;
	}

	public byte[] dequeue() throws Exception {
		if(_innerCount == 0) {
			throw new RTError("空のキューから読み込もうとしました。");
		}
		_innerCount--;

		byte[] bSize = new byte[4];
		int readSize = _reader.read(bSize);

		if(readSize == -1) {
			if(1 <= _midFiles.size()) {
				_reader.close();

				FileTools.delete(_rFile);
				_rFile = _midFiles.dequeue();

				_reader = new FileInputStream(_rFile);
			}
			else {
				_reader.close();
				_writer.close();

				{
					String tmp = _rFile;
					_rFile = _wFile;
					_wFile = tmp;
				}

				_reader = new FileInputStream(_rFile);
				_writer = new FileOutputStream(_wFile);
			}
			readSize = _reader.read(bSize);
		}
		if(readSize != 4) {
			throw new RTError("不正なサイズの読み込みサイズです。" + readSize);
		}
		int size = BinTools.toInt(bSize);

		if(size < 0 || IntTools.IMAX < size) {
			throw new RTError("不正なサイズです。" + size);
		}
		byte[] value = new byte[size];
		readSize = _reader.read(value, 0, size);

		if(readSize != size) {
			throw new RTError("不正なデータの読み込みサイズです。" + readSize + ", " + size);
		}
		return value;
	}

	@Override
	public void close() throws Exception {
		if(_wd != null) {
			_reader.close();
			_writer.close();

			_wd.close();
			_wd = null;
		}
	}
}
