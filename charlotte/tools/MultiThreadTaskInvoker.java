package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadTaskInvoker implements AutoCloseable {
	public int THREAD_MAX = Runtime.getRuntime().availableProcessors();
	public int THROWABLE_MAX = 10;

	// <---- prop

	private Object SYNCROOT = new Object();
	private List<Thread> _ths = new ArrayList<Thread>();
	private QueueUnit<RunnableEx> _tasks = new QueueUnit<RunnableEx>();
	private List<Throwable> _exs = new ArrayList<Throwable>();

	public void addTask(RunnableEx task) {
		synchronized(SYNCROOT) {
			_tasks.enqueue(task);

			if(_ths.size() < THREAD_MAX) {
				Thread th = new Thread(() -> {
					for(; ; ) {
						RunnableEx nextTask;

						synchronized(SYNCROOT) {
							if(_tasks.hasElements() == false) {
								_ths.removeIf(t -> t == Thread.currentThread());
								return;
							}
							nextTask = _tasks.dequeue();
						}

						try {
							nextTask.run();
						}
						catch(Throwable e) {
							synchronized(SYNCROOT) {
								if(_exs.size() < THROWABLE_MAX) {
									_exs.add(e);
								}
							}
						}
					}
				});

				th.start();

				_ths.add(th);
			}
		}
	}

	public boolean isEnded() {
		synchronized(SYNCROOT) {
			return _ths.size() <= 0;
		}
	}

	public void waitToEnd() throws Exception {
		for(; ; ) {
			Thread th;

			synchronized(SYNCROOT) {
				if(_ths.size() <= 0) {
					break;
				}
				th = _ths.get(0);
			}
			th.join();
		}
	}

	public void relayThrow() throws Exception {
		waitToEnd();

		if(1 <= _exs.size()) {
			throw RTError.re(_exs);
		}
	}

	@Override
	public void close() throws Exception {
		waitToEnd();
	}
}
