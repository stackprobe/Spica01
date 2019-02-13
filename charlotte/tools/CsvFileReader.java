package charlotte.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvFileReader implements AutoCloseable {
	public char DELIMITER = ',';
	//public char DELIMITER = '\t';

	private BufferedReader _reader;

	public CsvFileReader(String file) throws Exception {
		this(new FileInputStream(file));
	}

	public CsvFileReader(InputStream bindingReader) throws Exception {
		_reader = new BufferedReader(new InputStreamReader(bindingReader, StringTools.CHARSET_SJIS));
	}

	private int _lastChar;

	private int readChar() throws Exception {
		do {
			_lastChar = _reader.read();
		}
		while(_lastChar == '\r');

		return _lastChar;
	}

	private boolean _enclosedCell;

	private String readCell() throws Exception {
		StringBuffer buff = new StringBuffer();

		if(readChar() == '"') {
			while(readChar() != -1 && (_lastChar != '"' || readChar() == '"')) {
				buff.append((char)_lastChar);
			}
			_enclosedCell = true;
		}
		else {
			while(_lastChar != -1 && _lastChar != '\n' && _lastChar != DELIMITER) {
				buff.append((char)_lastChar);
				readChar();
			}
			_enclosedCell = false;
		}
		return buff.toString();
	}

	public List<String> readRow() throws Exception {
		List<String> row = new ArrayList<String>();

		do {
			row.add(readCell());
		}
		while(_lastChar != -1 && _lastChar != '\n');

		if(_lastChar == -1 && row.size() == 1 && row.get(0).isEmpty() && _enclosedCell == false) {
			return null;
		}
		return row;
	}

	public List<String[]> readToEnd() throws Exception {
		List<String[]> rows = new ArrayList<String[]>();

		for(; ; ) {
			List<String> row = readRow();

			if(row == null) {
				break;
			}
			rows.add(row.toArray(new String[row.size()]));
		}
		return rows;
	}

	@Override
	public void close() throws Exception {
		if(_reader != null) {
			_reader.close();
			_reader = null;
		}
	}
}
