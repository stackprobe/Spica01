package charlotte.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortableHugeQueue {
	private IQueue<byte[]> _queue;
	private Comparator<byte[]> _comp;

	public SortableHugeQueue(IQueue<byte[]> queue, Comparator<byte[]> comp) {
		_queue = queue;
		_comp = comp;
	}

	public int LOAD_SIZE_LIMIT = 100000000; // 100 MB
	public int LOAD_COUNT_LIMIT = 1000000;

	public void sort() throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			List<String> fileTower = new ArrayList<String>();

			{
				List<byte[]> values = new ArrayList<byte[]>();
				int size = 0;

				while(_queue.hasElements()) {
					{
						byte[] value = _queue.dequeue();

						values.add(value);
						size += value.length;
					}

					if(LOAD_SIZE_LIMIT < size || LOAD_COUNT_LIMIT < values.size()) {
						buildTower(wd, fileTower, values);

						values.clear();
						size = 0;
					}
				}
				if(1 <= values.size()) {
					buildTower(wd, fileTower, values);
				}
			}

			IQueue<String> files = IQueues.wrap(ListTools.where(fileTower, file -> file != null));

			if(files.hasElements()) {
				String file = files.dequeue();

				if(files.hasElements()) {
					String file2;

					for(; ; ) {
						file2 = files.dequeue();

						if(files.hasElements() == false) {
							break;
						}

						{
							String fileNew = wd.makePath();

							try(
									Reader r = new Reader(file);
									Reader s = new Reader(file2);
									Writer w = new Writer(fileNew);
									) {
								merge(r, s, value -> w.write(value));
							}

							file = fileNew;
						}
					}

					try(
							Reader r = new Reader(file);
							Reader s = new Reader(file2);
							) {
						merge(r, s, value -> _queue.enqueue(value));
					}
				}
				else {
					try(Reader reader = new Reader(file)) {
						for(; ; ) {
							byte[] value = reader.read();

							if(value == null) {
								break;
							}
							_queue.enqueue(value);
						}
					}
				}
			}
		}
	}

	private void buildTower(WorkingDir wd, List<String> fileTower, List<byte[]> values) throws Exception {
		values.sort(_comp);

		String file = wd.makePath();

		try(Writer writer = new Writer(file)) {
			for(byte[] value : values) {
				writer.write(value);
			}
		}

		for(int index = 0; ; index++) {
			if(fileTower.size() <= index) {
				fileTower.add(file);
				break;
			}
			if(fileTower.get(index) == null) {
				fileTower.set(index, file);
				break;
			}

			{
				String fileNew = wd.makePath();

				try(
						Reader r = new Reader(file);
						Reader s = new Reader(fileTower.get(index));
						Writer w = new Writer(fileNew);
						) {
					merge(r, s, value -> w.write(value));
				}

				file = fileNew;
				fileTower.set(index, null);
			}
		}
	}

	private void merge(Reader r, Reader s, ConsumerEx<byte[]> w) throws Exception {
		byte[] a = r.read();
		byte[] b = s.read();

		while(a != null && b != null) {
			int ret = _comp.compare(a, b);

			if(ret <= 0) {
				w.accept(a);
				a = r.read();
			}
			if(0 <= ret) {
				w.accept(b);
				b = s.read();
			}
		}
		while(a != null) {
			w.accept(a);
			a = r.read();
		}
		while(b != null) {
			w.accept(b);
			b = s.read();
		}
	}

	public long FILE_SIZE_LIMIT = 300000000L; // 300 MB

	private class Writer implements AutoCloseable {
		private String _fileBase;
		private SegmentWriter _files;
		private SegmentWriter _writer = null;

		public Writer(String file) throws Exception {
			_fileBase = file;
			_files = new SegmentWriter(file);
		}

		public void write(byte[] buff) throws Exception {
			if(_writer == null) {
				String file = _fileBase + "_" + SecurityTools.makePassword_9a();

				_files.write(file.getBytes(StringTools.CHARSET_UTF8));
				_writer = new SegmentWriter(file);
			}
			_writer.write(buff);

			if(FILE_SIZE_LIMIT < _writer.size) {
				_writer.close();
				_writer = null;
			}
		}

		@Override
		public void close() throws Exception {
			if(_files != null) {
				_files.close();
				_files = null;

				if(_writer != null) {
					_writer.close();
					_writer = null;
				}
			}
		}
	}

	private class Reader implements AutoCloseable {
		private SegmentReader _files;
		private SegmentReader _reader = null;

		public Reader(String file) throws Exception {
			_files = new SegmentReader(file);
		}

		public byte[] read() throws Exception {
			for(; ; ) {
				if(_reader == null) {
					byte[] bFile = _files.read();

					if(bFile == null) {
						return null;
					}
					_reader = new SegmentReader(new String(bFile, StringTools.CHARSET_UTF8));
				}
				byte[] value = _reader.read();

				if(value != null) {
					return value;
				}
				_reader.close();
				_reader = null;
			}
		}

		@Override
		public void close() throws Exception {
			if(_files != null) {
				_files.close();
				_files = null;

				if(_reader != null) {
					_reader.close();
					_reader = null;
				}
			}
		}
	}

	private class SegmentWriter implements AutoCloseable {
		private FileOutputStream _stream;

		public SegmentWriter(String file) throws Exception {
			_stream = new FileOutputStream(file);
		}

		public long size = 0L;

		public void write(byte[] buff) throws Exception {
			_stream.write(BinTools.toBytes(buff.length));
			_stream.write(buff);

			size += 4 + buff.length;
		}

		@Override
		public void close() throws Exception {
			if(_stream != null) {
				_stream.close();
				_stream = null;
			}
		}
	}

	private class SegmentReader implements AutoCloseable {
		private String _file;
		private FileInputStream _stream;

		public SegmentReader(String file) throws Exception {
			_file = file;
			_stream = new FileInputStream(file);
		}

		public byte[] read() throws Exception {
			byte[] buff = new byte[4];
			int readSize = _stream.read(buff);

			if(readSize == -1) {
				return null;
			}
			if(readSize != 4) {
				throw new RTError();
			}
			int size = BinTools.toInt(buff);

			if(size < 0 || IntTools.IMAX < size) {
				throw new RTError();
			}
			buff = new byte[size];
			readSize = _stream.read(buff);

			if(readSize != size) {
				throw new RTError();
			}
			return buff;
		}

		@Override
		public void close() throws Exception {
			if(_stream != null) {
				_stream.close();
				_stream = null;

				FileTools.delete(_file);
			}
		}
	}
}
