package charlotte.tools;

public class AutoTable<T> {
	private AutoList<AutoList<T>> _rows;

	public int w;
	public int h;

	public AutoTable(int w, int h) {
		if(w < 1 || h < 1) {
			throw new IllegalArgumentException();
		}
		_rows = new AutoList<AutoList<T>>(h);

		this.w = w;
		this.h = h;
	}

	private AutoList<T> getRow(int y) {
		AutoList<T> row = _rows.get(y);

		if(row == null) {
			row = new AutoList<T>(this.w);
			_rows.set(y, row);
		}
		return row;
	}

	public T get(int x, int y) {
		return getRow(y).get(x);
	}

	public T get(int x, int y, T defval) {
		return getRow(y).get(x, defval);
	}

	public void set(int x, int y, T value) {
		getRow(y).set(x, value);
	}
}
