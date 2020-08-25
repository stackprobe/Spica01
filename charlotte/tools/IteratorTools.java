package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * IQueue など、一度しか列挙できないものは Iterator // orig: * IQueue \u306a\u3069\u3001\u4e00\u5ea6\u3057\u304b\u5217\u6319\u3067\u304d\u306a\u3044\u3082\u306e\u306f Iterator
 * List など、何度も列挙できるものは Iterable // orig: * List \u306a\u3069\u3001\u4f55\u5ea6\u3082\u5217\u6319\u3067\u304d\u308b\u3082\u306e\u306f Iterable
 *
 * Iterator を Iterable にしたい時は IteratorTools.once() // orig: * Iterator \u3092 Iterable \u306b\u3057\u305f\u3044\u6642\u306f IteratorTools.once()
 *
 */
public class IteratorTools {
	public static <T> Iterable<T> once(Iterator<T> src) {
		final Iterator<T> f_src = src;

		return new Iterable<T>() {
			private Iterator<T> _src = f_src;

			@Override
			public Iterator<T> iterator() {
				if(_src == null) {
					throw new RTError("Can not iterator() twice.");
				}
				Iterator<T> ret = _src;

				_src = null;

				return ret;
			}
		};
	}

	/**
	 * 列挙の引数配列(2次元列挙)を列挙(1次元列挙)に変換する。<br> // orig: * \u5217\u6319\u306e\u5f15\u6570\u914d\u5217(2\u6b21\u5143\u5217\u6319)\u3092\u5217\u6319(1\u6b21\u5143\u5217\u6319)\u306b\u5909\u63db\u3059\u308b\u3002<br>
	 * 例：{{ A, B, C }, { D, E, D }, { G, H, I }} -> { A, B, C, D, E, F, G, H, I } // orig: * \u4f8b\uff1a{{ A, B, C }, { D, E, D }, { G, H, I }} -> { A, B, C, D, E, F, G, H, I }
	 * @param p 列挙の引数配列(2次元列挙) // orig: * @param p \u5217\u6319\u306e\u5f15\u6570\u914d\u5217(2\u6b21\u5143\u5217\u6319)
	 * @return 列挙(1次元列挙) // orig: * @return \u5217\u6319(1\u6b21\u5143\u5217\u6319)
	 */
	@SafeVarargs
	public static <T> Iterable<T> join(Iterable<T>... p) {
		return linearize(IArrays.asList(p));
	}

	/**
	 * IteratorTools.once() を使って Iterable 版の join() を呼び出して下さい。 // orig: * IteratorTools.once() \u3092\u4f7f\u3063\u3066 Iterable \u7248\u306e join() \u3092\u547c\u3073\u51fa\u3057\u3066\u4e0b\u3055\u3044\u3002
	 * @param p
	 * @return
	 */
	@Deprecated
	@SafeVarargs
	public static <T> Iterator<T> join(Iterator<T>... p) {
		return linearize(IArrays.asList(p).iterator());
	}

	/**
	 * 列挙の列挙(2次元列挙)を列挙(1次元列挙)に変換する。<br> // orig: * \u5217\u6319\u306e\u5217\u6319(2\u6b21\u5143\u5217\u6319)\u3092\u5217\u6319(1\u6b21\u5143\u5217\u6319)\u306b\u5909\u63db\u3059\u308b\u3002<br>
	 * 例：{{ A, B, C }, { D, E, D }, { G, H, I }} -> { A, B, C, D, E, F, G, H, I } // orig: * \u4f8b\uff1a{{ A, B, C }, { D, E, D }, { G, H, I }} -> { A, B, C, D, E, F, G, H, I }
	 * @param src 列挙の列挙(2次元列挙) // orig: * @param src \u5217\u6319\u306e\u5217\u6319(2\u6b21\u5143\u5217\u6319)
	 * @return 列挙(1次元列挙) // orig: * @return \u5217\u6319(1\u6b21\u5143\u5217\u6319)
	 */
	public static <T> Iterable<T> linearize(Iterable<Iterable<T>> src) {
		return () -> linearize(select(src.iterator(), v -> v.iterator()));
	}

	/**
	 * IteratorTools.once() を使って Iterable 版の linearize() を呼び出して下さい。 // orig: * IteratorTools.once() \u3092\u4f7f\u3063\u3066 Iterable \u7248\u306e linearize() \u3092\u547c\u3073\u51fa\u3057\u3066\u4e0b\u3055\u3044\u3002
	 * @param src
	 * @return
	 */
	@Deprecated
	public static <T> Iterator<T> linearize(Iterator<Iterator<T>> src) {
		return IEnumerators.iterator(new IEnumerator<T>() {
			private Iterator<T> _vehicle = new ArrayList<T>(0).iterator();
			private Iterator<Iterator<T>> _train = src;
			private T _current;

			@Override
			public boolean moveNext() {
				while(_vehicle.hasNext() == false) {
					if(_train.hasNext() == false) {
						_current = null;
						return false;
					}
					_vehicle = _train.next();
				}
				_current = _vehicle.next();
				return true;
			}

			@Override
			public T current() {
				return _current;
			}
		});
	}

	public static <T, R> Iterable<R> select(Iterable<T> src, Function<T, R> conv) {
		return () -> select(src.iterator(), conv);
	}

	@Deprecated
	public static <T, R> Iterator<R> select(Iterator<T> src, Function<T, R> conv) {
		return new Iterator<R>() {
			@Override
			public boolean hasNext() {
				return src.hasNext();
			}

			@Override
			public R next() {
				return conv.apply(src.next());
			}
		};
	}

	public static <T> Iterable<T> where(Iterable<T> src, Predicate<T> match) {
		return () -> where(src.iterator(), match);
	}

	@Deprecated
	public static <T> Iterator<T> where(Iterator<T> src, Predicate<T> match) {
		return IEnumerators.iterator(new IEnumerator<T>() {
			private T _current;

			@Override
			public boolean moveNext() {
				while(src.hasNext()) {
					_current = src.next();

					if(match.test(_current)) {
						return true;
					}
					_current = null;
				}
				return false;
			}

			@Override
			public T current() {
				return _current;
			}
		});
	}

	public static <T> Iterable<T> sort(Iterable<T> src, Comparator<T> comp) {
		List<T> list = ListTools.toList(src);
		list.sort(comp);
		return list;
	}

	/**
	 *
	 * @param element
	 * @param count
	 * @return 実体の無いリスト // orig: * @return \u5b9f\u4f53\u306e\u7121\u3044\u30ea\u30b9\u30c8
	 */
	public static <T> List<T> repeat(T element, int count) {
		return IArrays.asList(IArrays.repeat(element, count));
	}

	/**
	 *
	 * @param src
	 * @return 実体の無いリスト // orig: * @return \u5b9f\u4f53\u306e\u7121\u3044\u30ea\u30b9\u30c8
	 */
	public static <T> List<T> reverse(T[] src) {
		return reverse(IArrays.wrap(src));
	}

	/**
	 *
	 * @param src
	 * @return 実体の無いリスト // orig: * @return \u5b9f\u4f53\u306e\u7121\u3044\u30ea\u30b9\u30c8
	 */
	public static <T> List<T> reverse(List<T> src) {
		return reverse(IArrays.wrap(src));
	}

	/**
	 *
	 * @param src
	 * @return 実体の無いリスト // orig: * @return \u5b9f\u4f53\u306e\u7121\u3044\u30ea\u30b9\u30c8
	 */
	public static <T> List<T> reverse(IArray<T> src) {
		return IArrays.asList(IArrays.reverse(src));
	}
}
