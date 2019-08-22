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

		String half = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~｡｢｣､･ｦｧｨｩｪｫｬｭｮｯｰｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝﾞﾟ";
		String hira = "　０１２３４５６７８９ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ！”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝￣。「」、・をぁぃぅぇぉゃゅょっーあいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわん゛゜";
		String kata = "　０１２３４５６７８９ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ！”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝￣。「」、・ヲァィゥェォャュョッーアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワン゛゜";

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
				.change(w -> w.iterator())
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
				.change(w -> w.iterator())
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
				.change(w -> w.iterator())
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
							.change(w -> w.iterator())
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
		String half = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~｡｢｣､･ｦｧｨｩｪｫｬｭｮｯｰｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝﾞﾟ";
		String hira = "　０１２３４５６７８９ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ！”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝￣。「」、・をぁぃぅぇぉゃゅょっーあいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわん゛゜";
		String kata = "　０１２３４５６７８９ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ！”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝￣。「」、・ヲァィゥェォャュョッーアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワン゛゜";

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
