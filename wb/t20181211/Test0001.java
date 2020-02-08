package wb.t20181211;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IArray;
import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;
import charlotte.tools.Wrapper;

public class Test0001 {
	public static void main(String[] args) {
		try {
			//test06();
			test07();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test06() {
		//System.out.println(" " + StringTools.HALF);

		String half = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\uff69\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f\uff70\uff71\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79\uff7a\uff7b\uff7c\uff7d\uff7e\uff7f\uff80\uff81\uff82\uff83\uff84\uff85\uff86\uff87\uff88\uff89\uff8a\uff8b\uff8c\uff8d\uff8e\uff8f\uff90\uff91\uff92\uff93\uff94\uff95\uff96\uff97\uff98\uff99\uff9a\uff9b\uff9c\uff9d\uff9e\uff9f";
		String hira = "\u3000\uff10\uff11\uff12\uff13\uff14\uff15\uff16\uff17\uff18\uff19\uff21\uff22\uff23\uff24\uff25\uff26\uff27\uff28\uff29\uff2a\uff2b\uff2c\uff2d\uff2e\uff2f\uff30\uff31\uff32\uff33\uff34\uff35\uff36\uff37\uff38\uff39\uff3a\uff41\uff42\uff43\uff44\uff45\uff46\uff47\uff48\uff49\uff4a\uff4b\uff4c\uff4d\uff4e\uff4f\uff50\uff51\uff52\uff53\uff54\uff55\uff56\uff57\uff58\uff59\uff5a\uff01\u201d\uff03\uff04\uff05\uff06\u2019\uff08\uff09\uff0a\uff0b\uff0c\uff0d\uff0e\uff0f\uff1a\uff1b\uff1c\uff1d\uff1e\uff1f\uff20\uff3b\uffe5\uff3d\uff3e\uff3f\uff40\uff5b\uff5c\uff5d\uffe3\u3002\u300c\u300d\u3001\u30fb\u3092\u3041\u3043\u3045\u3047\u3049\u3083\u3085\u3087\u3063\u30fc\u3042\u3044\u3046\u3048\u304a\u304b\u304d\u304f\u3051\u3053\u3055\u3057\u3059\u305b\u305d\u305f\u3061\u3064\u3066\u3068\u306a\u306b\u306c\u306d\u306e\u306f\u3072\u3075\u3078\u307b\u307e\u307f\u3080\u3081\u3082\u3084\u3086\u3088\u3089\u308a\u308b\u308c\u308d\u308f\u3093\u309b\u309c";
		String kata = "\u3000\uff10\uff11\uff12\uff13\uff14\uff15\uff16\uff17\uff18\uff19\uff21\uff22\uff23\uff24\uff25\uff26\uff27\uff28\uff29\uff2a\uff2b\uff2c\uff2d\uff2e\uff2f\uff30\uff31\uff32\uff33\uff34\uff35\uff36\uff37\uff38\uff39\uff3a\uff41\uff42\uff43\uff44\uff45\uff46\uff47\uff48\uff49\uff4a\uff4b\uff4c\uff4d\uff4e\uff4f\uff50\uff51\uff52\uff53\uff54\uff55\uff56\uff57\uff58\uff59\uff5a\uff01\u201d\uff03\uff04\uff05\uff06\u2019\uff08\uff09\uff0a\uff0b\uff0c\uff0d\uff0e\uff0f\uff1a\uff1b\uff1c\uff1d\uff1e\uff1f\uff20\uff3b\uffe5\uff3d\uff3e\uff3f\uff40\uff5b\uff5c\uff5d\uffe3\u3002\u300c\u300d\u3001\u30fb\u30f2\u30a1\u30a3\u30a5\u30a7\u30a9\u30e3\u30e5\u30e7\u30c3\u30fc\u30a2\u30a4\u30a6\u30a8\u30aa\u30ab\u30ad\u30af\u30b1\u30b3\u30b5\u30b7\u30b9\u30bb\u30bd\u30bf\u30c1\u30c4\u30c6\u30c8\u30ca\u30cb\u30cc\u30cd\u30ce\u30cf\u30d2\u30d5\u30d8\u30db\u30de\u30df\u30e0\u30e1\u30e2\u30e4\u30e6\u30e8\u30e9\u30ea\u30eb\u30ec\u30ed\u30ef\u30f3\u309b\u309c";

		if(half.length() != hira.length()) throw null;
		if(half.length() != kata.length()) throw null;

		// ---- 1

		for(int index = 0; index < half.length(); index++) {
			System.out.println(String.format("%04x -> %04x, %04x",
					half.charAt(index) & 0xffff,
					hira.charAt(index) & 0xffff,
					kata.charAt(index) & 0xffff
					));
		}

		// ---- sort ----

		{
			String[] h2k = new String[] { half, hira, kata };

			new IArray<int[]>() {
				@Override
				public int length() {
					return h2k[0].length();
				}

				@Override
				public int[] get(int index) {
					return new int[] {
							h2k[0].charAt(index) & 0xffff,
							h2k[1].charAt(index) & 0xffff,
							h2k[2].charAt(index) & 0xffff
							};
				}

				@Override
				public void set(int index, int[] element) {
					h2k[0] = StringTools.setCharAt(h2k[0], index, (char)element[0]);
					h2k[1] = StringTools.setCharAt(h2k[1], index, (char)element[1]);
					h2k[2] = StringTools.setCharAt(h2k[2], index, (char)element[2]);
				}
			}
			.sort((a, b) -> IntTools.comp.compare(a[0], b[0]));

			half = h2k[0];
			hira = h2k[1];
			kata = h2k[2];

			System.out.println(half);
			System.out.println(hira);
			System.out.println(kata);
		}

		// ---- 2

		for(int index = 0; index < half.length(); index++) {
			System.out.println(String.format("%04x -> %04x, %04x",
					half.charAt(index) & 0xffff,
					hira.charAt(index) & 0xffff,
					kata.charAt(index) & 0xffff
					));
		}

		// ----

		int[] ds = new int[half.length()];
		int[] d2 = new int[half.length()];
		int[] d3 = new int[half.length()];

		for(int index = 0; index < half.length(); index++) {
			int d = half.charAt(index) & 0xffff;
			//System.out.println(d);
			ds[index] = d;
		}

		for(int index = 0; index < half.length(); index++) {
			int d = index == 0 ? ds[index] : (0x10000 + ds[index] - ds[index - 1]) & 0xffff;
			//System.out.println(d);
			d2[index] = d;
		}

		for(int index = 0; index < half.length(); index++) {
			int d = index == 0 ? d2[index] : (0x10000 + d2[index] - d2[index - 1]) & 0xffff;
			//System.out.println(d);
			d3[index] = d;
		}

		System.out.println(String.join(", ", Wrapper.create(
				//test06_b
				test06_c
				(
						//ds
						d2
						//d3
						))
				.change(w -> IntTools.asList(w))
				.change(w -> ListTools.select(w, value -> "" + value))
				.get()
				));;

		// ----

		System.out.println("*");

		for(int index = 0; index < half.length(); index++) {
			//int d = hira.charAt(index) & 0xffff;
			int d = (0x10000 + (hira.charAt(index) & 0xffff) - (half.charAt(index) & 0xffff)) & 0xffff;
			//System.out.println(d);
			ds[index] = d;
		}

		for(int index = 0; index < half.length(); index++) {
			int d = index == 0 ? ds[index] : ds[index] - ds[index - 1]; //int d = index == 0 ? ds[index] : (0x10000 + ds[index] - ds[index - 1]) & 0xffff;
			//System.out.println(d);
			d2[index] = d;
		}

		for(int index = 0; index < half.length(); index++) {
			int d = index == 0 ? d2[index] : d2[index] - d2[index - 1]; //int d = index == 0 ? d2[index] : (0x10000 + d2[index] - d2[index - 1]) & 0xffff;
			//System.out.println(d);
			d3[index] = d;
		}

		System.out.println(String.join(", ", Wrapper.create(
				//test06_b
				test06_c
				(
						//ds
						d2
						//d3
						))
				.change(w -> IntTools.asList(w))
				.change(w -> ListTools.select(w, value -> "" + value))
				.get()
				));;

		// ----

		System.out.println("*");

		for(int index = 0; index < half.length(); index++) {
			//int d = kata.charAt(index) & 0xffff;
			int d = (0x10000 + (kata.charAt(index) & 0xffff) - (half.charAt(index) & 0xffff)) & 0xffff;
			//int d = (kata.charAt(index) & 0xffff) ^ (hira.charAt(index) & 0xffff);
			//System.out.println(d);
			ds[index] = d;
		}

		for(int index = 0; index < half.length(); index++) {
			int d = index == 0 ? ds[index] : ds[index] - ds[index - 1]; //int d = index == 0 ? ds[index] : (0x10000 + ds[index] - ds[index - 1]) & 0xffff;
			//System.out.println(d);
			d2[index] = d;
		}

		for(int index = 0; index < half.length(); index++) {
			int d = index == 0 ? d2[index] : d2[index] - d2[index - 1]; //int d = index == 0 ? d2[index] : (0x10000 + d2[index] - d2[index - 1]) & 0xffff;
			//System.out.println(d);
			d3[index] = d;
		}

		System.out.println(String.join(", ", Wrapper.create(
				//test06_b
				test06_c
				(
						//ds
						d2
						//d3
						))
				.change(w -> IntTools.asList(w))
				.change(w -> ListTools.select(w, value -> "" + value))
				.get()
				));;
	}

	private static int[] test06_b(int[] src) {
		List<Integer> dest = new ArrayList<Integer>();

		for(int i = 0; i < src.length; ) {
			int j;

			for(j = i + 1; j < src.length && src[i] == src[j]; j++) {
				// noop
			}

			dest.add(j - i);
			dest.add(src[i]);

			i = j;
		}
		return IntTools.toArray(dest);
	}

	private static int[] test06_c(int[] src) {
		List<Integer> dest = new ArrayList<Integer>();

		for(int bChr = 2; 0 <= bChr; bChr--) {
			for(int i = src.length; 0 < i; ) {
				int j;

				for(j = i - 1; 0 <= j && src[j] == bChr; j--) {
					// noop
				}
				j++;

				if(3 <= i - j) {
					dest.add(j);
					dest.add(i - j);

					final int f_i = i;
					final int f_j = j;

					src = Wrapper.create(src)
							.change(w -> IntTools.asList(w))
							.change(w -> ListTools.copy(w))
							.accept(w -> w.subList(f_j, f_i).clear())
							.change(w -> IntTools.toArray(w))
							.get();
				}
				i = j - 1;
			}
			dest.add(-1);

			/*
			final int f_bChr = bChr;

			src = Wrapper.create(src)
					.change(w -> IntTools.asList(w))
					.change(w -> ListTools.where(w, i -> i != f_bChr))
					.change(w -> IntTools.toArray(w))
					.get();
					*/
		}
		for(int i : src) {
			dest.add(i);
		}
		return IntTools.toArray(dest);
	}

	private static void test07() {
		String half = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\uff69\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f\uff70\uff71\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79\uff7a\uff7b\uff7c\uff7d\uff7e\uff7f\uff80\uff81\uff82\uff83\uff84\uff85\uff86\uff87\uff88\uff89\uff8a\uff8b\uff8c\uff8d\uff8e\uff8f\uff90\uff91\uff92\uff93\uff94\uff95\uff96\uff97\uff98\uff99\uff9a\uff9b\uff9c\uff9d\uff9e\uff9f";
		String hira = "\u3000\uff10\uff11\uff12\uff13\uff14\uff15\uff16\uff17\uff18\uff19\uff21\uff22\uff23\uff24\uff25\uff26\uff27\uff28\uff29\uff2a\uff2b\uff2c\uff2d\uff2e\uff2f\uff30\uff31\uff32\uff33\uff34\uff35\uff36\uff37\uff38\uff39\uff3a\uff41\uff42\uff43\uff44\uff45\uff46\uff47\uff48\uff49\uff4a\uff4b\uff4c\uff4d\uff4e\uff4f\uff50\uff51\uff52\uff53\uff54\uff55\uff56\uff57\uff58\uff59\uff5a\uff01\u201d\uff03\uff04\uff05\uff06\u2019\uff08\uff09\uff0a\uff0b\uff0c\uff0d\uff0e\uff0f\uff1a\uff1b\uff1c\uff1d\uff1e\uff1f\uff20\uff3b\uffe5\uff3d\uff3e\uff3f\uff40\uff5b\uff5c\uff5d\uffe3\u3002\u300c\u300d\u3001\u30fb\u3092\u3041\u3043\u3045\u3047\u3049\u3083\u3085\u3087\u3063\u30fc\u3042\u3044\u3046\u3048\u304a\u304b\u304d\u304f\u3051\u3053\u3055\u3057\u3059\u305b\u305d\u305f\u3061\u3064\u3066\u3068\u306a\u306b\u306c\u306d\u306e\u306f\u3072\u3075\u3078\u307b\u307e\u307f\u3080\u3081\u3082\u3084\u3086\u3088\u3089\u308a\u308b\u308c\u308d\u308f\u3093\u309b\u309c";
		String kata = "\u3000\uff10\uff11\uff12\uff13\uff14\uff15\uff16\uff17\uff18\uff19\uff21\uff22\uff23\uff24\uff25\uff26\uff27\uff28\uff29\uff2a\uff2b\uff2c\uff2d\uff2e\uff2f\uff30\uff31\uff32\uff33\uff34\uff35\uff36\uff37\uff38\uff39\uff3a\uff41\uff42\uff43\uff44\uff45\uff46\uff47\uff48\uff49\uff4a\uff4b\uff4c\uff4d\uff4e\uff4f\uff50\uff51\uff52\uff53\uff54\uff55\uff56\uff57\uff58\uff59\uff5a\uff01\u201d\uff03\uff04\uff05\uff06\u2019\uff08\uff09\uff0a\uff0b\uff0c\uff0d\uff0e\uff0f\uff1a\uff1b\uff1c\uff1d\uff1e\uff1f\uff20\uff3b\uffe5\uff3d\uff3e\uff3f\uff40\uff5b\uff5c\uff5d\uffe3\u3002\u300c\u300d\u3001\u30fb\u30f2\u30a1\u30a3\u30a5\u30a7\u30a9\u30e3\u30e5\u30e7\u30c3\u30fc\u30a2\u30a4\u30a6\u30a8\u30aa\u30ab\u30ad\u30af\u30b1\u30b3\u30b5\u30b7\u30b9\u30bb\u30bd\u30bf\u30c1\u30c4\u30c6\u30c8\u30ca\u30cb\u30cc\u30cd\u30ce\u30cf\u30d2\u30d5\u30d8\u30db\u30de\u30df\u30e0\u30e1\u30e2\u30e4\u30e6\u30e8\u30e9\u30ea\u30eb\u30ec\u30ed\u30ef\u30f3\u309b\u309c";

		if(half.length() != hira.length()) throw null;
		if(half.length() != kata.length()) throw null;

		/*
		{
			String[] h2k = new String[] { half, hira, kata };

			new IArray<int[]>() {
				@Override
				public int length() {
					return h2k[0].length();
				}

				@Override
				public int[] get(int index) {
					return new int[] {
							h2k[0].charAt(index) & 0xffff,
							h2k[1].charAt(index) & 0xffff,
							h2k[2].charAt(index) & 0xffff
							};
				}

				@Override
				public void set(int index, int[] element) {
					h2k[0] = StringTools.set(h2k[0], index, (char)element[0]);
					h2k[1] = StringTools.set(h2k[1], index, (char)element[1]);
					h2k[2] = StringTools.set(h2k[2], index, (char)element[2]);
				}
			}
			.sort((a, b) -> IntTools.comp.compare(a[0], b[0]));

			half = h2k[0];
			hira = h2k[1];
			kata = h2k[2];

			System.out.println(half);
			System.out.println(hira);
			System.out.println(kata);
		}
		*/

		/*
		for(int index = 0; index < half.length(); index++) {
			System.out.println(String.format("%04x %04x %04x %04x",
					(half.charAt(index) & 0xffff),
					(half.charAt(index) & 0xffff) ^ (hira.charAt(index) & 0xffff),
					(half.charAt(index) & 0xffff) ^ (kata.charAt(index) & 0xffff),
					(hira.charAt(index) & 0xffff) ^ (kata.charAt(index) & 0xffff)
					));
		}
		*/

		for(int index = 0; index < half.length(); index++) {
			System.out.println(String.format("%04x %04x %04x",
					(half.charAt(index) & 0xffff),
					(hira.charAt(index) & 0xffff),
					(kata.charAt(index) & 0xffff)
					));
		}
	}
}
