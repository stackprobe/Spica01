package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo.tag;

import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag.TagBase;

public class Group extends TagBase {
	@Override
	public void init() {
		System.out.println("Group: " + this);
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
