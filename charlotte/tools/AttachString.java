package charlotte.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttachString {
	public AttachString() {
		this(':', '$', '.');
	}

	public AttachString(char delimiter, char escapeChr, char escapedDelimiter) {
		this(
				delimiter,
				new EscapeString(
						new String(new char[] { delimiter }),
						escapeChr,
						new String(new char[] { escapedDelimiter })
						));
	}

	private char _delimiter;
	private EscapeString _es;

	public AttachString(char delimiter, EscapeString es) {
		_delimiter = delimiter;
		_es = es;
	}

	public String untokenize(String[] tokens) {
		return untokenize(ArrayTools.iterator(tokens));
	}

	public String untokenize(Iterator<String> tokens) {
		List<String> dest = new ArrayList<String>();

		for(String token : IterableTools.once(tokens)) {
			dest.add(_es.encode(token));
		}
		dest.add("");
		return String.join(new String(new char[] { _delimiter }), dest);
	}

	public List<String> tokenize(String str) {
		List<String> dest = new ArrayList<String>();

		for(String token : StringTools.tokenize(str, new String(new char[] { _delimiter }))) {
			dest.add(_es.decode(token));
		}
		dest.remove(dest.size() - 1);
		return dest;
	}
}
