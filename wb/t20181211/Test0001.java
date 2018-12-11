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
			test06();

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
		//int[] d3 = new int[half.length()];

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
			//d3[index] = d;
		}

		System.out.println(String.join(", ", Wrapper.create(test06_b(d2))
				.change(w -> IntTools.asList(w))
				.change(w -> ListTools.select(w, value -> "" + value))
				.get()
				));;

		// ----

		System.out.println("*");

		for(int index = 0; index < half.length(); index++) {
			//int d = hira.charAt(index) & 0xffff;
			int d = kata.charAt(index) & 0xffff;
			//int d = (0x10000 + (hira.charAt(index) & 0xffff) - (half.charAt(index) & 0xffff)) & 0xffff;
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
			//d3[index] = d;
		}

		System.out.println(String.join(", ", Wrapper.create(test06_b(d2))
				.change(w -> IntTools.asList(w))
				.change(w -> ListTools.select(w, value -> "" + value))
				.get()
				));;

		// ----

		System.out.println("*");

		for(int index = 0; index < half.length(); index++) {
			int d = (hira.charAt(index) & 0xffff) ^ (kata.charAt(index) & 0xffff);
			//int d = hira.charAt(index) & 0xffff;
			//int d = (0x10000 + (hira.charAt(index) & 0xffff) - (half.charAt(index) & 0xffff)) & 0xffff;
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
			//d3[index] = d;
		}

		System.out.println(String.join(", ", Wrapper.create(test06_b(
				ds
				//d2
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
}
