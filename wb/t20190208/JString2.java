package wb.t20190208;

import java.io.ByteArrayOutputStream;

import charlotte.tools.JString;
import charlotte.tools.StringTools;

public class JString2 {
	public static String filter(String str) throws Exception {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			if(AllowedCharacters.i().contains(chr)) {
				buff.append(chr);
			}
			else {
				buff.append(String.format("\\u%04x", chr & 0xffff));
			}
		}
		return buff.toString();
	}

	private static class AllowedCharacters {
		private static AllowedCharacters _i = null;

		public static AllowedCharacters i() throws Exception {
			if(_i == null) {
				_i = new AllowedCharacters();
			}
			return _i;
		}

		private int[] _bits = new int[2048];

		private AllowedCharacters() throws Exception {
			add('\t');
			add('\n');
			add(' ');

			for(char chr : StringTools.HALF.toCharArray()) {
				add(chr);
			}

			try(ByteArrayOutputStream mem = new ByteArrayOutputStream(0x20000)) {
				for(int chr = 0x0000; chr <= 0xffff; chr++) {
					if(JString.JChar.i().contains(chr)) {
						mem.write(chr >> 8);
						mem.write(chr & 0xff);
					}
				}

				for(char chr : mem.toString(StringTools.CHARSET_SJIS).toCharArray()) {
					add(chr);
				}
			}
		}

		private void add(int chr) {
			_bits[(chr & 0x0000ffe0) >>> 5] |= 1 << (chr & 0x0000001f);
		}

		public boolean contains(int chr) {
			return (_bits[(chr & 0x0000ffe0) >>> 5] & (1 << (chr & 0x0000001f))) != 0;
		}
	}
}
