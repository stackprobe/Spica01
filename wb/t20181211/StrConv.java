package wb.t20181211;

import charlotte.tools.FileTools;
import charlotte.tools.JsonTools;
import charlotte.tools.ObjectTree;
import charlotte.tools.RTError;

public class StrConv {
	private static StrConv _i = null;

	public static StrConv i() {
		if(_i == null) {
			_i = new StrConv();
		}
		return _i;
	}

	private String _half;
	private String _hira;
	private String _kata;

	private StrConv() {
		ObjectTree res = new ObjectTree(RTError.get(() -> JsonTools.decode(FileTools.readToEnd(StrConv.class.getResource("res/StrConv.json")))));

		// TODO
	}
}
