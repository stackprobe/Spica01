package violet.labo.demo.httpserverfwdemo.tag;

import charlotte.tools.SecurityTools;
import violet.labo.module.httpserverfw.ContextInfo;
import violet.labo.module.httpserverfw.html.tag.TagBase;

public class RedWhite extends TagBase {
	private RedWhiteGroup _parent;

	@Override
	public void init() {
		_parent = (RedWhiteGroup)getParent(tag -> tag instanceof RedWhiteGroup);
	}

	private boolean _red;

	@Override
	public void access(ContextInfo context) {
		_red = SecurityTools.cRandom.getInt(2) == 1;
	}

	@Override
	public String getHTML(ContextInfo context, String innerHtml) {
		return "This is " + (_red ? "RED" : "WHITE") + " color, Parent is " + (_parent.isRed() ? "RED" : "WHITE") + " color.";
	}

	public boolean isRed() {
		return _red;
	}
}
