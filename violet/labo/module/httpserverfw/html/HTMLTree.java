package violet.labo.module.httpserverfw.html;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.RTError;

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
		_root = getNode();

		if(_rPos != _sequence.size()) {
			throw new RTError("Maybe HTML structure broken");
		}
		check();
		normalize();
	}

	private void check() {
		throw null; // TODO
	}

	private void normalize() {
		throw null; // TODO
	}

	private INode getNode() {
		ComplexedNode dest = new ComplexedNode();

		for(; ; ) {
			dest.children.add(new SimpleHTMLNode((String)next()));

			if(_sequence.size() <= _rPos) {
				break;
			}
			HTMLParser.Tag tag = (HTMLParser.Tag)next();

			if(tag.closing) {
				_rPos--;
				break;
			}
			if(tag.selfClosing) {
				dest.children.add(new SelfClosingTagNode(tag));
			}
			else {
				TagNode tagNode = new TagNode();

				tagNode.openTag = tag;
				tagNode.inner = getNode();
				tagNode.closingTag = (HTMLParser.Tag)next();

				dest.children.add(tagNode);
			}
		}
		return dest;
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
