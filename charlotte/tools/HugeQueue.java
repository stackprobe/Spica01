package charlotte.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HugeQueue implements IQueue<byte[]>, AutoCloseable {
	private WorkingDir _wd;
	private String _rFile;
	private String _wFile;
	private FileInputStream _reader;
	private FileOutputStream _writer;
	private QueueUnit<String> _midFiles = new QueueUnit<String>();
	private long _count;

	public HugeQueue() throws Exception {
		HandleDam.section(hDam -> {
			_wd = hDam.add(new WorkingDir());
			_rFile = _wd.makePath();
			_wFile = _wd.makePath();

			FileTools.writeAllBytes(_rFile, BinTools.EMPTY);

			_reader = hDam.add(new FileInputStream(_rFile));
			_writer = hDam.add(new FileOutputStream(_wFile));
		});
	}

	@Override
	public boolean hasElements() {
		return _count != 0L;
	}

	@Override
	public void enqueue(byte[] element) {
		RTError.run(() -> enqueue_e(element));
	}

	@Override
	public byte[] dequeue() {
		return RTError.get(() -> dequeue_e());
	}

	public long FILE_SIZE_LIMIT = 100000000L; // 100 MB  --  need 0 <=

	private void enqueue_e(byte[] element) throws Exception {
		if(FILE_SIZE_LIMIT < _writer.getChannel().position()) {
			_writer.close();

			_midFiles.enqueue(_wFile);
			_wFile = _wd.makePath();

			_writer = new FileOutputStream(_wFile);
		}
		_writer.write(BinTools.toBytes(element.length));
		_writer.write(element);
		_count++;
	}

	private byte[] dequeue_e() throws Exception {
		if(_count == 0L) {
			throw new RTError("空のキューから読み込もうとしました。");
		}
		_count--;

		byte[] bSize = new byte[4];
		int readSize = _reader.read(bSize);

		if(readSize == -1) {
			if(_midFiles.hasElements()) {
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
		byte[] element = new byte[size];
		readSize = _reader.read(element, 0, size);

		if(readSize != size) {
			throw new RTError("不正なデータの読み込みサイズです。" + readSize + ", " + size);
		}
		return element;
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
