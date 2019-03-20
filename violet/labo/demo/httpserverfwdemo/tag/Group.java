package violet.labo.demo.httpserverfwdemo.tag;

import violet.labo.module.httpserverfw.ContextInfo;
import violet.labo.module.httpserverfw.html.tag.TagBase;

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
