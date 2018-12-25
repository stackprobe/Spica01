package tests.charlotte.tools;

import charlotte.tools.HTTPServer;
import charlotte.tools.HTTPServerChannel;
import charlotte.tools.StringTools;

public class HTTPServerTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		HTTPServer hs = new HTTPServer() {
			@Override
			public void httpConnected(HTTPServerChannel hsChannel) throws Exception {
				hsChannel.resContentType = "text/html; charset=US-ASCII";
				hsChannel.resBody = "<html><body><h1>200</h1></body></html>".getBytes(StringTools.CHARSET_ASCII);
			}
		};

		hs.perform();
	}

	private static StringBuffer _buff;

	private static void test02() throws Exception {
		HTTPServer hs = new HTTPServer() {
			@Override
			public void httpConnected(HTTPServerChannel hsChannel) throws Exception {
				//hsChannel.resStatus = 200;
				hsChannel.resContentType = "text/html; charset=UTF-8";

				_buff = new StringBuffer();

				_buff.append("<html>");
				_buff.append("<body>");
				_buff.append("<table border=\"1\">");

				addTr("method", hsChannel.method);
				addTr("path", hsChannel.path);
				addTr("httpVersion", hsChannel.httpVersion);

				for(String[] pair : hsChannel.headerPairs) {
					addTr("header_" + pair[0], pair[1]);
				}
				addTr("body-length", "" + hsChannel.body.length);

				_buff.append("</table>");
				_buff.append("</body>");
				_buff.append("</html>");

				hsChannel.resBody = _buff.toString().getBytes(StringTools.CHARSET_UTF8);
			}

			private void addTr(String name, String value) {
				_buff.append("<tr>");
				_buff.append("<th>");
				_buff.append(name);
				_buff.append("</th>");
				_buff.append("<td>");
				_buff.append(value);
				_buff.append("</td>");
				_buff.append("</tr>");
			}
		};

		System.out.println("Press ENTER to stop the server.");

		hs.perform();

		System.out.println("Stopped the server.");
	}
}
