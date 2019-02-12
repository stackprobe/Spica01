package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class CharTools {
	public static Comparator<Character> comp = new Comparator<Character>() {
		@Override
		public int compare(Character a, Character b) {
			return (a.charValue() & 0xffff) - (b.charValue() & 0xffff);
		}
	};

	public static Comparator<char[]> comp_array = new Comparator<char[]>() {
		@Override
		public int compare(char[] a, char[] b) {
			return ListTools.comp(asList(a), asList(b), comp);
		}
	};

	public static List<Character> asList(char[] inner) {
		return IArrays.asList(wrap(inner));
	}

	public static IArray<Character> wrap(char[] inner) {
		return new IArray<Character>() {
			@Override
			public int length() {
				return inner.length;
			}

			@Override
			public Character get(int index) {
				return inner[index];
			}

			@Override
			public void set(int index, Character element) {
				inner[index] = element;
			}
		};
	}

	public static char[] toArray(List<Character> src) {
		int size = src.size();
		char[] dest = new char[size];

		for(int index = 0; index < size; index++) {
			dest[index] = src.get(index);
		}
		return dest;
	}
}
