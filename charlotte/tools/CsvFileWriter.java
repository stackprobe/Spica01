package charlotte.tools;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class CsvFileWriter implements AutoCloseable {
	public char DELIMITER = ',';
	//public char DELIMITER = '\t';

	private OutputStreamWriter _writer;
	private boolean _rowHead;

	public CsvFileWriter(String file) throws Exception {
		this(file, false);
	}

	public CsvFileWriter(String file, boolean append) throws Exception {
		_writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file, append)), StringTools.CHARSET_SJIS);
		_rowHead = true;
	}

	public void writeCell(String cell) throws Exception {
		if(_rowHead) {
			_rowHead = false;
		}
		else {
			_writer.write(DELIMITER);
		}

		if(
				cell.contains("\"") ||
				cell.contains("\n") ||
				cell.contains("" + DELIMITER)
				) {
			_writer.write('"');
			_writer.write(cell.replace("\"", "\"\""));
			_writer.write('"');
		}
		else {
			_writer.write(cell);
		}
	}

	public void endRow() throws Exception {
		_writer.write('\n');
		_rowHead = true;
	}

	public void writeCells(String[] cells) throws Exception {
		writeCells(ArrayTools.iterable(cells));
	}

	public void writeCells(Iterable<String> cells) throws Exception {
		for(String cell : cells) {
			writeCell(cell);
		}
	}

	public void writeRow(String[] row) throws Exception {
		writeRow(ArrayTools.iterable(row));
	}

	public void writeRow(Iterable<String> row) throws Exception {
		for(String cell : row) {
			writeCell(cell);
		}
		endRow();
	}

	public void writeRows(String[][] rows) throws Exception {
		writeRows(ArrayTools.iterable(rows));
	}

	public void writeRows(Iterable<String[]> rows) throws Exception {
		for(String[] row : rows) {
			writeRow(row);
		}
	}

	@Override
	public void close() throws Exception {
		if(_writer != null) {
			_writer.close();
			_writer = null;
		}
	}
}
