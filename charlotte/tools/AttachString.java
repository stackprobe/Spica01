package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class AttachString {
	public AttachString() {
		this("\t\r\n ", "trns");
	}

	public AttachString(String allowedChrs, String disallowedChrs) {
		this(':', '$', '.', allowedChrs, disallowedChrs);
	}

	public AttachString(char delimiter, char escapeChr, char escapedDelimiter) {
		this(delimiter, escapeChr, escapedDelimiter, "", "");
	}

	public AttachString(char delimiter, char escapeChr, char escapedDelimiter, String allowedChrs, String disallowedChrs) {
		this(
				delimiter,
				new EscapeString(
						allowedChrs + delimiter,
						escapeChr,
						disallowedChrs + escapedDelimiter
						));
	}

	private char _delimiter;
	private EscapeString _es;

	public AttachString(char delimiter, EscapeString es) {
		_delimiter = delimiter;
		_es = es;
	}

	public String untokenize(String[] tokens) {
		return untokenize(ArrayTools.iterable(tokens));
	}

	public String untokenize(Iterable<String> tokens) {
		List<String> dest = new ArrayList<String>();

		for(String token : tokens) {
			dest.add(_es.encode(token));
		}
		dest.add("");
		return String.join("" + _delimiter, dest);
	}

	public List<String> tokenize(String str) {
		List<String> dest = new ArrayList<String>();

		for(String token : StringTools.tokenize(str, "" + _delimiter)) {
			dest.add(_es.decode(token));
		}
		dest.remove(dest.size() - 1);
		return dest;
	}
}
