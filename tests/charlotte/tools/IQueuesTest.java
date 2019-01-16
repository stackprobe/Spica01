package tests.charlotte.tools;

import charlotte.tools.IQueue;
import charlotte.tools.IQueues;
import charlotte.tools.QueueUnit;

public class IQueuesTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static int _test01_c = 0;

	private static void test01() {
		for(Integer v : IQueues.iterable(IQueues.wrap(IQueues.iterable(() -> _test01_c < 10 ? _test01_c++ : null)))) {
			System.out.println(v);
		}
	}

	private static void test02() {
		IQueue<String> q = new QueueUnit<String>();

		q.enqueue("A");
		q.enqueue("B");
		q.enqueue("C");
		q.enqueue(null); // terminator
		q.enqueue("X");
		q.enqueue("Y");
		q.enqueue("Z");

		IQueue<String> r = IQueues.wrap(() -> q.dequeue());

		while(r.hasElements()) {
			System.out.println(r.dequeue());
		}
		System.out.println("----");

		while(q.hasElements()) {
			System.out.println(q.dequeue());
		}
	}
}
