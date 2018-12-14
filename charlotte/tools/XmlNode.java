package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class XmlNode {
	public String name;
	public String value;
	public List<XmlNode> children;

	public XmlNode() {
		this("");
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
		String name = this.name;

		if(name.isEmpty()) {
			name = "element";
		}
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

	// TODO
}
