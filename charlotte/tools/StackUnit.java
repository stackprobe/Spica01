package charlotte.tools;

public class StackUnit<T> implements IStack<T> {
	private Chain _tail = null;
	private int _count = 0;

	public int size() {
		return _count;
	}

	@Override
	public boolean hasElements() {
		return _count != 0;
	}

	@Override
	public void push(T element) {
		Chain tail = new Chain();
		tail.element = element;
		tail.next = _tail;
		_tail = tail;
		_count++;
	}

	@Override
	public T pop() {
		if(_count == 0) {
			throw new RTError("空のスタックから値を読み込もうとしました。");
		}
		T ret = _tail.element;
		_tail = _tail.next;
		_count--;
		return ret;
	}

	private class Chain {
		public T element;
		public Chain next;
	}
}
