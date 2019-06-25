package charlotte.tools;

import java.util.function.Function;

public class JsonTools {
	public static String encode(Object src) {
		return encode(src, false);
	}

	public static String encode(Object src, boolean noBlank) {
		Encoder e = new Encoder();

		if(noBlank) {
			e.BLANK = "";
			e.INDENT = "";
			e.NEW_LINE = "";
		}
		e.add(src, 0);

		return e.getString();
	}

	private static class Encoder {
		public String BLANK = " ";
		public String INDENT = "\t";
		public String NEW_LINE = "\r\n";

		private StringBuilder _buff = new StringBuilder();

		public void add(Object src, int indent) {
			if(src instanceof ObjectMap) {
				ObjectMap om =(ObjectMap)src;
				boolean secondOrLater = false;

				_buff.append("{");
				_buff.append(NEW_LINE);

				for(String key : om.keys()) {
					Object value = om.get(key);

					if(secondOrLater) {
						_buff.append(",");
						_buff.append(NEW_LINE);
					}
					addIndent(indent + 1);
					add(key, 0); // key is String
					_buff.append(BLANK);
					_buff.append(":");
					_buff.append(BLANK);
					add(value, indent + 1);

					secondOrLater = true;
				}
				_buff.append(NEW_LINE);
				addIndent(indent);
				_buff.append("}");
			}
			else if(src instanceof ObjectList) {
				ObjectList ol =(ObjectList)src;
				boolean secondOrLater = false;

				_buff.append("[");
				_buff.append(NEW_LINE);

				for(Object value : ol.direct()) {
					if(secondOrLater) {
						_buff.append(",");
						_buff.append(NEW_LINE);
					}
					addIndent(indent + 1);
					add(value, indent + 1);

					secondOrLater = true;
				}
				_buff.append(NEW_LINE);
				addIndent(indent);
				_buff.append("]");
			}
			else if(src instanceof Word) {
				_buff.append("" +((Word)src).value);
			}
			else { //if(src instanceof String) {
				String str = "" + src;
				//String str =(String)src;

				_buff.append("\"");

				for(char chr : str.toCharArray()) {
					if(chr == '"') {
						_buff.append("\\\"");
					}
					else if(chr == '\\') {
						_buff.append("\\\\");
					}
					else if(chr == '\b') {
						_buff.append("\\b");
					}
					else if(chr == '\f') {
						_buff.append("\\f");
					}
					else if(chr == '\n') {
						_buff.append("\\n");
					}
					else if(chr == '\r') {
						_buff.append("\\r");
					}
					else if(chr == '\t') {
						_buff.append("\\t");
					}
					else {
						_buff.append(chr);
					}
				}
				_buff.append("\"");
			}
		}

		private void addIndent(int count) {
			while(0 < count) {
				_buff.append(INDENT);
				count--;
			}
		}

		public String getString() {
			return _buff.toString();
		}
	}

	public static Object decode(byte[] src) throws Exception {
		return decode(new String(src, getCharset(src)));
	}

	private static String getCharset(byte[] src) {
		if(4 <= src.length) {
			String x4 = BinTools.Hex.toString(BinTools.getSubBytes(src, 0, 4));

			if("0000feff" == x4 || "fffe0000" == x4) {
				return StringTools.CHARSET_UTF32;
			}
		}
		if(2 <= src.length) {
			String x2 = BinTools.Hex.toString(BinTools.getSubBytes(src, 0, 2));

			if("feff" == x2 || "fffe" == x2) {
				return StringTools.CHARSET_UTF16;
			}
		}
		return StringTools.CHARSET_UTF8;
	}

	public static Object decode(String src) {
		return new Decoder(src).getObject();
	}

	private static class Decoder {
		public String _src;

		public Decoder(String src) {
			_src = src;
		}

		private int _rPos = 0;

		private char next() {
			return _src.charAt(_rPos++);
		}

		private char nextNS() {
			char chr;

			do {
				chr = next();
			}
			while(chr <= ' ' || chr == 0xfeff); // ? control code || space || BOM

			return chr;
		}

		private char nextNS(String allowedChrs) {
			char chr = nextNS();

			if(StringTools.contains(allowedChrs, chr) == false) {
				throw new RTError("JSON format error: " + allowedChrs + ", " + chr);
			}
			return chr;
		}

		public Object getObject() {
			return getObject(nextNS());
		}

		public Object getObject(char chr) {
			if(chr == '{') {
				ObjectMap om = ObjectMap.createIgnoreCase();

				if((chr = nextNS()) != '}') {
					for(; ; ) {
						Object key = getObject(chr);

						if(key instanceof String == false) {
							System.out.println("JSON format warning: key is not String");
						}
						nextNS(":");

						om.put(key, getObject());

						if(nextNS(",}") == '}') {
							break;
						}
						if((chr = nextNS()) == '}') {
							System.out.println("JSON format warning: found ',' before '}'");
							break;
						}
					}
				}
				return om;
			}
			if(chr == '[') {
				ObjectList ol = new ObjectList();

				if((chr = nextNS()) != ']') {
					for(; ; ) {
						ol.add(getObject(chr));

						if(nextNS(",]") == ']') {
							break;
						}
						if((chr = nextNS()) == ']') {
							System.out.println("JSON format warning: found ',' before ']'");
							break;
						}
					}
				}
				return ol;
			}
			if(chr == '"') {
				StringBuilder buff = new StringBuilder();

				for(; ; ) {
					chr = next();

					if(chr == '"') {
						break;
					}
					if(chr == '\\') {
						chr = next();

						if(chr == 'b') {
							chr = '\b';
						}
						else if(chr == 'f') {
							chr = '\f';
						}
						else if(chr == 'n') {
							chr = '\n';
						}
						else if(chr == 'r') {
							chr = '\r';
						}
						else if(chr == 't') {
							chr = '\t';
						}
						else if(chr == 'u') {
							char c1 = next();
							char c2 = next();
							char c3 = next();
							char c4 = next();

							chr =(char)Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
						}
					}
					buff.append(chr);
				}
				String str = buff.toString();
				str = decodeStringFilter.apply(str);
				return str;
			}

			{
				StringBuffer buff = new StringBuffer();

				buff.append(chr);

				while(_rPos < _src.length()) {
					chr = next();

					if(
							chr == '}' ||
							chr == ']' ||
							chr == ',' ||
							chr == ':'
							) {
						_rPos--;
						break;
					}
					buff.append(chr);
				}
				Word word = new Word(buff.toString().trim());

				if(word.isFairJsonWord() == false) {
					System.out.println("JSON format warning: value is not fair JSON word");
				}
				word.value = decodeStringFilter.apply(word.value);
				return word;
			}
		}
	}

	public static class Word {
		public String value;

		public Word(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}

		public boolean isFairJsonWord() {
			return
					value.equals("true") ||
					value.equals("false") ||
					value.equals("null") ||
					isNumber();
		}

		private boolean isNumber() { // XXX
			return StringTools.liteValidate(value, StringTools.DECIMAL + "+-.Ee");
			/*
			String fmt = value;

			fmt = StringTools.replaceChars(fmt, StringTools.DECIMAL + "+-.Ee", '9');
			fmt = StringTools.replaceLoop(fmt, "99", "9");

			return fmt.equals("9");
			*/

			/*
			try {
				double d = Double.parseDouble(value);

				if(Double.isNaN(d)) {
					return false;
				}
				if(Double.isInfinite(d)) {
					return false;
				}
				return true;
			}
			catch(NumberFormatException e) {
				return false;
			}
			*/
		}
	}

	public static Function<String, String> decodeStringFilter = str -> str;
}
