package violet.labo.module.httpserverfw.html.tag;

import java.util.Map;
import java.util.function.Predicate;

public abstract class TagBase implements ITag {
	private Map<String, String> _attributes;
	private ITag _parent;

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
}
