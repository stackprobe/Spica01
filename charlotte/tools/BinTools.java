package charlotte.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
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

	public static Comparator<byte[]> comp_array = new Comparator<byte[]>() {
		@Override
		public int compare(byte[] a, byte[] b) {
			int minlen = Math.min(a.length, b.length);

			for(int index = 0; index < minlen; index++) {
				if(a[index] != b[index]) {
					return (a[index] & 0xff) - (b[index] & 0xff);
				}
			}
			return a.length - b.length;

			//return ListTools.comp(asList(a), asList(b), comp); // old
		}
	};

	public static List<Byte> asList(byte[] inner) {
		return IArrays.asList(wrap(inner));
	}

	public static IArray<Byte> wrap(byte[] inner) {
		return new IArray<Byte>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Byte get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Byte element) {
				inner[index] = element;
			}
		};
	}

	public static int compFile(String file, String file2) throws Exception {
		try(
				FileInputStream nBReader = new FileInputStream(file);
				FileInputStream nBReader2 = new FileInputStream(file2);
				BufferedInputStream reader = new BufferedInputStream(nBReader);
				BufferedInputStream reader2 = new BufferedInputStream(nBReader2);
				) {
			for(; ; ) {
				int chr = reader.read();
				int chr2 = reader2.read();

				if(chr != chr2) {
					return chr - chr2;
				}
				if(chr == -1) {
					return 0;
				}
			}
		}
	}

	public static class Hex {
		public static String toString(byte chr) {
			return toString(new byte[] { chr });
		}

		public static String toString(byte[] src) {
			StringBuffer buff = new StringBuffer();

			for(byte chr : src) {
				buff.append(StringTools.hexadecimal.charAt((chr & 0xf0) >> 4));
				buff.append(StringTools.hexadecimal.charAt((chr & 0x0f) >> 0));
			}
			return buff.toString();
		}

		public static byte[] toBytes(String src) {
			if(src.length() % 2 != 0) {
				throw new RTError("Bad hex-string: " + src);
			}
			byte[] dest = new byte[src.length() / 2];

			for(int index = 0; index < dest.length; index++) {
				dest[index] = (byte)((to4Bit(src.charAt(index * 2)) << 4 ) | to4Bit(src.charAt(index * 2 + 1)));
			}
			return dest;
		}

		private static int to4Bit(char chr) {
			int ret = StringTools.hexadecimal.indexOf(Character.toLowerCase(chr));

			if(ret == -1) {
				throw new RTError("Bad hex-character: " + chr);
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

	public static byte[] toLongBytes(long value) {
		byte[] dest = new byte[8];
		toLongBytes(value, dest);
		return dest;
	}

	public static void toLongBytes(long value, byte[] dest) {
		toLongBytes(value, dest, 0);
	}

	public static void toLongBytes(long value, byte[] dest, int index) {
		dest[index + 0] = (byte)((value >>> 0) & 0xff);
		dest[index + 1] = (byte)((value >>> 8) & 0xff);
		dest[index + 2] = (byte)((value >>> 16) & 0xff);
		dest[index + 3] = (byte)((value >>> 24) & 0xff);
		dest[index + 4] = (byte)((value >>> 32) & 0xff);
		dest[index + 5] = (byte)((value >>> 40) & 0xff);
		dest[index + 6] = (byte)((value >>> 48) & 0xff);
		dest[index + 7] = (byte)((value >>> 56) & 0xff);
	}

	public static long toLong(byte[] src) {
		return toLong(src, 0);
	}

	public static long toLong(byte[] src, int index) {
		return
				((src[index + 0] & 0xffL) << 0) |
				((src[index + 1] & 0xffL) << 8) |
				((src[index + 2] & 0xffL) << 16) |
				((src[index + 3] & 0xffL) << 24) |
				((src[index + 4] & 0xffL) << 32) |
				((src[index + 5] & 0xffL) << 40) |
				((src[index + 6] & 0xffL) << 48) |
				((src[index + 7] & 0xffL) << 56);
	}

	public static byte[] join(byte[][] src) {
		return join(ArrayTools.iterable(src));
	}

	public static byte[] join(Iterable<byte[]> src) {
		int offset = 0;

		for(byte[] block : src) {
			offset += block.length;
		}
		byte[] dest = new byte[offset];
		offset = 0;

		for(byte[] block : src) {
			System.arraycopy(block, 0, dest, offset, block.length);
			offset += block.length;
		}
		return dest;
	}

	public static byte[] splittableJoin(byte[][] src) {
		return splittableJoin(ArrayTools.iterable(src));
	}

	public static byte[] splittableJoin(Iterable<byte[]> src) {
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

	public static List<byte[]> split(byte[] src) {
		List<byte[]> dest = new ArrayList<byte[]>();

		for(int offset = 0; offset < src.length; ) {
			int size = toInt(src, offset);
			offset += 4;
			dest.add(getSubBytes(src, offset, offset + size));
			offset += size;
		}
		return dest;
	}

	public static List<byte[]> divide(byte[] src, int partSize) {
		if(src.length % partSize != 0) {
			throw new IllegalArgumentException("Bad part size: " + partSize + ", " + src.length);
		}
		List<byte[]> dest = new ArrayList<byte[]>();

		for(int offset = 0; offset < src.length; offset += partSize) {
			dest.add(getSubBytes(src, offset, offset + partSize));
		}
		return dest;
	}

	public static byte[] toArray(List<Byte> src) {
		int size = src.size();
		byte[] dest = new byte[size];

		for(int index = 0; index < size; index++) {
			dest[index] = src.get(index);
		}
		return dest;
	}

	/*
	public static List<Byte> toList(byte[] src) {
		List<Byte> dest = new ArrayList<Byte>();

		for(int index = 0; index < src.length; index++) {
			dest.add(src[index]);
		}
		return dest;
	}
	*/
}
