package charlotte.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Comparator;

public class SortableHugeQueue {
	private IQueue<byte[]> _queue;
	private Comparator<byte[]> _comp;

	public SortableHugeQueue(IQueue<byte[]> queue, Comparator<byte[]> comp) {
		_queue = queue;
		_comp = comp;
	}

	public void sort() throws Exception {
		throw null; // TODO ???
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
