package charlotte.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class HTTPClient {
	private String _url;
	private Map<String, String> _headerFields = MapTools.<String>createIgnoreCase();

	public HTTPClient(String url) {
		_url = url;
	}

	public void setAuthorization(String user, String password) throws Exception {
		String plain = user + ":" + password;
		String enc = Base64.getEncoder().encodeToString(plain.getBytes(StringTools.CHARSET_UTF8));
		addHeader("Authorization", "Basic " + enc);
	}

	public void addHeader(String name, String value) {
		_headerFields.put(name, value);
	}

	public int connectTimeoutMillis = 20000;
	public int idleTimeoutMillis = 10000;
	public String proxyDomain = null;
	public int proxyPortNo = 8080;

	public void head() throws Exception {
		send(null, "HEAD");
	}

	public void get() throws Exception {
		send(null);
	}

	public void post(byte[] body) throws Exception {
		send(body);
	}

	public void send(byte[] body) throws Exception {
		send(body, body == null ? "GET" : "POST");
	}

	public void send(byte[] body, String method) throws Exception {
		HttpURLConnection con = null;

		try {
			{
				URL url = new URL(_url);

				if(proxyDomain == null) {
					con = (HttpURLConnection)url.openConnection();
				}
				else {
					con = (HttpURLConnection)url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyDomain, proxyPortNo)));
				}
			}

			con.setRequestMethod(method);
			con.setDoOutput(body != null);
			con.setInstanceFollowRedirects(false);
			con.setConnectTimeout(connectTimeoutMillis);
			con.setReadTimeout(idleTimeoutMillis);

			for(String name : _headerFields.keySet()) {
				con.setRequestProperty(name, _headerFields.get(name));
			}
			con.connect();

			if(body != null) {
				try(OutputStream os = con.getOutputStream()) {
					os.write(body);
				}
			}

			for(String name : con.getHeaderFields().keySet()) {
				List<String> valueTokens = con.getHeaderFields().get(name);
				String value = String.join(" ", valueTokens);

				if(name == null) {
					resFirstLine = value;
					resFirstLineTokens = value.split("[ ]");
					resHTTPVersion = resFirstLineTokens[0];
					resStatus = Integer.parseInt(resFirstLineTokens[1]);
					resReasonPhrase = resFirstLineTokens[2];
				}
				else {
					resHeaderFields.put(name, value);
				}
			}

			try(InputStream reader = con.getInputStream()) {
				resBody = FileTools.readToEnd(reader);
			}
		}
		finally {
			if(con != null) {
				con.disconnect();
			}
		}
	}

	public String resFirstLine = null;
	public String[] resFirstLineTokens = null;
	public String resHTTPVersion = null;
	public int resStatus = -1;
	public String resReasonPhrase = null;
	public Map<String, String> resHeaderFields = MapTools.<String>createIgnoreCase();
	public byte[] resBody = null;
}
