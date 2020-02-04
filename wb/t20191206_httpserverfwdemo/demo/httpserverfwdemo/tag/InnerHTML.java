package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo.tag;

import java.io.File;

import charlotte.tools.RTError;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.HTMLDesigner;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag.TagBase;

public class InnerHTML extends TagBase {
	private HTMLDesigner _designer;

	@Override
	public void init() {
		String file = getAttributes().get("file");

		if(file == null) {
			throw new RTError("no file");
		}
		_designer = new HTMLDesigner(new File(file));
	}

	@Override
	public void access(ContextInfo context) {
		// noop
	}

	@Override
	public String getHTML(ContextInfo context, String innerHtml) {
		return _designer.getHTML(context);
	}
}
