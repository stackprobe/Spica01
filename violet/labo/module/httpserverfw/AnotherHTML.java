package violet.labo.module.httpserverfw;

public class AnotherHTML extends RuntimeException {
	private String _html;

	public AnotherHTML(String html) {
		_html = html;
	}

	public String getHTML() {
		return _html;
	}
}
