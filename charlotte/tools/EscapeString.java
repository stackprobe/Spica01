package charlotte.tools;

public class EscapeString {
	public EscapeString() {
		this("\t\r\n ", '$', "trns");
	}

	private String _disallowedChrs;
	private char _escapeChr;
	private String _allowedChrs;

	public EscapeString(String disallowedChrs, char escapeChr, String allowedChrs) {
		if(
				disallowedChrs == null ||
				allowedChrs == null ||
				disallowedChrs.length() != allowedChrs.length() ||
				StringTools.hasSameChar(disallowedChrs + escapeChr + allowedChrs)
				) {
			throw new RTError("不正な引数です。" + disallowedChrs + ", " + escapeChr + ", " + allowedChrs);
		}

		_disallowedChrs = disallowedChrs + escapeChr;
		_escapeChr = escapeChr;
		_allowedChrs = allowedChrs + escapeChr;
	}

	public String encode(String str) {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			int chrPos = _disallowedChrs.indexOf(chr);

			if(chrPos == -1) {
				buff.append(chr);
			}
			else {
				buff.append(_escapeChr);
				buff.append(_allowedChrs.charAt(chrPos));
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
				int chrPos = _allowedChrs.indexOf(chr);

				if(chrPos != -1) {
					chr = _disallowedChrs.charAt(chrPos);
				}
			}
			buff.append(chr);
		}
		return buff.toString();
	}
}
