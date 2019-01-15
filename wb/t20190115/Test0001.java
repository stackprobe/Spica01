package wb.t20190115;

import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		QueueUnit<String> q = new QueueUnit<String>();

		IQueue<String> iq = IQueues.combine(
				IQueues.wrap(IQueues.iterable(IQueues.supplier(IQueues.wrap(IQueues.supplier(IQueues.iterable(q)))))),
				IQueues.wrap(IQueues.consumer(q))
				);

		iq.enqueue("A");
		iq.enqueue("B");
		iq.enqueue("C");

		System.out.println(iq.dequeue());
		System.out.println(iq.dequeue());
		System.out.println(iq.dequeue());
	}
}
