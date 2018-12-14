package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class XmlNode {
	public String name;
	public String value;
	public List<XmlNode> children;

	public XmlNode() {
		this("element");
	}

	public XmlNode(String name) {
		this(name, "");
	}

	public XmlNode(String name, String value) {
		this(name, value, new ArrayList<XmlNode>());
	}

	public XmlNode(String name, String value, List<XmlNode> children) {
		this.name = name;
		this.value = value;
		this.children = children;
	}

	public static XmlNode loadFile(String xmlFile) {
		throw null; // TODO
	}

	private static void postLoad(XmlNode node) {
		// TODO
	}

	public void writeToFile(String xmlFile) throws Exception {
		FileTools.writeAllLines(xmlFile, getLines(), StringTools.CHARSET_UTF8);
	}

	private static final String INDENT = "\t";

	private List<String> getLines() {
		List<String> dest = new ArrayList<String>();
		dest.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		addTo(dest, "");
		return dest;
	}

	private void addTo(List<String> dest, String indent) {
		if(children.size() != 0) {
			dest.add(indent + "<" + name + ">" + value);

			for(XmlNode child : children) {
				child.addTo(dest, indent + INDENT);
			}
			dest.add(indent + "</" + name + ">");
		}
		else if(value.isEmpty() == false) {
			dest.add(indent + "<" + name + ">" + value + "</" + name + ">");
		}
		else {
			dest.add(indent + "<" + name + "/>");
		}
	}

	public XmlNode ref(String path) {
		List<XmlNode> dest = collect(path);

		if(dest.size() == 0) {
			return add(path);
		}
		return dest.get(0);
	}

	private List<XmlNode> collect(String path) {
		List<XmlNode> dest = new ArrayList<XmlNode>();
		collect(StringTools.tokenize(path, "/"), 0, dest);
		return dest;
	}

	private void collect(List<String> pTkns, int pTknIndex, List<XmlNode> dest) {
		if(pTknIndex < pTkns.size()) {
			for(XmlNode child : children) {
				if(child.name.equals(pTkns.get(pTknIndex))) {
					child.collect(pTkns, pTknIndex + 1, dest);
				}
			}
		}
		else {
			dest.add(this);
		}
	}

	public void delete(String path) {
		delete(StringTools.tokenize(path, "/"), 0);
	}

	public void delete(List<String> pTkns, int pTknIndex) {
		if(pTknIndex + 1 < pTkns.size()) {
			for(XmlNode child : children) {
				if(child.name.equals(pTkns.get(pTknIndex))) {
					child.delete(pTkns, pTknIndex + 1);
				}
			}
		}
		else {
			for(int index = children.size() - 1; 0 <= index; index++) {
				if(children.get(index).name.equals(pTkns.get(pTknIndex))) {
					children.remove(index);
				}
			}
		}
	}

	public XmlNode add(String path) {
		return add(StringTools.tokenize(path, "/"), 0);
	}

	private XmlNode add(List<String> pTkns, int pTknIndex) {
		if(pTknIndex < pTkns.size()) {
			XmlNode child = find(children, pTkns.get(pTknIndex));

			if(child == null) {
				child = new XmlNode(pTkns.get(pTknIndex));
				children.add(child);
			}
			return child.add(pTkns, pTknIndex + 1);
		}
		else {
			return this;
		}
	}

	private static XmlNode find(List<XmlNode> children, String name) {
		for(XmlNode child : children) {
			if(child.name.equals(name)) {
				return child;
			}
		}
		return null;
	}
}
