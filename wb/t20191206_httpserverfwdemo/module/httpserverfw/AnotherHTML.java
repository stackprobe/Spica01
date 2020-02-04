package wb.t20191206_httpserverfwdemo.module.httpserverfw;

public class AnotherHTML extends RuntimeException {
	private String _html;

	public AnotherHTML(String html) {
		_html = html;
	}

	public String getHTML() {
		return _html;
	}
}
