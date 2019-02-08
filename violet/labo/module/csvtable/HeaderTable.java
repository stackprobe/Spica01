package violet.labo.module.csvtable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import charlotte.tools.ConsumerEx;
import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.MapTools;
import charlotte.tools.RTError;

public class HeaderTable {
	public class Column {
		public String name;
		public int index;
	}

	public class Header {
		public Map<String, Column> columns = MapTools.<Column>create();
		public List<Column> columnList = new ArrayList<Column>();
		public String file;
	}

	private Header _header = new Header();
	private String _file;

	public HeaderTable(String file) throws Exception {
		try(CsvFileReader reader = new CsvFileReader(file)) {
			List<String> row = reader.readRow();

			if(row == null) {
				throw new IllegalArgumentException();
			}
			if(row.size() == 0) {
				throw new IllegalArgumentException();
			}
			for(int index = 0; index < row.size(); index++) {
				String name = row.get(index);

				Column column = new Column();

				column.name = name;
				column.index = index;

				_header.columns.put(name, column);
				_header.columnList.add(column);
			}
		}
		_header.file = file;
		_file = file;
	}

	public void readToEnd(Function<HeaderRow, Boolean> routine) throws Exception {
		try(CsvFileReader reader = new CsvFileReader(_file)) {
			reader.readRow(); // header

			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				if(routine.apply(new HeaderRow(_header, row)) == false) {
					break;
				}
			}
		}
	}

	public void update(ConsumerEx<HeaderRow> routine) throws Exception {
		String rFile = _file + ".active-original.csv";
		String wFile = _file + ".active-new.csv";

		if(
				new File(_file).exists() == false ||
				new File(rFile).exists() ||
				new File(wFile).exists()
				) {
			throw new IllegalArgumentException();
		}
		Throwable ex = null;

		FileTools.moveFile(_file, rFile);

		try(
				CsvFileReader reader = new CsvFileReader(rFile);
				CsvFileWriter writer = new CsvFileWriter(wFile);
				) {
			writer.writeRow(reader.readRow()); // header

			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				HeaderRow hr = new HeaderRow(_header, row);

				try {
					routine.accept(hr);
				}
				catch(Throwable e) {
					ex = e;
					break;
				}

				if(hr.isDeleted() == false) {
					writer.writeRow(hr.row());
				}
			}
		}

		if(ex != null) {
			FileTools.moveFile(rFile, _file);
			FileTools.delete(wFile);

			throw RTError.re(ex);
		}
		FileTools.moveFile(wFile, _file);
		FileTools.delete(rFile);
	}

	public HeaderRow createRow() {
		return new HeaderRow(_header, new ArrayList<String>());
	}

	public void addRow(HeaderRow hr) throws Exception {
		addRows(ListTools.one(hr));
	}

	public void addRows(Iterable<HeaderRow> hrs) throws Exception {
		String rwFile = _file + ".active.csv";

		if(
				new File(_file).exists() == false ||
				new File(rwFile).exists()
				) {
			throw new IllegalArgumentException();
		}

		FileTools.moveFile(_file, rwFile);

		try(CsvFileWriter writer = new CsvFileWriter(rwFile, true)) {
			for(HeaderRow hr : hrs) {
				writer.writeRow(hr.row());
			}
		}

		FileTools.moveFile(rwFile, _file);
	}
}
