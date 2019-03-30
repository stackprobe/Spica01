package tests.charlotte.tools;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.function.Supplier;

import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.IQueue;
import charlotte.tools.IQueues;
import charlotte.tools.QueueUnit;
import charlotte.tools.RTError;
import charlotte.tools.WorkingDir;
import charlotte.tools.Wrapper;

public class IQueuesTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			test03();
			//test04();

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

	private static void test02() { // smpl
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

	private static void test03() throws Exception { // smpl
		try(WorkingDir wd = new WorkingDir()) {
			String file = wd.makePath();

			try(CsvFileWriter writer = new CsvFileWriter(file)) {
				writer.writeRow(new String[] { "1", "2", "3" });
				writer.writeRow(new String[] { "4", "5", "6" });
				writer.writeRow(new String[] { "7", "8", "9" });
			}

			try(CsvFileReader reader = new CsvFileReader(file)) {
				IQueue<?> q = IQueues.wrap(() -> RTError.get(() -> reader.readRow()));

				int count = IQueues.counter(q);

				System.out.println("count: " + count);
			}
		}
	}

	private static void test04() { // smpl
		Enumeration<Object> a = new StringTokenizer("a:bb:ccc", ":");
		Supplier<Object> b = IQueues.supplier(a);
		Supplier<String> c = () -> (String)b.get();
		Iterable<String> d = IQueues.iterable(c);

		for(String s : d) {
			System.out.println(s);
		}

		// ----

		for(String s : Wrapper.create(new StringTokenizer("1:22:333", ":"))
				.change(w -> IQueues.supplier(w))
				.change(w -> (Supplier<String>)() -> (String)w.get())
				.change(w -> IQueues.iterable(w))
				.get()
				) {
			System.out.println(s);
		}

		// ----

		for(String s : Wrapper.create(new StringTokenizer("4:55:666", ":"))
				.change(w -> IQueues.supplier(w))
				.change(w -> IQueues.iterable(() -> (String)w.get()))
				.get()
				) {
			System.out.println(s);
		}

		// ----

		for(Object s : IQueues.iterable(new StringTokenizer("A:BB:CCC", ":"))) {
			System.out.println(s);
		}
	}
}
