package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListTools {
	public static <T> int comp(List<T> a, List<T> b, Comparator<T> comp) {
		int minlen = Math.min(a.size(), b.size());

		for(int index = 0; index < minlen; index++) {
			int ret = comp.compare(a.get(index), b.get(index));

			if(ret != 0) {
				return ret;
			}
		}
		return IntTools.comp.compare(a.size(), b.size());
	}

	public static <T> void swap(List<T> list, int a, int b) {
		T tmp = list.get(a);
		list.set(a, list.get(b));
		list.set(b, tmp);
	}

	public static <T> int indexOf(List<T> list, T target, Comparator<T> comp) {
		return indexOf(list, target, comp, -1);
	}

	public static <T> int indexOf(List<T> list, T target, Comparator<T> comp, int defval) {
		for(int index = 0; index < list.size(); index++) {
			if(comp.compare(list.get(index), target) == 0) {
				return index;
			}
		}
		return defval;
	}

	public static <T> int indexOf(List<T> list, Predicate<T> match) {
		return indexOf(list, match, -1);
	}

	public static <T> int indexOf(List<T> list, Predicate<T> match, int defval) {
		for(int index = 0; index < list.size(); index++) {
			if(match.test(list.get(index))) {
				return index;
			}
		}
		return defval;
	}

	public static <T> int lastIndexOf(List<T> list, T target, Comparator<T> comp) {
		return lastIndexOf(list, target, comp, -1);
	}

	public static <T> int lastIndexOf(List<T> list, T target, Comparator<T> comp, int defval) {
		for(int index = list.size() - 1; 0 <= index; index++) {
			if(comp.compare(list.get(index), target) == 0) {
				return index;
			}
		}
		return defval;
	}

	public static <T> int lastIndexOf(List<T> list, Predicate<T> match) {
		return lastIndexOf(list, match, -1);
	}

	public static <T> int lastIndexOf(List<T> list, Predicate<T> match, int defval) {
		for(int index = list.size() - 1; 0 <= index; index++) {
			if(match.test(list.get(index))) {
				return index;
			}
		}
		return defval;
	}

	public static <T> boolean contains(List<T> list, T target, Comparator<T> comp) {
		return indexOf(list, target, comp) != -1;
	}

	public static <T> boolean contains(List<T> list, Predicate<T> match) {
		return indexOf(list, match) != -1;
	}

	public static <T> List<T> toList(Iterator<T> src) {
		return toList(IteratorTools.once(src));
	}

	public static <T> List<T> toList(Iterable<T> src) {
		List<T> dest = new ArrayList<T>();

		for(T element : src) {
			dest.add(element);
		}
		return dest;
	}

	public static <T> List<T> repeat(T element, int count) {
		List<T> dest = new ArrayList<T>(count);

		for(int index = 0; index < count; index++) {
			dest.add(element);
		}
		return dest;
	}

	/**
	 * マージ // orig: * \u30de\u30fc\u30b8
	 * @param list1 入力1 // orig: * @param list1 \u5165\u529b1
	 * @param list2 入力2 // orig: * @param list2 \u5165\u529b2
	 * @param destOnly1 入力1だけに存在する要素をここへ追加する。null のとき何もしない。 // orig: * @param destOnly1 \u5165\u529b1\u3060\u3051\u306b\u5b58\u5728\u3059\u308b\u8981\u7d20\u3092\u3053\u3053\u3078\u8ffd\u52a0\u3059\u308b\u3002null \u306e\u3068\u304d\u4f55\u3082\u3057\u306a\u3044\u3002
	 * @param destBoth1 両方に存在する入力1の要素をここへ追加する。null のとき何もしない。 // orig: * @param destBoth1 \u4e21\u65b9\u306b\u5b58\u5728\u3059\u308b\u5165\u529b1\u306e\u8981\u7d20\u3092\u3053\u3053\u3078\u8ffd\u52a0\u3059\u308b\u3002null \u306e\u3068\u304d\u4f55\u3082\u3057\u306a\u3044\u3002
	 * @param destBoth2 両方に存在する入力2の要素をここへ追加する。null のとき何もしない。 // orig: * @param destBoth2 \u4e21\u65b9\u306b\u5b58\u5728\u3059\u308b\u5165\u529b2\u306e\u8981\u7d20\u3092\u3053\u3053\u3078\u8ffd\u52a0\u3059\u308b\u3002null \u306e\u3068\u304d\u4f55\u3082\u3057\u306a\u3044\u3002
	 * @param destOnly2 入力2だけに存在する要素をここへ追加する。null のとき何もしない。 // orig: * @param destOnly2 \u5165\u529b2\u3060\u3051\u306b\u5b58\u5728\u3059\u308b\u8981\u7d20\u3092\u3053\u3053\u3078\u8ffd\u52a0\u3059\u308b\u3002null \u306e\u3068\u304d\u4f55\u3082\u3057\u306a\u3044\u3002
	 * @param comp 要素の比較 // orig: * @param comp \u8981\u7d20\u306e\u6bd4\u8f03
	 */
	public static <T> void merge(List<T> list1, List<T> list2, List<T> destOnly1, List<T> destBoth1, List<T> destBoth2, List<T> destOnly2, Comparator<T> comp) {
		list1.sort(comp);
		list2.sort(comp);

		merge_noSort(list1, list2, destOnly1, destBoth1, destBoth2, destOnly2, comp);
	}

	public static <T> void merge_noSort(List<T> list1, List<T> list2, List<T> destOnly1, List<T> destBoth1, List<T> destBoth2, List<T> destOnly2, Comparator<T> comp) {
		int index1 = 0;
		int index2 = 0;

		for(; ; ) {
			int ret;

			if(list1.size() <= index1) {
				if(list2.size() <= index2) {
					break;
				}
				ret = 1;
			}
			else if(list2.size() <= index2) {
				ret = -1;
			}
			else {
				ret = comp.compare(list1.get(index1), list2.get(index2));
			}

			if(ret < 0) {
				if(destOnly1 != null) {
					destOnly1.add(list1.get(index1));
				}
				index1++;
			}
			else if(0 < ret) {
				if(destOnly2 != null) {
					destOnly2.add(list2.get(index2));
				}
				index2++;
			}
			else {
				if(destBoth1 != null) {
					destBoth1.add(list1.get(index1));
				}
				if(destBoth2 != null) {
					destBoth2.add(list2.get(index2));
				}
				index1++;
				index2++;
			}
		}
	}

	public static <T> List<PairUnit<T, T>> getMergedPairs(List<T> list1, List<T> list2, T defval, Comparator<T> comp) {
		list1.sort(comp);
		list2.sort(comp);

		return getMergedPairs_noSort(list1, list2, defval, comp);
	}

	public static <T> List<PairUnit<T, T>> getMergedPairs_noSort(List<T> list1, List<T> list2, T defval, Comparator<T> comp) {
		int index1 = 0;
		int index2 = 0;

		List<PairUnit<T, T>> dest = new ArrayList<PairUnit<T, T>>();

		for(; ; ) {
			int ret;

			if(list1.size() <= index1) {
				if(list2.size() <= index2) {
					break;
				}
				ret = 1;
			}
			else if(list2.size() <= index2) {
				ret = -1;
			}
			else {
				ret = comp.compare(list1.get(index1), list2.get(index2));
			}

			if(ret < 0) {
				dest.add(new PairUnit<T, T>(list1.get(index1++), defval));
			}
			else if(0 < ret) {
				dest.add(new PairUnit<T, T>(defval, list2.get(index2++)));
			}
			else {
				dest.add(new PairUnit<T, T>(list1.get(index1++), list2.get(index2++)));
			}
		}
		return dest;
	}

	/**
	 *
	 * @param src ソート済みであること。 // orig: * @param src \u30bd\u30fc\u30c8\u6e08\u307f\u3067\u3042\u308b\u3053\u3068\u3002
	 * @param comp
	 * @return
	 */
	public static <T> List<T> distinct(Iterable<T> src, Comparator<T> comp) {
		return distinct(src.iterator(), comp);
	}

	/**
	 *
	 * @param src ソート済みであること。 // orig: * @param src \u30bd\u30fc\u30c8\u6e08\u307f\u3067\u3042\u308b\u3053\u3068\u3002
	 * @param comp
	 * @return
	 */
	@Deprecated
	public static <T> List<T> distinct(Iterator<T> iterator, Comparator<T> comp) {
		List<T> dest = new ArrayList<T>();

		if(iterator.hasNext()) {
			T lastElement = iterator.next();

			dest.add(lastElement);

			while(iterator.hasNext()) {
				T element = iterator.next();

				if(comp.compare(element, lastElement) != 0) {
					dest.add(element);
					lastElement = element;
				}
			}
		}
		return dest;
	}

	public static <T> T lightest(Iterable<T> src, Function<T, Double> toWeight) {
		return lightest(src.iterator(), toWeight);
	}

	@Deprecated
	public static <T> T lightest(Iterator<T> iterator, Function<T, Double> toWeight) {
		if(iterator.hasNext() == false) {
			throw new RTError("Cannot find lightest when list is empty");
		}
		T ret = iterator.next();
		double ret_weight = toWeight.apply(ret);

		while(iterator.hasNext()) {
			T element = iterator.next();
			double weight = toWeight.apply(element);

			if(weight < ret_weight) {
				ret = element;
				ret_weight = weight;
			}
		}
		return ret;
	}

	public static <T> T heaviest(Iterable<T> src, Function<T, Double> toWeight) {
		return heaviest(src.iterator(), toWeight);
	}

	@Deprecated
	public static <T> T heaviest(Iterator<T> src, Function<T, Double> toWeight) {
		return lightest(src, element -> toWeight.apply(element) * -1);
	}

	public static <T> T smallest(Iterable<T> src, Comparator<T> comp) {
		return smallest(src.iterator(), comp);
	}

	@Deprecated
	public static <T> T smallest(Iterator<T> iterator, Comparator<T> comp) {
		if(iterator.hasNext() == false) {
			throw new RTError("Cannot find smallest when list is empty");
		}
		T ret = iterator.next();

		while(iterator.hasNext()) {
			T element = iterator.next();

			if(comp.compare(element, ret) < 0) {
				ret = element;
			}
		}
		return ret;
	}

	public static <T> T largest(Iterable<T> src, Comparator<T> comp) {
		return largest(src.iterator(), comp);
	}

	@Deprecated
	public static <T> T largest(Iterator<T> src, Comparator<T> comp) {
		return smallest(src, (a, b) -> comp.compare(a, b) * -1);
	}

	@Deprecated
	public static <T, R> List<R> select(Iterator<T> src, Function<T, R> conv) {
		return select(IteratorTools.once(src), conv);
	}

	public static <T, R> List<R> select(Iterable<T> src, Function<T, R> conv) {
		List<R> dest = new ArrayList<R>();

		for(T element : src) {
			dest.add(conv.apply(element));
		}
		return dest;
	}

	@Deprecated
	public static <T> List<T> where(Iterator<T> src, Predicate<T> match) {
		return where(IteratorTools.once(src), match);
	}

	public static <T> List<T> where(Iterable<T> src, Predicate<T> match) {
		List<T> dest = new ArrayList<T>();

		for(T element : src) {
			if(match.test(element)) {
				dest.add(element);
			}
		}
		return dest;
	}

	@Deprecated
	public static <T> boolean any(Iterator<T> src, Predicate<T> match) {
		return any(IteratorTools.once(src), match);
	}

	public static <T> boolean any(Iterable<T> src, Predicate<T> match) {
		for(T element : src) {
			if(match.test(element)) {
				return true;
			}
		}
		return false;
	}

	public static <T> List<T> one(T element) {
		List<T> list = new ArrayList<T>(1);
		list.add(element);
		return list;
	}

	@SafeVarargs
	public static <T> List<T> lot(T... elements) {
		List<T> list = new ArrayList<T>(elements.length);

		for(T element : elements) {
			list.add(element);
		}
		return list;
	}

	public static <T> void reverse(List<T> list) {
		int l = 0;
		int r = list.size() - 1;

		while(l < r) {
			swap(list, l, r);
			l++;
			r--;
		}
	}

	@Deprecated
	public static <T> List<T> copy(Iterator<T> src) {
		return copy(IteratorTools.once(src));
	}

	public static <T> List<T> copy(Iterable<T> src) {
		List<T> dest = new ArrayList<T>();

		for(T element : src) {
			dest.add(element);
		}
		return dest;
	}

	public static <T> List<T> copyOfRange(List<T> src, int start) {
		return copyOfRange(src, start, src.size());
	}

	public static <T> List<T> copyOfRange(List<T> src, int start, int end) {
		return copy(range(src, start, end));
	}

	public static <T> List<T> range(List<T> src, int start) {
		return range(src, start, src.size());
	}

	/**
	 * 部分リストを返す。<br> // orig: * \u90e8\u5206\u30ea\u30b9\u30c8\u3092\u8fd4\u3059\u3002<br>
	 * List の subList() と同じ。 // orig: * List \u306e subList() \u3068\u540c\u3058\u3002
	 * @param src 元のリスト // orig: * @param src \u5143\u306e\u30ea\u30b9\u30c8
	 * @param start 開始位置 // orig: * @param start \u958b\u59cb\u4f4d\u7f6e
	 * @param end 終了位置 // orig: * @param end \u7d42\u4e86\u4f4d\u7f6e
	 * @return 部分リスト // orig: * @return \u90e8\u5206\u30ea\u30b9\u30c8
	 */
	public static <T> List<T> range(List<T> src, int start, int end) {
		return IArrays.asList(IArrays.range(IArrays.wrap(src), start, end));
	}

	public static <T> void removeRange(List<T> list, int fromIndex, int toIndex) {
		if(fromIndex < 0 || toIndex < fromIndex || list.size() < toIndex) {
			throw new IllegalArgumentException(String.format("(0, end: %d) -> (%d, end: %d)", list.size(), fromIndex, toIndex));
		}
		int w = fromIndex;

		for(int r = toIndex; r < list.size(); r++) {
			list.set(w++, list.get(r));
		}
		while(w < list.size()) {
			list.remove(list.size() - 1);
		}
	}

	public static <T> void insertRange(List<T> list, int insertIndex, List<T> part) {
		if(insertIndex < 0 || list.size() < insertIndex) {
			throw new IllegalArgumentException("Bad insert index: " + insertIndex);
		}

		for(int index = 0; index < part.size(); index++) {
			list.add(null);
		}
		for(int r = list.size() - 1 - part.size(), w = list.size() - 1; insertIndex <= r; r--, w--) {
			list.set(w, list.get(r));
		}
		for(int index = 0; index < part.size(); index++) {
			list.set(insertIndex + index, part.get(index));
		}
	}
}
