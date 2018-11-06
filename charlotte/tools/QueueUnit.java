package charlotte.tools;

public class QueueUnit<T> {
	private Chain _head;
	private Chain _tail;
	private int _count = 0;

	public QueueUnit() {
		_head = new Chain();
		_tail = _head;
	}

	public void enqueue(T value) {
		_tail.value = value;
		_tail.next = new Chain();
		_tail = _tail.next;
		_count++;
	}

	public T dequeue() {
		if(_count < 1) {
			throw new RTError("空のキューから値を読み込もうとしました。");
		}
		T ret = _head.value;
		_head = _head.next;
		_count--;
		return ret;
	}

	public int size() {
		return _count;
	}

	private class Chain {
		public T value;
		public Chain next;
	}
}
