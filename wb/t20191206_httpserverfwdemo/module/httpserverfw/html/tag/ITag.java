package wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag;

import java.util.Map;

import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;

public interface ITag {
	void setAttributes(Map<String, String> attributes);
	void setParent(ITag parent);
	void init();
	default void activate() { }
	default void activate2() { }
	void access(ContextInfo context);
	default void access2(ContextInfo context) { }
	String getHTML(ContextInfo context, String innerHtml);
}
