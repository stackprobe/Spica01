package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo.tag;

import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag.TagBase;

public class Element extends TagBase {
	@Override
	public void init() {
		System.out.println("Element: " + getParent(tag -> tag instanceof Group));
	}

	@Override
	public void access(ContextInfo context) {
		// noop
	}

	@Override
	public String getHTML(ContextInfo context, String innerHtml) {
		return innerHtml;
	}
}
