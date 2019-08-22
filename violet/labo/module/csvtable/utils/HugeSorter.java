package violet.labo.module.csvtable.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import charlotte.tools.ConsumerEx;
import charlotte.tools.ExceptionDam;
import charlotte.tools.IQueue;
import charlotte.tools.IQueues;
import charlotte.tools.IteratorTools;
import charlotte.tools.QueueUnit;

public abstract class HugeSorter<T> {
	protected abstract void beforeFirstRead() throws Exception;
	protected abstract T read() throws Exception;
	protected abstract void afterLastRead() throws Exception;
	protected abstract void beforeFirstWrite() throws Exception;
	protected abstract void write(T value) throws Exception;
	protected abstract void afterLastWrite() throws Exception;
	protected abstract void copy(IPart<T> part) throws Exception;

	protected abstract int getWeight(T value) throws Exception;
	protected abstract int capacity() throws Exception;

	protected abstract IPart<T> createPart() throws Exception;

	protected interface IPart<U> extends AutoCloseable {
		void beforeFirstWrite() throws Exception;
		void write(U value) throws Exception;
		void afterLastWrite() throws Exception;
		void beforeFirstRead() throws Exception;
		U read() throws Exception;
		void afterLastRead() throws Exception;
	}

	public void sort(Comparator<T> comp) throws Exception {
		ExceptionDam.section(eDam -> {
			QueueUnit<IPart<T>> parts = new QueueUnit<IPart<T>>();

			eDam.invoke(() -> {
				List<T> values = new ArrayList<T>();
				int loaded = 0;

				beforeFirstRead();

				for(; ; ) {
					T value = read();

					if(value == null) {
						break;
					}
					values.add(value);
					loaded += getWeight(value);

					if(capacity() < loaded) {
						writeToPart(values, comp, parts);
						values.clear();
						loaded = 0;
					}
				}
				if(1 <= values.size()) {
					writeToPart(values, comp, parts);
				}
				afterLastRead();

				values = null;
				loaded = -1;

				if(parts.size() == 0) {
					beforeFirstWrite();
					afterLastWrite();
				}
				else if(parts.size() == 1) {
					try(IPart<T> part = parts.dequeue()) {
						copy(part);
					}
				}
				else {
					while(2 <= parts.size()) {
						try(
								IPart<T> part = parts.dequeue();
								IPart<T> part2 = parts.dequeue();
								) {
							if(parts.size() == 0) {
								beforeFirstWrite();
								mergePart(part, part2, value -> write(value), comp);
								afterLastWrite();
							}
							else {
								IPart<T> partNew = createPart();

								parts.enqueue(partNew);
								partNew.beforeFirstWrite();

								mergePart(part, part2, value -> partNew.write(value), comp);

								partNew.afterLastWrite();
							}
						}
					}
				}
			});

			for(IPart<T> part : IteratorTools.once(IQueues.iterator(parts))) {
				eDam.invoke(() -> part.close());
			}
		});
	}

	private void writeToPart(List<T> values, Comparator<T> comp, IQueue<IPart<T>> parts) throws Exception {
		values.sort(comp);

		IPart<T> part = createPart();

		parts.enqueue(part);
		part.beforeFirstWrite();

		for(T value : values) {
			part.write(value);
		}
		part.afterLastWrite();
	}

	private void mergePart(IPart<T> part, IPart<T> part2, ConsumerEx<T> writer, Comparator<T> comp) throws Exception {
		part.beforeFirstRead();
		part2.beforeFirstRead();

		T value = part.read();
		T value2 = part2.read();

		while(value != null && value2 != null) {
			int ret = comp.compare(value, value2);

			if(ret <= 0) {
				writer.accept(value);
				value = part.read();
			}
			if(0 <= ret) {
				writer.accept(value2);
				value2 = part2.read();
			}
		}
		while(value != null) {
			writer.accept(value);
			value = part.read();
		}
		while(value2 != null) {
			writer.accept(value2);
			value2 = part2.read();
		}
		part.afterLastRead();
		part2.afterLastRead();
	}
}
