package charlotte.tools;

public class VisitorCounter {
	private int _count;

	public VisitorCounter() {
		this(0);
	}

	public VisitorCounter(int count) {
		_count = count;
	}

	public boolean hasVisitor() {
		return hasVisitor(1);
	}

	public boolean hasVisitor(int count) {
		return count <= _count;
	}

	public void enter() {
		_count++;
	}

	public void leave() {
		_count--;
	}

	public AutoCloseable section() {
		enter();
		return AnonyAutoCloseable.create(() -> leave());
	}
}
