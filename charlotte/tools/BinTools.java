package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinTools {
	public static final byte[] EMPTY = new byte[0];

	public static Comparator<Byte> comp = new Comparator<Byte>() {
		@Override
		public int compare(Byte a, Byte b) {
			return IntTools.comp.compare(a & 0xff, b & 0xff);
		}
	};

	public static Comparator<byte[]> compArray = new Comparator<byte[]>() {
		@Override
		public int compare(byte[] a, byte[] b) {
			// XXX 効率悪い。
			return ArrayTools.comp(toList(a).toArray(new Byte[a.length]), toList(b).toArray(new Byte[b.length]), comp);
		}
	};

	public static class Hex {
		public static String toString(byte chr) {
			return toString(new byte[] { chr });
		}

		public static String toString(byte[] src) {
			StringBuffer buff = new StringBuffer();

			for(byte chr : src) {
				buff.append(StringTools.hexadecimal.charAt(chr >> 4));
				buff.append(StringTools.hexadecimal.charAt(chr & 0x0f));
			}
			return buff.toString();
		}

		public static byte[] toBytes(String src) {
			if(src.length() % 2 != 0) {
				throw new RTError();
			}
			byte[] dest = new byte[src.length() / 2];

			for(int index = 0; index < dest.length; index++) {
				dest[index] = (byte)((to4Bit(src.charAt(index * 2)) << 4 ) | to4Bit(src.charAt(index * 2 + 1)));
			}
			return dest;
		}

		private static int to4Bit(char chr) {
			int ret = StringTools.hexadecimal.indexOf(chr);

			if(ret == -1) {
				throw new RTError();
			}
			return ret;
		}
	}

	public static byte[] getSubBytes(byte[] src, int beginIndex) {
		return getSubBytes(src, beginIndex, src.length);
	}

	public static byte[] getSubBytes(byte[] src, int beginIndex, int endIndex) {
		byte[] dest = new byte[endIndex - beginIndex];
		System.arraycopy(src, beginIndex, dest, 0, endIndex - beginIndex);
		return dest;
	}

	public static byte[] toBytes(int value) {
		byte[] dest = new byte[4];
		toBytes(value, dest);
		return dest;
	}

	public static void toBytes(int value, byte[] dest) {
		toBytes(value, dest, 0);
	}

	public static void toBytes(int value, byte[] dest, int index) {
		dest[index + 0] = (byte)((value >>> 0) & 0xff);
		dest[index + 1] = (byte)((value >>> 8) & 0xff);
		dest[index + 2] = (byte)((value >>> 16) & 0xff);
		dest[index + 3] = (byte)((value >>> 24) & 0xff);
	}

	public static int toInt(byte[] src) {
		return toInt(src, 0);
	}

	public static int toInt(byte[] src, int index) {
		return
				((src[index + 0] & 0xff) << 0) |
				((src[index + 1] & 0xff) << 8) |
				((src[index + 2] & 0xff) << 16) |
				((src[index + 3] & 0xff) << 24);
	}

	public static byte[] join(byte[][] src) {
		int offset = 0;

		for(byte[] block : src) {
			offset += 4 + block.length;
		}
		byte[] dest = new byte[offset];
		offset = 0;

		for(byte[] block : src) {
			System.arraycopy(toBytes(block.length), 0, dest, offset, 4);
			offset += 4;
			System.arraycopy(block, 0, dest, offset, block.length);
			offset += block.length;
		}
		return dest;
	}

	public static byte[][] split(byte[] src) {
		List<byte[]> dest = new ArrayList<byte[]>();

		for(int offset = 0; offset < src.length; ) {
			int size = toInt(src, offset);
			offset += 4;
			dest.add(getSubBytes(src, offset, size));
			offset += size;
		}
		return dest.toArray(new byte[dest.size()][]);
	}

	public static byte[] toArray(List<Byte> src) {
		int size = src.size();
		byte[] dest = new byte[size];

		for(int index = 0; index < size; index++) {
			dest[index] = src.get(index);
		}
		return dest;
	}

	public static List<Byte> toList(byte[] src) {
		List<Byte> dest = new ArrayList<Byte>();

		for(int index = 0; index < src.length; index++) {
			dest.add(src[index]);
		}
		return dest;
	}
}
