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
		HandleDam.transaction(hDam -> {
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

	public long FILE_SIZE_LIMIT = 100000000L; // 100 MB

	private void enqueue_e(byte[] element) throws Exception {
		if(_writer.getChannel().position() != 0L && FILE_SIZE_LIMIT < _writer.getChannel().position()) {
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
			throw new RTError("Can not dequeue_e() when queue is empty");
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
			throw new RTError("Bad readSize: " + readSize);
		}
		int size = BinTools.toInt(bSize);

		if(size < 0 || IntTools.IMAX < size) {
			throw new RTError("Bad size: " + size);
		}
		byte[] element = new byte[size];
		readSize = _reader.read(element, 0, size);

		if(readSize != size) {
			throw new RTError("Bad readSize, size: " + readSize + ", " + size);
		}
		return element;
	}

	private LimitCounter _closeOnce = LimitCounter.one();

	@Override
	public void close() throws Exception {
		if(_closeOnce.issue()) {
			ExceptionDam.section(eDam -> {
				eDam.invoke(() -> _reader.close());
				eDam.invoke(() -> _writer.close());
				eDam.invoke(() -> _wd.close());
			});
		}
	}
}
