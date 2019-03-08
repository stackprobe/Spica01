package violet.labo.module.httpserverfw.html.tag;

import java.util.Map;

import violet.labo.module.httpserverfw.ContextInfo;

public interface ITag {
	void setAttributes(Map<String, String> attributes);
	void init();
	void access(ContextInfo context);
	String getHTML(ContextInfo context, String innerHtml);
}
