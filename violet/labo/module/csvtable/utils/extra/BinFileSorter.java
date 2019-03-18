package violet.labo.module.csvtable.utils.extra;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import charlotte.tools.ExceptionDam;
import charlotte.tools.FileTools;
import charlotte.tools.WorkingDir;
import violet.labo.module.csvtable.utils.HugeSorter;

public class BinFileSorter extends HugeSorter<byte[]> implements AutoCloseable {
	public int RECORD_SIZE = 16;

	private String _rFile;
	private String _wFile;
	private WorkingDir _wd;
	private InputStream _reader = null;
	private OutputStream _writer = null;

	public BinFileSorter(String rwFile) throws Exception {
		this(rwFile, rwFile);
	}

	public BinFileSorter(String rFile, String wFile) throws Exception {
		_rFile = rFile;
		_wFile = wFile;
		_wd = new WorkingDir();
	}

	@Override
	protected void beforeFirstRead() throws Exception {
		_reader = new FileInputStream(_rFile);
	}

	@Override
	protected byte[] read() throws Exception {
		byte[] record = new byte[RECORD_SIZE];
		int readSize = _reader.read(record);

		if(readSize == -1) {
			return null;
		}
		if(readSize != RECORD_SIZE) {
			throw new Exception("Bad read size: " + readSize + ", " + RECORD_SIZE);
		}
		return record;
	}

	@Override
	protected void afterLastRead() throws Exception {
		_reader.close();
		_reader = null;
	}

	@Override
	protected void beforeFirstWrite() throws Exception {
		_writer = new FileOutputStream(_wFile);
	}

	@Override
	protected void write(byte[] record) throws Exception {
		if(record.length != RECORD_SIZE) {
			throw new Exception("Bad record size: " + record.length + ", " + RECORD_SIZE);
		}
		_writer.write(record);
	}

	@Override
	protected void afterLastWrite() throws Exception {
		_writer.close();
		_writer = null;
	}

	private class Part implements IPart<byte[]> {
		private String _file;
		private OutputStream _writer = null;
		private InputStream _reader = null;

		public Part() {
			_file = _wd.makePath();
		}

		public String getFile() {
			return _file;
		}

		@Override
		public void beforeFirstWrite() throws Exception {
			_writer = new FileOutputStream(_file);
		}

		@Override
		public void write(byte[] record) throws Exception {
			if(record.length != RECORD_SIZE) {
				throw new Exception("Bad record size: " + record.length + ", " + RECORD_SIZE);
			}
			_writer.write(record);
		}

		@Override
		public void afterLastWrite() throws Exception {
			_writer.close();
			_writer = null;
		}

		@Override
		public void beforeFirstRead() throws Exception {
			_reader = new FileInputStream(_file);
		}

		@Override
		public byte[] read() throws Exception {
			byte[] record = new byte[RECORD_SIZE];
			int readSize = _reader.read(record);

			if(readSize == -1) {
				return null;
			}
			if(readSize != RECORD_SIZE) {
				throw new Exception("Bad read size: " + readSize + ", " + RECORD_SIZE);
			}
			return record;
		}

		@Override
		public void afterLastRead() throws Exception {
			_reader.close();
			_reader = null;
		}

		@Override
		public void close() throws Exception {
			ExceptionDam.section(eDam -> {
				if(_reader != null) {
					eDam.invoke(() -> _reader.close());
					_reader = null;
				}
				if(_writer != null) {
					eDam.invoke(() -> _writer.close());
					_writer = null;
				}
				eDam.invoke(() -> FileTools.delete(_file));
			});
		}
	}

	@Override
	protected void copy(IPart<byte[]> part) throws Exception {
		FileTools.delete(_wFile);
		FileTools.moveFile(((Part) part).getFile(), _wFile);
	}

	@Override
	protected int getWeight(byte[] record) throws Exception {
		return record.length + 10;
	}

	@Override
	protected int capacity() throws Exception {
		return 60000000; // 60 MB
	}

	@Override
	protected IPart<byte[]> createPart() throws Exception {
		return new Part();
	}

	@Override
	public void close() throws Exception {
		ExceptionDam.section(eDam -> {
			if(_reader != null) {
				eDam.invoke(() -> _reader.close());
				_reader = null;
			}
			if(_writer != null) {
				eDam.invoke(() -> _writer.close());
				_writer = null;
			}
			if(_wd != null) {
				eDam.invoke(() -> _wd.close());
				_wd = null;
			}
		});
	}
}
