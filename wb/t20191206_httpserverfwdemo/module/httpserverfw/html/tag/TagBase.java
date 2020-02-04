package wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;

public abstract class TagBase implements ITag {
	private Map<String, String> _attributes;
	private ITag _parent;
	private List<ITag> _children = new ArrayList<ITag>();

	@Override
	public void setAttributes(Map<String, String> attributes) {
		_attributes = attributes;
	}

	public Map<String, String> getAttributes() {
		return _attributes;
	}

	@Override
	public void setParent(ITag parent) {
		_parent = parent;

		if(parent != null) {
			((TagBase)parent)._children.add(this);
		}
	}

	public ITag getParent() {
		return _parent;
	}

	public ITag getParent(Predicate<ITag> match) {
		ITag curr = this;

		do {
			curr = ((TagBase)curr).getParent();
		}
		while(match.test(curr) == false);

		return curr;
	}

	public List<ITag> getChildren() {
		return _children;
	}

	public void dive(Consumer<ITag> routine) {
		dive(tag -> {
			routine.accept(tag);
			return true;
		});
	}

	public void dive(Predicate<ITag> routine) {
		dive(routine, tag -> true);
	}

	public void dive(Predicate<ITag> routine, Predicate<ITag> correctDiveTo) {
		IQueue<List<ITag>> childrenEntries = new QueueUnit<List<ITag>>();

		childrenEntries.enqueue(_children);

		while(childrenEntries.hasElements()) {
			for(ITag tag : childrenEntries.dequeue()) {
				if(routine.test(tag) == false) {
					return;
				}
				if(correctDiveTo.test(tag)) {
					childrenEntries.enqueue(((TagBase)tag)._children);
				}
			}
		}
	}
}
