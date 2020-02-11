package wb.t20191206_httpserverfwdemo.module.httpserverfw.html;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import charlotte.tools.IQueue;
import charlotte.tools.IStack;
import charlotte.tools.QueueUnit;
import charlotte.tools.RTError;
import charlotte.tools.StackUnit;

public class HTMLTree {
	private List<Object> _sequence;
	private int _rPos = 0;

	public HTMLTree(List<Object> sequence) {
		_sequence = sequence;
	}

	public Object next() {
		return _sequence.get(_rPos++);
	}

	private INode _root;

	public void parse() {
		_root = getRoot();

		if(_rPos != _sequence.size()) {
			throw new RTError("Maybe HTML structure broken");
		}
		check();
		normalize();
	}

	private INode getRoot() {
		IStack<INode> parents = new StackUnit<INode>();
		ComplexedNode nodes = new ComplexedNode();

		for(; ; ) {
			nodes.children.add(new SimpleHTMLNode((String)next()));

			if(_sequence.size() <= _rPos) {
				break;
			}
			HTMLParser.Tag tag = (HTMLParser.Tag)next();

			if(tag.closing) {
				if(parents.hasElements() == false) {
					throw new RTError("Has some over-closing tags: " + tag.name);
				}
				TagNode tagNode = (TagNode)parents.pop();

				tagNode.closingTag = tag;
				nodes = (ComplexedNode)parents.pop();
				nodes.children.add(tagNode);
			}
			else if(tag.selfClosing) {
				nodes.children.add(new SelfClosingTagNode(tag));
			}
			else {
				TagNode tagNode = new TagNode();

				tagNode.openTag = tag;
				parents.push(nodes);
				nodes = new ComplexedNode();
				tagNode.inner = nodes;
				parents.push(tagNode);
			}
		}
		if(parents.hasElements()) {
			throw new RTError("Has some un-closed tags: " + ((TagNode)parents.pop()).openTag.name);
		}
		return nodes;
	}

	private void check() {
		IQueue<INode> targetNodes = new QueueUnit<INode>();

		targetNodes.enqueue(_root);

		while(targetNodes.hasElements()) {
			INode node = targetNodes.dequeue();

			if(node instanceof SelfClosingTagNode) {
				// noop
			}
			else if(node instanceof TagNode) {
				TagNode tagNode = (TagNode)node;

				targetNodes.enqueue(tagNode.inner);

				if(tagNode.openTag.name.equals(tagNode.closingTag.name) == false) {
					throw new RTError("Has some mismatched tags: " + tagNode.openTag.name + " --> " + tagNode.closingTag.name);
				}
			}
			else if(node instanceof ComplexedNode) {
				ComplexedNode nodes = (ComplexedNode)node;

				for(INode subNode : nodes.children) {
					targetNodes.enqueue(subNode);
				}
			}
			else if(node instanceof SimpleHTMLNode) {
				// noop
			}
			else {
				throw null; // never
			}
		}
	}

	private void normalize() {
		List<ChangeableValue<INode>> targetNodes = new ArrayList<ChangeableValue<INode>>();

		targetNodes.add(new ChangeableValue<INode>(() -> _root, value -> _root = value, () -> _root = new SimpleHTMLNode("")));

		for(int targetNodeIndex = 0; targetNodeIndex < targetNodes.size(); targetNodeIndex++) {
			ChangeableValue<INode> node = targetNodes.get(targetNodeIndex);

			if(node.get() instanceof SelfClosingTagNode) {
				// noop
			}
			else if(node.get() instanceof TagNode) {
				TagNode tagNode = (TagNode)node.get();

				targetNodes.add(new ChangeableValue<INode>(() -> tagNode.inner, value -> tagNode.inner = value, () -> tagNode.inner = new SimpleHTMLNode("")));
			}
			else if(node.get() instanceof ComplexedNode) {
				ComplexedNode nodes = (ComplexedNode)node.get();

				for(int index = 0; index < nodes.children.size(); index++) {
					final int f_index = index;
					targetNodes.add(new ChangeableValue<INode>(() -> nodes.children.get(f_index), value -> nodes.children.set(f_index, value), () -> nodes.children.remove(f_index)));
				}
			}
			else if(node.get() instanceof SimpleHTMLNode) {
				// noop
			}
			else {
				throw null; // never
			}
		}

		for(int targetNodeIndex = targetNodes.size() - 1; 0 <= targetNodeIndex; targetNodeIndex--) {
			ChangeableValue<INode> node = targetNodes.get(targetNodeIndex);

			if(node.get() instanceof SelfClosingTagNode) {
				// noop
			}
			else if(node.get() instanceof TagNode) {
				// noop
			}
			else if(node.get() instanceof ComplexedNode) {
				ComplexedNode nodes = (ComplexedNode)node.get();

				// FIXME SimpleHTMLNode が連続する場所は今のところ無いはず。 // orig: // FIXME SimpleHTMLNode \u304c\u9023\u7d9a\u3059\u308b\u5834\u6240\u306f\u4eca\u306e\u3068\u3053\u308d\u7121\u3044\u306f\u305a\u3002
				for(int index = nodes.children.size() - 2; 0 <= index; index--) {
					if(
							nodes.children.get(index + 0) instanceof SimpleHTMLNode &&
							nodes.children.get(index + 1) instanceof SimpleHTMLNode
							) {
						((SimpleHTMLNode)nodes.children.get(index)).html += ((SimpleHTMLNode)nodes.children.get(index + 1)).html;
						nodes.children.remove(index + 1);
					}
				}

				if(nodes.children.size() == 0) {
					node.delete();
				}
				else if(nodes.children.size() == 1) {
					node.set(nodes.children.get(0));
				}
			}
			else if(node.get() instanceof SimpleHTMLNode) {
				SimpleHTMLNode simpleHTMLNode = (SimpleHTMLNode)node.get();

				if(simpleHTMLNode.html.isEmpty()) {
					node.delete();
				}
			}
			else {
				throw null; // never
			}
		}
	}

	private class ChangeableValue<T> {
		private Supplier<T> _getter;
		private Consumer<T> _setter;
		private Runnable _deleter;

		public ChangeableValue(Supplier<T> getter, Consumer<T> setter, Runnable deleter) {
			_getter = getter;
			_setter = setter;
			_deleter = deleter;
		}

		public T get() {
			return _getter.get();
		}

		public void set(T value) {
			_setter.accept(value);
		}

		public void delete() {
			_deleter.run();
		}
	}

	public INode root() {
		return _root;
	}

	public interface INode {
		// none
	}

	public class SelfClosingTagNode implements INode {
		public HTMLParser.Tag selfClosingTag;

		public SelfClosingTagNode(HTMLParser.Tag tag) {
			selfClosingTag = tag;
		}
	}

	public class TagNode implements INode {
		public HTMLParser.Tag openTag;
		public INode inner;
		public HTMLParser.Tag closingTag;
	}

	public class ComplexedNode implements INode {
		public List<INode> children = new ArrayList<INode>();
	}

	public class SimpleHTMLNode implements INode {
		public String html;

		public SimpleHTMLNode(String html) {
			this.html = html;
		}
	}
}
