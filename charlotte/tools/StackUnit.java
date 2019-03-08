package charlotte.tools;

import java.util.LinkedList;

public class StackUnit<T> implements IStack<T> {
	private LinkedList<T> _tower = new LinkedList<T>();

	@Override
	public boolean hasElements() {
		return 1 <= _tower.size();
	}

	@Override
	public void push(T element) {
		_tower.add(element);
	}

	@Override
	public T pop() {
		if(_tower.isEmpty()) {
			throw new RTError("空のスタックから値を読み込もうとしました。");
		}
		return _tower.removeLast();
	}
}
