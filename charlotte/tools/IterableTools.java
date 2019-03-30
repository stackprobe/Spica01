package charlotte.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class IterableTools {
	private static <T> Iterator<T> iteratorMN(Consumer<Consumer<T>> moveNext) {
		return new Iterator<T>() {
			private int _ready = 2;
			private T _next;

			@Override
			public boolean hasNext() {
				if(_ready == 2) {
					_ready = 0;

					moveNext.accept(element -> {
						_ready = 1;
						_next = element;
					});
				}
				return _ready == 1;
			}

			@Override
			public T next() {
				if(hasNext() == false) {
					throw new RTError("No more elements.");
				}
				_ready = 2;
				return _next;
			}
		};
	}

	public static <T> Iterable<T> linearize(Iterable<Iterable<T>> src) {
		return new Iterable<T>() {
			private Iterator<T> _vehicle = new ArrayList<T>(0).iterator();
			private Iterator<Iterable<T>> _train = src.iterator();

			@Override
			public Iterator<T> iterator() {
				return iteratorMN(setter -> {
					while(_vehicle.hasNext() == false) {
						if(_train.hasNext() == false) {
							return;
						}
						_vehicle = _train.next().iterator();
					}
					setter.accept(_vehicle.next());
				});
			}
		};
	}

	// old
	/*
	public static <T> Iterable<T> linearize(Iterable<Iterable<T>> src) {
		return () -> new Iterator<T>() {
			private Iterator<T> _vehicle = new ArrayList<T>(0).iterator();
			private Iterator<Iterable<T>> _train = src.iterator();

			public Iterator<T> init() {
				moveNext();
				return this;
			}

			private boolean _hasNext = true;
			private T _next;

			private void moveNext() {
				while(_vehicle.hasNext() == false) {
					if(_train.hasNext() == false) {
						_hasNext = false;
						_next = null;
						return;
					}
					_vehicle = _train.next().iterator();
				}
				_next = _vehicle.next();
			}

			@Override
			public boolean hasNext() {
				return _hasNext;
			}

			@Override
			public T next() {
				T ret = _next;
				moveNext();
				return ret;
			}
		}
		.init();
	}
	*/

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
		return iteratorMN(setter -> {
			while(src.hasNext()) {
				T next = src.next();

				if(match.test(next)) {
					setter.accept(next);
					break;
				}
			}
		});
	}

	// old
	/*
	public static <T> Iterator<T> where(Iterator<T> src, Predicate<T> match) {
		return new Iterator<T>() {
			public Iterator<T> init() {
				moveNext();
				return this;
			}

			private boolean _hasNext = true;
			private T _next;

			private void moveNext() {
				while(src.hasNext()) {
					_next = src.next();

					if(match.test(_next)) {
						return;
					}
				}
				_hasNext = false;
			}

			@Override
			public boolean hasNext() {
				return _hasNext;
			}

			@Override
			public T next() {
				T ret = _next;
				moveNext();
				return ret;
			}
		}
		.init();
	}
	*/
}
