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
	public int ELEMENT_WEIGHT = 30;

	public void sort() throws Exception {
		try(WorkingDir wd = new WorkingDir()) {
			QueueUnit<String> files = new QueueUnit<String>();

			{
				List<byte[]> values = new ArrayList<byte[]>();
				int size = 0;

				for(; ; ) {
					if(_queue.hasElements()) {
						byte[] value = _queue.dequeue();

						values.add(value);
						size += value.length + ELEMENT_WEIGHT;
					}
					else {
						size = -1;
					}

					if(size == -1 || LOAD_SIZE_LIMIT < size) {
						values.sort(_comp);

						{
							String file = wd.makePath();

							files.enqueue(file);

							try(Writer writer = new Writer(file)) {
								for(byte[] value : values) {
									writer.write(value);
								}
							}
						}

						if(size == -1) {
							break;
						}
						values.clear();
						size = 0;
					}
				}
			}

			if(files.size() == 1) {
				try(Reader reader = new Reader(files.dequeue())) {
					for(; ; ) {
						byte[] value = reader.read();

						if(value == null) {
							break;
						}
						_queue.enqueue(value);
					}
				}
			}
			else {
				while(2 < files.size()) {
					String file = wd.makePath();

					files.enqueue(file);

					try(
							Reader r = new Reader(files.dequeue());
							Reader s = new Reader(files.dequeue());
							Writer w = new Writer(file);
							) {
						merge(r, s, value -> w.write(value));
					}
				}

				try(
						Reader r = new Reader(files.dequeue());
						Reader s = new Reader(files.dequeue());
						) {
					merge(r, s, value -> _queue.enqueue(value));
				}
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
			b = r.read();
		}
	}

	private class Writer implements AutoCloseable {
		private FileOutputStream _stream;

		public Writer(String file) throws Exception {
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

	private class Reader implements AutoCloseable {
		private String _file;
		private FileInputStream _stream;

		public Reader(String file) throws Exception {
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
