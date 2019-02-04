package violet.labo.module.csvtable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.MapTools;

public class HeaderTable {
	public class Header {
		public List<String> names;
		public Map<String, Integer> indexes = MapTools.<Integer>create();
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
			for(int colidx = 0; colidx < row.size(); colidx++) {
				String name = row.get(colidx);

				if(_header.indexes.containsKey(name)) {
					throw new IllegalArgumentException();
				}
				_header.indexes.put(name, colidx);
			}
			_header.names = row;
		}
		_file = file;
	}

	public void readToEnd(Function<HeaderRow, Boolean> routine) throws Exception {
		try(CsvFileReader reader = new CsvFileReader(_file)) {
			reader.readRow(); // skip header

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

	public void update(Consumer<HeaderRow> routine) throws Exception {
		String rFile = _file + ".activated-r.csv";
		String wFile = _file + ".activated-w.csv";

		if(
				new File(_file).exists() == false ||
				new File(rFile).exists() ||
				new File(wFile).exists()
				) {
			throw new IllegalArgumentException();
		}

		FileTools.moveFile(_file, rFile);

		try(
				CsvFileReader reader = new CsvFileReader(rFile);
				CsvFileWriter writer = new CsvFileWriter(wFile);
				) {
			reader.readRow(); // skip header

			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				HeaderRow hr = new HeaderRow(_header, row);

				routine.accept(hr);

				if(hr.isDeleted() == false) {
					writer.writeRow(hr.row());
				}
			}
		}

		FileTools.moveFile(wFile, _file);
		FileTools.delete(rFile);
	}

	public HeaderRow createRow() {
		return new HeaderRow(_header, new ArrayList<String>());
	}

	public void addRow(HeaderRow hr) throws Exception {
		try(CsvFileWriter writer = new CsvFileWriter(_file, true)) {
			writer.writeRow(hr.row());
		}
	}

	public void addRows(Iterable<HeaderRow> hrs) throws Exception {
		try(CsvFileWriter writer = new CsvFileWriter(_file, true)) {
			for(HeaderRow hr : hrs) {
				writer.writeRow(hr.row());
			}
		}
	}
}
