package violet.labo.module.httpserverfw;

import java.io.File;

import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;
import violet.labo.module.httpserverfw.html.HTMLParser;
import violet.labo.module.httpserverfw.html.HTMLProcessor;
import violet.labo.module.httpserverfw.html.HTMLTree;

public class HTMLDesigner {
	private HTMLProcessor _processor;

	public HTMLDesigner(File f) {
		String html = RTError.get(() -> FileTools.readAllText(f.getCanonicalPath(), StringTools.CHARSET_UTF8));

		HTMLParser parser = new HTMLParser(html);
		parser.parse();

		HTMLTree tree = new HTMLTree(parser.sequence());
		tree.parse();

		HTMLProcessor processor = new HTMLProcessor(tree.root());
		processor.parse();

		_processor = processor;
	}

	public String getHTML(ContextInfo context) {
		try {
			return _processor.getHTML(context);
		}
		catch(AnotherHTML e) {
			return e.getHTML();
		}
	}
}
