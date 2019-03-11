package violet.labo.module.httpserverfw.html.tag;

import violet.labo.module.httpserverfw.ContextInfo;

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
