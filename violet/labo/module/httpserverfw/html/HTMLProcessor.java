package violet.labo.module.httpserverfw.html;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IntTools;
import charlotte.tools.IterableTools;
import charlotte.tools.RTError;
import charlotte.tools.ReflectTools;
import violet.labo.module.httpserverfw.Config;
import violet.labo.module.httpserverfw.ContextInfo;
import violet.labo.module.httpserverfw.html.tag.ITag;

public class HTMLProcessor {
	private HTMLTree.INode _root;

	public HTMLProcessor(HTMLTree.INode root) {
		_root = root;
	}

	private List<Part> _parts = new ArrayList<Part>();
	private List<ITag> _tags = new ArrayList<ITag>();

	public void parse() {
		parseNode(_root, -1);

		for(int index = 0; index < _parts.size(); index++) {
			parsePart(_parts.get(index));
		}
		postParseParts();
	}

	private void parseNode(HTMLTree.INode node, int parentIndex) {
		Part part;

		if(node instanceof HTMLTree.SelfClosingTagNode) {
			part = new SelfClosingTagPart();
		}
		else if(node instanceof HTMLTree.TagNode) {
			part = new TagPart();
		}
		else if(node instanceof HTMLTree.ComplexedNode) {
			part = new ComplexedPart();
		}
		else if(node instanceof HTMLTree.SimpleHTMLNode) {
			part = new SimpleHTMLPart();
		}
		else {
			throw null; // never
		}

		part.selfNode = node;
		part.parentIndex = parentIndex;
		part.selfIndex = _parts.size();

		_parts.add(part);
	}

	private void parsePart(Part part) {
		part.parsePart();

		if(part.parentIndex != -1) {
			Part parentPart = _parts.get(part.parentIndex);

			parentPart.linkToChild(part);
		}
	}

	private void postParseParts() {
		for(Part part : _parts) {
			part.postParsePart();
		}
		for(ITag tag : _tags) {
			tag.activate();
		}
		for(ITag tag : IterableTools.reverse(_tags)) {
			tag.activate2();
		}
	}

	public String getHTML(ContextInfo context) {
		for(ITag tag : _tags) {
			tag.access(context);
		}
		for(ITag tag : IterableTools.reverse(_tags)) {
			tag.access2(context);
		}
		String[] htmls = new String[_parts.size()];

		for(int index = _parts.size() - 1; 0 <= index; index--) {
			_parts.get(index).run(htmls, context);
		}
		return htmls[0];
	}

	private abstract class Part {
		public HTMLTree.INode selfNode;
		public int parentIndex;
		public int selfIndex;
		public void parsePart() { }
		public void linkToChild(Part childPart) { }
		public void postParsePart() { }
		public abstract void run(String[] htmls, ContextInfo context);
	}

	private abstract class TagPartBase extends Part {
		public ITag getTag(HTMLParser.Tag parserTag) {
			ITag tag = createTag(parserTag.name);

			tag.setAttributes(parserTag.attributes);
			tag.setParent(getParentTag(parentIndex));
			tag.init();

			return tag;
		}

		public ITag createTag(String tagName) {
			for(String tagPackage : Config.i().TAG_PACKAGES) {
				String className = tagPackage + "." + tagName;

				try {
					ReflectTools.ConstructorUnit ctor = ReflectTools.getConstructor(Class.forName(className), new Object[0]);
					ITag tag = (ITag)ctor.invoke(new Object[0]);
					return tag;
				}
				catch(Throwable e) {
					System.out.println(e.getMessage());
				}
			}
			throw new RTError("No such tag: " + tagName);
		}

		private ITag getParentTag(int index) {
			while(index != -1) {
				Part part = _parts.get(index);

				if(part instanceof TagPart) {
					return ((TagPart)part).tag;
				}
				index = part.parentIndex;
			}
			return null;
		}
	}

	private class SelfClosingTagPart extends TagPartBase {
		public ITag tag;

		@Override
		public void parsePart() {
			tag = getTag(((HTMLTree.SelfClosingTagNode)selfNode).selfClosingTag);
			_tags.add(tag);
		}

		@Override
		public void run(String[] htmls, ContextInfo context) {
			htmls[selfIndex] = tag.getHTML(context, "");
		}
	}

	private class TagPart extends TagPartBase {
		public ITag tag;
		public int innerIndex;

		@Override
		public void parsePart() {
			tag = getTag(((HTMLTree.TagNode)selfNode).openTag);
			parseNode(((HTMLTree.TagNode)selfNode).inner, selfIndex);
			_tags.add(tag);
		}

		@Override
		public void linkToChild(Part childPart) {
			innerIndex = childPart.selfIndex;
		}

		@Override
		public void run(String[] htmls, ContextInfo context) {
			htmls[selfIndex] = tag.getHTML(context, htmls[innerIndex]);
		}
	}

	private class ComplexedPart extends Part {
		public List<Integer> indexList = new ArrayList<Integer>();
		public int[] indexes;

		@Override
		public void parsePart() {
			for(HTMLTree.INode node : ((HTMLTree.ComplexedNode)selfNode).children) {
				parseNode(node, selfIndex);
			}
		}

		@Override
		public void linkToChild(Part childPart) {
			indexList.add(childPart.selfIndex);
		}

		@Override
		public void postParsePart() {
			indexes = IntTools.toArray(indexList);
			indexList = null;
		}

		@Override
		public void run(String[] htmls, ContextInfo context) {
			StringBuffer buff = new StringBuffer();

			for(int index : indexes) {
				buff.append(htmls[index]);
				htmls[index] = null;
			}
			htmls[selfIndex] = buff.toString();
		}
	}

	private class SimpleHTMLPart extends Part {
		public String html;

		@Override
		public void parsePart() {
			html = ((HTMLTree.SimpleHTMLNode)selfNode).html;
		}

		@Override
		public void run(String[] htmls, ContextInfo context) {
			htmls[selfIndex] = html;
		}
	}
}
