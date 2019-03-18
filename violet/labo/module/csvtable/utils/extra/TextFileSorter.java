package violet.labo.module.csvtable.utils.extra;

import java.io.Reader;
import java.io.Writer;

import charlotte.tools.ExceptionDam;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;
import charlotte.tools.WorkingDir;
import violet.labo.module.csvtable.utils.HugeSorter;

public class TextFileSorter extends HugeSorter<String> implements AutoCloseable {
	public String charset = StringTools.CHARSET_SJIS;

	private String _rFile;
	private String _wFile;
	private WorkingDir _wd;
	private Reader _reader = null;
	private Writer _writer = null;

	public TextFileSorter(String rwFile) throws Exception {
		this(rwFile, rwFile);
	}

	public TextFileSorter(String rFile, String wFile) throws Exception {
		_rFile = rFile;
		_wFile = wFile;
		_wd = new WorkingDir();
	}

	@Override
	protected void beforeFirstRead() throws Exception {
		_reader = FileTools.openReader(_rFile, charset);
	}

	@Override
	protected String read() throws Exception {
		return FileTools.readLine(_reader);
	}

	@Override
	protected void afterLastRead() throws Exception {
		_reader.close();
		_reader = null;
	}

	@Override
	protected void beforeFirstWrite() throws Exception {
		_writer = FileTools.openWriter(_wFile, charset);
	}

	@Override
	protected void write(String line) throws Exception {
		_writer.write(line);
		_writer.write('\n');
	}

	@Override
	protected void afterLastWrite() throws Exception {
		_writer.close();
		_writer = null;
	}

	private class Part implements IPart<String> {
		private String _file;
		private Writer _writer = null;
		private Reader _reader = null;

		public Part() {
			_file = _wd.makePath();
		}

		public String getFile() {
			return _file;
		}

		@Override
		public void beforeFirstWrite() throws Exception {
			_writer = FileTools.openWriter(_file, charset);
		}

		@Override
		public void write(String line) throws Exception {
			_writer.write(line);
			_writer.write('\n');
		}

		@Override
		public void afterLastWrite() throws Exception {
			_writer.close();
			_writer = null;
		}

		@Override
		public void beforeFirstRead() throws Exception {
			_reader = FileTools.openReader(_file, charset);
		}

		@Override
		public String read() throws Exception {
			return FileTools.readLine(_reader);
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
	protected void copy(IPart<String> part) throws Exception {
		FileTools.delete(_wFile);
		FileTools.moveFile(((Part) part).getFile(), _wFile);
	}

	@Override
	protected int getWeight(String line) throws Exception {
		return line.length() + 10;
	}

	@Override
	protected int capacity() throws Exception {
		return 30000000; // 30 M char
	}

	@Override
	protected IPart<String> createPart() throws Exception {
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
