package wb.t20181211;

import java.util.Map;
import java.util.TreeMap;

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

	private Map<Character, Character> _half = createCharPairs();
	private Map<Character, Character> _hira = createCharPairs();
	private Map<Character, Character> _kata = createCharPairs();

	private Map<Character, Character> createCharPairs() {
		return new TreeMap<Character, Character>((a, b) -> (a.charValue() & 0xffff) - (b.charValue() & 0xffff));
	}

	private StrConv() {
		ObjectTree res = new ObjectTree(RTError.get(() -> JsonTools.decode(FileTools.readToEnd(StrConv.class.getResource("res/StrConv.json")))));

		for(ObjectTree row : res) {
			char half = toChar(row.get(0));
			char hira = toChar(row.get(1));
			char kata = toChar(row.get(2));

			putCharPair(_half, hira, half);
			putCharPair(_half, kata, half);

			putCharPair(_hira, half, hira);
			putCharPair(_hira, kata, hira);

			putCharPair(_kata, half, kata);
			putCharPair(_kata, hira, kata);
		}
	}

	private char toChar(ObjectTree cell) {
		return (char)Integer.parseInt(cell.asString(), 16);
	}

	private void putCharPair(Map<Character, Character> dest, char rChr, char wChr) {
		if(rChr != wChr) {
			dest.put(rChr, wChr);
		}
	}

	public String toHalf(String str) {
		return conv(str, _half);
	}

	public String toHira(String str) {
		return conv(str, _hira);
	}

	public String toKata(String str) {
		return conv(str, _kata);
	}

	private String conv(String str, Map<Character, Character> pairs) {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			Character destChr = pairs.get(chr);

			if(destChr == null) {
				buff.append(chr);
			}
			else {
				buff.append(destChr);
			}
		}
		return buff.toString();
	}
}
