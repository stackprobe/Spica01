package violet.labo.demo.httpserverfwdemo.tag;

import violet.labo.module.httpserverfw.ContextInfo;
import violet.labo.module.httpserverfw.html.tag.TagBase;

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
