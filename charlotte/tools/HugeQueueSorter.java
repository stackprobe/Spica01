package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HugeQueueSorter {
	public static void sort(HugeQueue target, Comparator<byte[]> comp) throws Exception {
		QueueUnit<HugeQueue> pool = new QueueUnit<HugeQueue>();
		try {
			{
				List<byte[]> dest = new ArrayList<byte[]>();
				int count = 0;

				for(; ;) {
					if(target.size() == 0L || 300000000 < count) {
						dest.sort(comp);

						{
							HugeQueue q = new HugeQueue();
							try {
								for(byte[] value : dest) {
									q.enqueue(value);
								}
								pool.enqueue(q);
								q = null;
							}
							finally {
								if(q != null) {
									q.close();
								}
							}
						}

						if(target.size() == 0L) {
							break;
						}
						dest.clear();
						count = 0;
					}

					{
						byte[] value = target.dequeue();

						dest.add(value);
						count += value.length + 30;
					}
				}
			}

			for(; ; ) {
				try(
						HugeQueue p1 = pool.dequeue();
						HugeQueue p2 = pool.dequeue();
						) {
					HugeQueue q;

					if(pool.size() == 0) {
						q = target;
					}
					else {
						q = new HugeQueue(); // XXX un-catched
					}
					byte[] b1 = null;
					byte[] b2 = null;

					while(0L < p1.size() && 0L < p2.size()) {
						if(b1 == null) {
							b1 = p1.dequeue();
						}
						if(b2 == null) {
							b2 = p2.dequeue();
						}
						int ret = comp.compare(b1, b2);

						if(ret < 0) {
							q.enqueue(b1);
							b1 = null;
						}
						else if(0 < ret) {
							q.enqueue(b2);
							b2 = null;
						}
						else {
							q.enqueue(b1);
							q.enqueue(b2);
							b1 = null;
							b2 = null;
						}
					}
					while(0L < p1.size()) {
						q.enqueue(p1.dequeue());
					}
					while(0L < p2.size()) {
						q.enqueue(p2.dequeue());
					}
					if(pool.size() == 0) {
						break;
					}
					pool.enqueue(q);
				}
			}
		}
		finally {
			while(1 <= pool.size()) {
				pool.dequeue().close();
			}
		}
	}
}
