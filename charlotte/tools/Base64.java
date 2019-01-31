package charlotte.tools;

public class Base64 {
	public static Base64 createByC6364P(String c6364P) {
		return new Base64(StringTools.ALPHA + StringTools.alpha + StringTools.DECIMAL + c6364P);
	}

	public Base64() {
		this(StringTools.ALPHA + StringTools.alpha + StringTools.DECIMAL + "+/=");
	}

	private char[] _chrs;
	private byte[] _chrMap;

	public Base64(String chrs) {
		if(chrs.length() != 65) {
			throw new IllegalArgumentException();
		}
		if(StringTools.hasSameChar(chrs)) {
			throw new IllegalArgumentException();
		}
		_chrs = chrs.toCharArray();
		_chrMap = new byte[Character.MAX_VALUE + 1];

		for(int index = 0; index < 64; index++) {
			_chrMap[_chrs[index]] = (byte)index;
		}
	}

	public String encode(byte[] src) {
		char[] dest = new char[((src.length + 2) / 3) * 4];
		int writer = 0;
		int index = 0;
		int chr;

		while(index + 3 <= src.length) {
			chr = (src[index++] & 0xff) << 16;
			chr |= (src[index++] & 0xff) << 8;
			chr |= src[index++] & 0xff;
			dest[writer++] = _chrs[chr >>> 18];
			dest[writer++] = _chrs[(chr >>> 12) & 0x3f];
			dest[writer++] = _chrs[(chr >>> 6) & 0x3f];
			dest[writer++] = _chrs[chr & 0x3f];
		}
		if(index + 2 == src.length) {
			chr = (src[index++] & 0xff) << 8;
			chr |= src[index++] & 0xff;
			dest[writer++] = _chrs[chr >>> 10];
			dest[writer++] = _chrs[(chr >>> 4) & 0x3f];
			dest[writer++] = _chrs[(chr << 2) & 0x3c];
			dest[writer++] = _chrs[64];
		}
		else if(index + 1 == src.length) {
			chr = src[index++] & 0xff;
			dest[writer++] = _chrs[chr >>> 2];
			dest[writer++] = _chrs[(chr << 4) & 0x30];
			dest[writer++] = _chrs[64];
			dest[writer++] = _chrs[64];
		}
		return new String(dest);
	}

	public byte[] decode(String src) {
		int destSize = (src.length() / 4) * 3;

		if(destSize != 0) {
			if(src.charAt(src.length() - 2) == _chrs[64]) {
				destSize -= 2;
			}
			else if(src.charAt(src.length() - 1) == _chrs[64]) {
				destSize--;
			}
		}
		byte[] dest = new byte[destSize];
		int writer = 0;
		int index = 0;
		int chr;

		while(writer + 3 <= destSize) {
			chr = (_chrMap[src.charAt(index++)] & 0x3f) << 18;
			chr |= (_chrMap[src.charAt(index++)] & 0x3f) << 12;
			chr |= (_chrMap[src.charAt(index++)] & 0x3f) << 6;
			chr |= _chrMap[src.charAt(index++)] & 0x3f;
			dest[writer++] = (byte)(chr >>> 16);
			dest[writer++] = (byte)((chr >>> 8) & 0xff);
			dest[writer++] = (byte)(chr & 0xff);
		}
		if(writer + 2 == destSize) {
			chr = (_chrMap[src.charAt(index++)] & 0x3f) << 10;
			chr |= (_chrMap[src.charAt(index++)] & 0x3f) << 4;
			chr |= (_chrMap[src.charAt(index++)] & 0x3c) >>> 2;
			dest[writer++] = (byte)(chr >>> 8);
			dest[writer++] = (byte)(chr & 0xff);
		}
		else if(writer + 1 == destSize) {
			chr = (_chrMap[src.charAt(index++)] & 0x3f) << 2;
			chr |= (_chrMap[src.charAt(index++)] & 0x30) >>> 4;
			dest[writer++] = (byte)chr;
		}
		return dest;
	}

	public String removePadding(String data) {
		if(data.length() != 0) {
			if(data.charAt(data.length() - 2) == _chrs[64]) {
				return data.substring(0, data.length() - 2);
			}
			if(data.charAt(data.length() - 1) == _chrs[64]) {
				return data.substring(0, data.length() - 1);
			}
		}
		return data;
	}

	public String addPadding(String data) {
		int rem = data.length() % 4;

		if(rem == 2) {
			data = data + _chrs[64] + _chrs[64];
		}
		else if(rem == 3) {
			data = data + _chrs[64];
		}
		return data;
	}

	public NoPadding noPadding() {
		return new NoPadding(this);
	}

	public static class NoPadding {
		private Base64 _inner;

		public NoPadding(Base64 inner) {
			_inner = inner;
		}

		public String encode(byte[] src) {
			return _inner.removePadding(_inner.encode(src));
		}

		public byte[] decode(String src) {
			return _inner.decode(_inner.addPadding(src));
		}
	}
}
