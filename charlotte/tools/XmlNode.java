package charlotte.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

	public static XmlNode loadFile(String xmlFile) throws Exception {
		XmlNode root = loadNode(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xmlFile)));
		root = root.children.get(0);
		postLoad(root);
		return root;
	}

	public static XmlNode loadNode(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		List<XmlNode> children = new ArrayList<XmlNode>();

		if(attributes != null) {
			for(int i = 0; i < attributes.getLength(); i++) {
				Node attribute = attributes.item(i);

				children.add(new XmlNode(
						attribute.getNodeName(),
						attribute.getNodeValue()
						));
			}
		}
		NodeList nodeChildren = node.getChildNodes();
		StringBuffer text = new StringBuffer();

		for(int i = 0; i < nodeChildren.getLength(); i++) {
			Node nodeChild = nodeChildren.item(i);

			switch(nodeChild.getNodeType()) {
			case Node.TEXT_NODE:
				text.append(nodeChild.getNodeValue());
				break;

			case Node.ELEMENT_NODE:
				children.add(loadNode(nodeChild));
				break;
			}
		}
		return new XmlNode(
				node.getNodeName(),
				text.toString(),
				children
				);
	}

	private static void postLoad(XmlNode node) {
		node.name = StringTools.antiNull(node.name);
		node.value = StringTools.antiNull(node.value);

		{
			int index = node.name.indexOf(':');

			if(index != -1) {
				node.name = node.name.substring(index + 1);
			}
		}

		node.name = node.name.trim();
		node.value = node.value.trim();

		for(XmlNode child : node.children) {
			postLoad(child);
		}
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
