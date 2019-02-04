package violet.labo.module.csvtable;

import java.util.List;

import charlotte.tools.JString;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;

public class HeaderRow {
	private static final String DEFAULT_VALUE = StringTools.EMPTY;

	private HeaderTable.Header _header;
	private List<String> _row;
	private boolean _deleted;

	public HeaderRow(HeaderTable.Header header, List<String> row) {
		_header = header;
		_row = row;
	}

	public List<String> row() {
		return _row;
	}

	public void delete() {
		_deleted = true;
	}

	public boolean isDeleted() {
		return _deleted;
	}

	private int columnIndexOf(String name) {
		Integer colidx = _header.indexes.get(name);

		if(colidx == null) {
			throw new RTError("Unknown column name: " + name);
		}
		return colidx.intValue();
	}

	private void touchColumn(int colidx) {
		while(_row.size() <= colidx) {
			_row.add(DEFAULT_VALUE);
		}
	}

	private String valueFilter(String value) throws Exception {
		return JString.toJString(value, true, true, true, true);
	}

	public void set(String name, String value) throws Exception {
		int colidx = columnIndexOf(name);
		touchColumn(colidx);
		value = valueFilter(value);
		_row.set(colidx, value);
	}

	public String get(String name) {
		int colidx = columnIndexOf(name);
		touchColumn(colidx);
		return _row.get(colidx);
	}
}
