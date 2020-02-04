package wb.t20191206_httpserverfwdemo.module.httpserverfw;

import java.io.File;

import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.HTMLParser;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.HTMLProcessor;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.HTMLTree;

public class HTMLDesigner {
	private HTMLProcessor _processor;

	public HTMLDesigner(File f) {
		System.out.println("HTMLDesigner: " + f);

		String html = RTError.get(() -> FileTools.readAllText(f.getCanonicalPath(), Config.i().SERVICE_PAGE_CHARSET));

		HTMLParser parser = new HTMLParser(html);
		parser.parse();

		HTMLTree tree = new HTMLTree(parser.sequence());
		tree.parse();

		HTMLProcessor processor = new HTMLProcessor(tree.root());
		processor.parse();

		_processor = processor;

		System.out.println("HTMLDesigner: " + f + " OK!");
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
