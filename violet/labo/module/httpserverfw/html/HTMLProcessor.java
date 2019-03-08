package violet.labo.module.httpserverfw.html;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IntTools;
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
		if(part instanceof SelfClosingTagPart) {
			SelfClosingTagPart selfClosingTagPart = (SelfClosingTagPart)part;
			ITag tag = getTag(((HTMLTree.SelfClosingTagNode)part.selfNode).selfClosingTag);

			selfClosingTagPart.tag = tag;
		}
		else if(part instanceof TagPart) {
			TagPart tagPart = (TagPart)part;
			ITag tag = getTag(((HTMLTree.TagNode)part.selfNode).openTag);

			tagPart.tag = tag;

			parseNode(((HTMLTree.TagNode)part.selfNode).inner, part.selfIndex);
		}
		else if(part instanceof ComplexedPart) {
			ComplexedPart parts = (ComplexedPart)part;

			for(HTMLTree.INode node : ((HTMLTree.ComplexedNode)part.selfNode).children) {
				parseNode(node, parts.selfIndex);
			}
		}
		else if(part instanceof SimpleHTMLPart) {
			SimpleHTMLPart simpleHTMLPart = (SimpleHTMLPart)part;

			simpleHTMLPart.html = ((HTMLTree.SimpleHTMLNode)part.selfNode).html;
		}
		else {
			throw null; // never
		}

		if(part.parentIndex != -1) {
			Part parentPart = _parts.get(part.parentIndex);

			if(parentPart instanceof SelfClosingTagPart) {
				// noop
			}
			else if(parentPart instanceof TagPart) {
				((TagPart)parentPart).innerIndex = part.selfIndex;
			}
			else if(parentPart instanceof ComplexedPart) {
				((ComplexedPart)parentPart).indexList.add(part.selfIndex);
			}
			else if(parentPart instanceof SimpleHTMLPart) {
				// noop
			}
			else {
				throw null; // never
			}
		}
	}

	private ITag getTag(HTMLParser.Tag parserTag) {
		ITag tag = createTag(parserTag.name);

		tag.setAttributes(parserTag.attributes);
		tag.init();

		return tag;
	}

	private ITag createTag(String tagName) {
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
		throw new RTError("No such tag");
	}

	private void postParseParts() {
		for(Part part : _parts) {
			if(part instanceof SelfClosingTagPart) {
				SelfClosingTagPart selfClosingTagPart = (SelfClosingTagPart)part;

				_tags.add(selfClosingTagPart.tag);
			}
			else if(part instanceof TagPart) {
				TagPart tagPart = (TagPart)part;

				_tags.add(tagPart.tag);
			}
			else if(part instanceof ComplexedPart) {
				ComplexedPart complexedPart = (ComplexedPart)part;

				complexedPart.indexes = IntTools.toArray(complexedPart.indexList);
				complexedPart.indexList = null;
			}
			else if(part instanceof SimpleHTMLPart) {
				// noop
			}
			else {
				throw null; // never
			}
		}
	}

	public String getHTML(ContextInfo context) {
		for(ITag tag : _tags) {
			tag.access(context);
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
		public abstract void run(String[] htmls, ContextInfo context);
	}

	private class SelfClosingTagPart extends Part {
		public ITag tag;

		@Override
		public void run(String[] htmls, ContextInfo context) {
			htmls[selfIndex] = tag.getHTML(context, "");
		}
	}

	private class TagPart extends Part {
		public ITag tag;
		public int innerIndex;

		@Override
		public void run(String[] htmls, ContextInfo context) {
			htmls[selfIndex] = tag.getHTML(context, htmls[innerIndex]);
		}
	}

	private class ComplexedPart extends Part {
		public List<Integer> indexList = new ArrayList<Integer>();
		public int[] indexes;

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
		public void run(String[] htmls, ContextInfo context) {
			htmls[selfIndex] = html;
		}
	}
}
