package charlotte.tools;

public class EscapeString {
	public EscapeString() {
		this("\t\r\n ", '$', "trns");
	}

	private String _decChrs;
	private char _escapeChr;
	private String _encChrs;

	public EscapeString(String decChrs, char escapeChr, String encChrs) {
		if(
				decChrs == null ||
				encChrs == null ||
				decChrs.length() != encChrs.length() ||
				StringTools.hasSameChar(decChrs + escapeChr + encChrs)
				) {
			throw new RTError("不正な引数です。" + decChrs + ", " + escapeChr + ", " + encChrs);
		}

		_decChrs = decChrs + escapeChr;
		_escapeChr = escapeChr;
		_encChrs = encChrs + escapeChr;
	}

	public String encode(String str) {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			int chrPos = _decChrs.indexOf(chr);

			if (chrPos == -1) {
				buff.append(chr);
			}
			else {
				buff.append(_escapeChr);
				buff.append(_encChrs.charAt(chrPos));
			}
		}
		return buff.toString();
	}

	public String decode(String str) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < str.length(); index++) {
			char chr = str.charAt(index);

			if(chr == _escapeChr && index + 1 < str.length()) {
				index++;
				chr = str.charAt(index);
				int chrPos = _encChrs.indexOf(chr);

				if(chrPos != -1) {
					chr = _decChrs.charAt(chrPos);
				}
			}
			buff.append(chr);
		}
		return buff.toString();
	}
}
