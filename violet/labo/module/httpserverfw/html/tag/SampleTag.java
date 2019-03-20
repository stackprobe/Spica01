package violet.labo.module.httpserverfw.html.tag;

import violet.labo.module.httpserverfw.ContextInfo;

public class SampleTag extends TagBase {
	@Override
	public void init() {
		// noop
	}

	@Override
	public void access(ContextInfo context) {
		// noop
	}

	@Override
	public String getHTML(ContextInfo context, String innerHtml) {
		return "<div class=\"SampleTag\">" + innerHtml + "</div>";
	}
}
