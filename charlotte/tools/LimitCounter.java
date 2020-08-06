package charlotte.tools;

public class LimitCounter {
	private int _count;

	public LimitCounter(int count) {
		_count = count;
	}

	public boolean issue() {
		if (1 <= _count) {
			_count--;
			return true;
		}
		return false;
	}

	public static LimitCounter one() {
		return new LimitCounter(1);
	}
}
