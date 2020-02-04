package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo.tag;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.SecurityTools;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag.TagBase;

public class RedWhiteGroup extends TagBase {
	@Override
	public void init() {
		// noop
	}

	private List<RedWhite> _children = new ArrayList<RedWhite>();

	@Override
	public void activate() {
		dive(tag -> {
			if(tag instanceof RedWhite) {
				_children.add((RedWhite)tag);
			}
		});
	}

	private boolean _red;

	@Override
	public void access(ContextInfo context) {
		_red = SecurityTools.cRandom.getInt(2) == 1;
	}

	@Override
	public String getHTML(ContextInfo context, String innerHtml) {
		return "Group has " + getRedCount() + " RED colors, This is " + (_red ? "RED" : "WHITE") + " color.<br/>" + innerHtml;
	}

	private int getRedCount() {
		int count = 0;

		for(RedWhite tag : _children) {
			if(tag.isRed()) {
				count++;
			}
		}
		return count;
	}

	public boolean isRed() {
		return _red;
	}
}
