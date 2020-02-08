package charlotte.tools;

public class QueueUnit<T> implements IQueue<T>  {
	private Chain _head;
	private Chain _tail;
	private int _count = 0;

	public QueueUnit() {
		_head = new Chain();
		_tail = _head;
	}

	public int size() {
		return _count;
	}

	@Override
	public boolean hasElements() {
		return _count != 0;
	}

	@Override
	public void enqueue(T element) {
		_tail.element = element;
		_tail.next = new Chain();
		_tail = _tail.next;
		_count++;
	}

	@Override
	public T dequeue() {
		if(_count == 0) {
			throw new RTError("\u7a7a\u306e\u30ad\u30e5\u30fc\u304b\u3089\u5024\u3092\u8aad\u307f\u8fbc\u3082\u3046\u3068\u3057\u307e\u3057\u305f\u3002");
		}
		T ret = _head.element;
		_head = _head.next;
		_count--;
		return ret;
	}

	private class Chain {
		public T element;
		public Chain next;
	}
}
