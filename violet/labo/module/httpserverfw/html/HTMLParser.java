package violet.labo.module.httpserverfw.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import charlotte.tools.MapTools;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;

public class HTMLParser {
	private String _html;
	private int _rPos = 0;

	public HTMLParser(String html) {
		_html = html;
	}

	private char charAt(int index) {
		if(_html.length() <= index) {
			throw new ReachedEndOfHTML();
		}
		return _html.charAt(index);
	}

	private char curr() {
		return charAt(_rPos);
	}

	private char next() {
		return charAt(_rPos++);
	}

	private void back() {
		back(1);
	}

	private void back(int range) {
		_rPos -= range;
	}

	private void nextWhile(Predicate<Character> match) {
		while(match.test(next())) {
			// noop
		}
		back();
	}

	private void nextUntil(Predicate<Character> match) {
		nextWhile(chr -> match.test(chr) == false);
	}

	private List<Object> _sequence = new ArrayList<Object>();

	public void parse() {
		int index = 0;

		for(; ; ) {
			Tag tag = nextTag();

			if(tag == null) {
				break;
			}
			_sequence.add(_html.substring(index, tag.start));
			_sequence.add(tag);

			index = tag.end;
			_rPos = index;
		}
		_sequence.add(_html.substring(index));
	}

	private Tag nextTag() {
		while(_rPos < _html.length()) {
			if(next() == '<') {
				int savedPos = _rPos;
				try {
					return getTag();
				}
				catch(ParseTagFault e) {
					System.out.println(e.getMessage());
				}
				catch(ReachedEndOfHTML e) {
					System.out.println("Reached end of HTML");
				}
				_rPos = savedPos;
			}
		}
		return null;
	}

	private static final String ASCII_CLASS_NAME_CHRS = StringTools.DECIMAL + StringTools.ALPHA + StringTools.alpha + "_";

	private Tag getTag() throws ParseTagFault {
		Tag tag = new Tag();

		tag.start = _rPos - 1;

		nextWhile(chr -> chr <= ' ');

		if(curr() == '/') {
			_rPos++;
			tag.closing = true;
		}
		int namePos = _rPos;
		nextUntil(chr -> chr <= ' ' || chr == '/' || chr == '>');
		tag.name = _html.substring(namePos, _rPos);

		if(StringTools.ALPHA.indexOf(tag.name.charAt(0)) == -1) {
			throw new ParseTagFault("Tag name is not started with UPPER-ALPHA: " + tag.name);
		}
		if(StringTools.contains(tag.name, chr -> ASCII_CLASS_NAME_CHRS.indexOf(chr) == -1)) {
			throw new ParseTagFault("Tag name is not US-ASCII class name: " + tag.name);
		}
		//tag.attributes = MapTools.<String>createOrdered();
		tag.attributes = MapTools.<String>createOrderedIgnoreCase();

		for(; ; ) {
			nextWhile(chr -> chr <= ' ');

			if(curr() == '/') {
				_rPos++;
				nextWhile(chr -> chr <= ' ');

				if(next() != '>') {
					throw new ParseTagFault("Bad self-closing tag");
				}
				tag.selfClosing = true;
				break;
			}
			if(curr() == '>') {
				_rPos++;
				break;
			}
			int attrNamePos = _rPos;
			nextUntil(chr -> chr <= ' ' || chr == '=');
			String attrName = _html.substring(attrNamePos, _rPos);

			nextWhile(chr -> chr <= ' ');

			if(next() != '=') {
				throw new ParseTagFault("Bad attribute delimiter");
			}
			nextWhile(chr -> chr <= ' ');

			if(next() != '"') {
				throw new ParseTagFault("Attribute value is not started with (\")");
			}
			int attrValuePos = _rPos;
			nextUntil(chr -> chr == '"');
			String attrValue = _html.substring(attrValuePos, _rPos);
			_rPos++;

			attrValue = decodeLiteral(attrValue);

			tag.attributes.put(attrName, attrValue);
		}
		tag.end = _rPos;

		if(tag.closing && tag.selfClosing) {
			throw new ParseTagFault("Closing tag has self-closing mark");
		}
		if(tag.closing && 1 <= tag.attributes.size()) {
			throw new ParseTagFault("Closing tag has some attributes");
		}
		return tag;
	}

	private String decodeLiteral(String value) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < value.length(); index++) {
			char chr = value.charAt(index);

			if(chr == '\\') {
				chr = value.charAt(++index);

				if(chr == '\\') {
					buff.append('\\');
				}
				else if(chr == 't') {
					buff.append('\t');
				}
				else if(chr == 'r') {
					buff.append('\r');
				}
				else if(chr == 'n') {
					buff.append('\n');
				}
				else if(chr == 'q') {
					buff.append('"');
				}
				else if(
						chr == 'u' &&
						StringTools.containsIgnoreCase(StringTools.hexadecimal, value.charAt(index + 1)) &&
						StringTools.containsIgnoreCase(StringTools.hexadecimal, value.charAt(index + 2)) &&
						StringTools.containsIgnoreCase(StringTools.hexadecimal, value.charAt(index + 3)) &&
						StringTools.containsIgnoreCase(StringTools.hexadecimal, value.charAt(index + 4))
						) {
					buff.append((char)(
							(StringTools.indexOfIgnoreCase(StringTools.hexadecimal, value.charAt(index + 1)) * 0x1000) |
							(StringTools.indexOfIgnoreCase(StringTools.hexadecimal, value.charAt(index + 2)) * 0x100) |
							(StringTools.indexOfIgnoreCase(StringTools.hexadecimal, value.charAt(index + 3)) * 0x10) |
							(StringTools.indexOfIgnoreCase(StringTools.hexadecimal, value.charAt(index + 4)) * 0x1)
							));
					index += 4;
				}
				else {
					throw new RTError("Bad literal");
				}
			}
			else {
				buff.append(chr);
			}
		}
		return buff.toString();
	}

	public List<Object> sequence() {
		return _sequence;
	}

	public class Tag {
		public int start;
		public int end;
		public String name;
		public Map<String, String> attributes;
		public boolean closing = false;
		public boolean selfClosing = false;
	}

	private class ParseTagFault extends Exception {
		public ParseTagFault(String message) {
			super(message);
		}
	}

	private class ReachedEndOfHTML extends RuntimeException {
		// none
	}
}
