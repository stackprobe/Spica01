package violet.labo.module.csvtable.utils;

import java.util.List;

import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.WorkingDir;

public class CsvFileSorter extends HugeSorter<List<String>> implements AutoCloseable {
	private String _rFile;
	private String _wFile;
	private WorkingDir _wd;
	private CsvFileReader _reader = null;
	private CsvFileWriter _writer = null;

	public CsvFileSorter(String rwFile) throws Exception {
		this(rwFile, rwFile);
	}

	public CsvFileSorter(String rFile, String wFile) throws Exception {
		_rFile = rFile;
		_wFile = wFile;
		_wd = new WorkingDir();
	}

	@Override
	protected void beforeFirstRead() throws Exception {
		_reader = new CsvFileReader(_rFile);
	}

	@Override
	protected List<String> read() throws Exception {
		return _reader.readRow();
	}

	@Override
	protected void afterLastRead() throws Exception {
		_reader.close();
		_reader = null;
	}

	@Override
	protected void beforeFirstWrite() throws Exception {
		_writer = new CsvFileWriter(_wFile);
	}

	@Override
	protected void write(List<String> row) throws Exception {
		_writer.writeRow(row);
	}

	@Override
	protected void afterLastWrite() throws Exception {
		_writer.close();
		_writer = null;
	}

	private class Part implements IPart<List<String>> {
		private String _file;
		private CsvFileWriter _writer = null;
		private CsvFileReader _reader = null;

		public Part() {
			_file = _wd.makePath();
		}

		public String getFile() {
			return _file;
		}

		@Override
		public void beforeFirstWrite() throws Exception {
			_writer = new CsvFileWriter(_file);
		}

		@Override
		public void write(List<String> row) throws Exception {
			_writer.writeRow(row);
		}

		@Override
		public void afterLastWrite() throws Exception {
			_writer.close();
			_writer = null;
		}

		@Override
		public void beforeFirstRead() throws Exception {
			_reader = new CsvFileReader(_file);
		}

		@Override
		public List<String> read() throws Exception {
			return _reader.readRow();
		}

		@Override
		public void afterLastRead() throws Exception {
			_reader.close();
			_reader = null;
		}

		@Override
		public void close() throws Exception {
			if(_reader != null) {
				_reader.close();
				_reader = null;
			}
			if(_writer != null) {
				_writer.close();
				_writer = null;
			}
			FileTools.delete(_file);
		}
	}

	@Override
	protected void copy(IPart<List<String>> part) throws Exception {
		FileTools.delete(_wFile);
		FileTools.moveFile(((Part)part).getFile(), _wFile);
	}

	@Override
	protected int getWeight(List<String> row) throws Exception {
		int weight = 10;

		for(String cell : row) {
			weight += cell.length() + 10;
		}
		return weight;
	}

	@Override
	protected int capacity() throws Exception {
		return 50000000; // 50 M char
	}

	@Override
	protected IPart<List<String>> createPart() throws Exception {
		return new Part();
	}

	@Override
	public void close() throws Exception {
		if(_reader != null) {
			_reader.close();
			_reader = null;
		}
		if(_writer != null) {
			_writer.close();
			_writer = null;
		}
		if(_wd != null) {
			_wd.close();
			_wd = null;
		}
	}
}
