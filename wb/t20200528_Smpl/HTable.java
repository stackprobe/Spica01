package wb.t20200528_Smpl;

import java.util.List;
import java.util.Map;

import charlotte.tools.CsvFileReader;
import charlotte.tools.MapTools;

public class HTable implements AutoCloseable {
	private CsvFileReader _reader;
	private List<String> _headerRow;
	private Map<String, Integer> _header2colIdx = MapTools.<Integer>create();
	private List<String> _row;

	public HTable(String file) throws Exception {
		_reader = new CsvFileReader(file);
		_headerRow = _reader.readRow();

		for(int colidx = 0; colidx < _headerRow.size(); colidx++) {
			_header2colIdx.put(_headerRow.get(colidx), colidx);
		}
	}

	public boolean read() throws Exception {
		_row = _reader.readRow();

		if(_row == null) {
			close();
			return false;
		}
		return true;
	}

	public String getCell(String header) {
		return _row.get(_header2colIdx.get(header));
	}

	@Override
	public void close() throws Exception {
		if(_reader != null) {
			_reader.close();
			_reader = null;
		}
	}
}
