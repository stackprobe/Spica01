package violet.labo.module.httpserverfw;

public class Config {
	private static Config _i = null;

	public static Config i() {
		if(_i == null) {
			_i = new Config();
		}
		return _i;
	}

	private Config() {
		// TODO ファイルからも読み込み。
	}

	public int PORT_NO = 8080;
	public String DOR_ROOT_DIR = "C:/var/httpserverfw/DocRoot";
	public String MIME_TYPE_FILE = "C:/var/httpserverfw/MimeType.tsv";
	public String INDEX_PAGE_NAME = "index.html";
	//public String INDEX_PAGE_NAME = "index.htm";
	public String SERVICE_PAGE_SUFFIX = ".html";
	//public String SERVICE_PAGE_SUFFIX = ".htm";
}
