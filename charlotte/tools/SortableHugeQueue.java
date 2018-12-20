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

			QueueUnit<String> files = new QueueUnit<String>();

			for(String file : fileTower) {
				if(file != null) {
					files.enqueue(file);
				}
			}

			if(files.size() == 0) {
				// noop
			}
			else if(files.size() == 1) {
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
					String fileNew = wd.makePath();

					try(
							Reader r = new Reader(files.dequeue());
							Reader s = new Reader(files.dequeue());
							Writer w = new Writer(fileNew);
							) {
						merge(r, s, value -> w.write(value));
					}

					files.enqueue(fileNew);
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

	private class Writer implements AutoCloseable {
		private FileOutputStream _stream;

		public Writer(String file) throws Exception {
			_stream = new FileOutputStream(file);
		}

		public void write(byte[] buff) throws Exception {
			_stream.write(BinTools.toBytes(buff.length));
			_stream.write(buff);
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
