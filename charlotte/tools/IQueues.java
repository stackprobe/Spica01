package charlotte.tools;

import java.util.function.BinaryOperator;

public class IQueues {
	public static <T> T merge(IQueue<T> queue, BinaryOperator<T> conv) {
		for(; ; ) {
			T element = queue.dequeue();

			if(queue.hasElements() == false) {
				return element;
			}
			queue.enqueue(conv.apply(element, queue.dequeue()));
		}
	}
}
