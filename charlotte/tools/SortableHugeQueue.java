package charlotte.tools;

import java.util.Comparator;

public class SortableHugeQueue {
	private IQueue<byte[]> _queue;
	private Comparator<byte[]> _comp;

	public SortableHugeQueue(IQueue<byte[]> queue, Comparator<byte[]> comp) {
		_queue = queue;
		_comp = comp;
	}

	public void sort() {
		throw null; // TODO
	}
}
