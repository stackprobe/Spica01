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
		HeaderTable.Column column = _header.columns.get(name);

		if(column == null) {
			throw new RTError("Column '" + name + "' does not exist in '" + _header.file + "'");
		}
		return column.index;
	}

	private void touchColumn(int index) {
		while(_row.size() <= index) {
			_row.add(DEFAULT_VALUE);
		}
	}

	public String get(String name) {
		int index = columnIndexOf(name);
		touchColumn(index);
		return _row.get(index);
	}

	public void set(String name, String value) throws Exception {
		int index = columnIndexOf(name);
		touchColumn(index);
		value = cellFilter(value);
		_row.set(index, value);
	}

	public static String cellFilter(String value) throws Exception {
		value = JString.toJString(value, true, true, true, true);

		value = value.replace("\r\n", "\n");
		value = value.replace("\r", "\n");

		return value;
	}
}
