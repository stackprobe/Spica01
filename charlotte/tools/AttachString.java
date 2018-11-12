package charlotte.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttachString {
	public AttachString() {
		this("\t\r\n ", "trns");
	}

	public AttachString(String decChrs, String encChrs) {
		this(':', '$', '.', decChrs, encChrs);
	}

	public AttachString(char delimiter, char escapeChr, char escapeDelimiter) {
		this(delimiter, escapeChr, escapeDelimiter, "", "");
	}

	public AttachString(char delimiter, char escapeChr, char escapeDelimiter, String decChrs, String encChrs) {
		this(
				delimiter,
				new EscapeString(
						decChrs + delimiter,
						escapeChr,
						encChrs + escapeDelimiter
						));
	}

	private char _delimiter;
	private EscapeString _es;

	public AttachString(char delimiter, EscapeString es) {
		_delimiter = delimiter;
		_es = es;
	}

	public String untokenize(String[] tokens) {
		return untokenize(Arrays.asList(tokens));
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
