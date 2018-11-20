package charlotte.tools;

public class EscapeString {
	public EscapeString() {
		this("\t\r\n ", '$', "trns");
	}

	private String _allowedChrs;
	private char _escapeChr;
	private String _disallowedChrs;

	public EscapeString(String allowedChrs, char escapeChr, String disallowedChrs) {
		if(
				allowedChrs == null ||
				disallowedChrs == null ||
				allowedChrs.length() != disallowedChrs.length() ||
				StringTools.hasSameChar(allowedChrs + escapeChr + disallowedChrs)
				) {
			throw new RTError("不正な引数です。" + allowedChrs + ", " + escapeChr + ", " + disallowedChrs);
		}

		_allowedChrs = allowedChrs + escapeChr;
		_escapeChr = escapeChr;
		_disallowedChrs = disallowedChrs + escapeChr;
	}

	public String encode(String str) {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			int chrPos = _allowedChrs.indexOf(chr);

			if(chrPos == -1) {
				buff.append(chr);
			}
			else {
				buff.append(_escapeChr);
				buff.append(_disallowedChrs.charAt(chrPos));
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
				int chrPos = _disallowedChrs.indexOf(chr);

				if(chrPos != -1) {
					chr = _allowedChrs.charAt(chrPos);
				}
			}
			buff.append(chr);
		}
		return buff.toString();
	}
}
