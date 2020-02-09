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
			throw new RTError("Cannot dequeue() when queue is empty");
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
