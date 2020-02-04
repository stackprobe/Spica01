package wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag;

import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;

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
		return "<fieldset><legend>SampleTag</legend>" + innerHtml + "</fieldset>";
	}
}
