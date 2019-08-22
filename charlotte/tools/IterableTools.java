package charlotte.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class IterableTools {
	public static <T> Iterable<T> linearize(Iterable<Iterable<T>> src) {
		return () -> linearize(select(src.iterator(), v -> v.iterator()));
	}

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

	public static <T> Iterable<T> once(Iterator<T> src) {
		final Iterator<T> f_src = src;

		return new Iterable<T>() {
			private Iterator<T> _src = f_src;

			@Override
			public Iterator<T> iterator() {
				if(_src == null) {
					throw new RTError("2回目の列挙は出来ません。");
				}
				Iterator<T> ret = _src;

				_src = null;

				return ret;
			}
		};
	}

	/**
	 *
	 * @param element
	 * @param count
	 * @return 実体の無いリスト
	 */
	public static <T> List<T> repeat(T element, int count) {
		return IArrays.asList(IArrays.repeat(element, count));
	}

	/**
	 *
	 * @param src
	 * @return 実体の無いリスト
	 */
	public static <T> List<T> reverse(T[] src) {
		return reverse(IArrays.wrap(src));
	}

	/**
	 *
	 * @param src
	 * @return 実体の無いリスト
	 */
	public static <T> List<T> reverse(List<T> src) {
		return reverse(IArrays.wrap(src));
	}

	/**
	 *
	 * @param src
	 * @return 実体の無いリスト
	 */
	public static <T> List<T> reverse(IArray<T> src) {
		return IArrays.asList(IArrays.reverse(src));
	}
}
