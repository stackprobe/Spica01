package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo.tag;

import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag.TagBase;

public class Dummy extends TagBase {
	private String _title;

	@Override
	public void init() {
		System.out.println("Dummy: " + this);
		_title = getAttributes().get("title");
		System.out.println("parent: " + getParent());
		System.out.println("parent2: " + (getParent() == null ? null : ((TagBase)getParent()).getParent()));

		//getParent(tag -> true);
	}

	@Override
	public void activate() {
		/*
		System.out.println("dive >");
		dive(tag -> System.out.println("childTag: " + tag));
		System.out.println("< dive");
		*/
	}

	@Override
	public void access(ContextInfo context) {
		System.out.println("Dummy_access: " + _title);
		//throw new AnotherHTML("<h1>error</h1>");
	}

	@Override
	public String getHTML(ContextInfo context, String innerHtml) {
		System.out.println("Dummy_getHTML: " + _title);
		return "<fieldset><legend>" + _title + "</legend>" + innerHtml + "</fieldset>";
	}
}
