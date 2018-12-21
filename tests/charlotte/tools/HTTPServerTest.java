package tests.charlotte.tools;

import charlotte.tools.HTTPServer;
import charlotte.tools.HTTPServerChannel;
import charlotte.tools.StringTools;

public class HTTPServerTest {
	public static void main(String[] args) {
		try {
			test01();
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
				hsChannel.resStatus = 200;
				hsChannel.resContentType = "text/html; charset=UTF-8";

				StringBuffer buff = new StringBuffer();

				buff.append("<html>");
				buff.append("<body>");
				buff.append("<table border=\"1\">");

				addTr(buff, "method", hsChannel.method);
				addTr(buff, "path", hsChannel.path);
				addTr(buff, "httpVersion", hsChannel.httpVersion);

				for(String[] pair : hsChannel.headerPairs) {
					addTr(buff, "header_" + pair[0], pair[1]);
				}
				addTr(buff, "body-length", "" + hsChannel.body.length);

				buff.append("</table>");
				buff.append("</body>");
				buff.append("</html>");

				hsChannel.resBody = buff.toString().getBytes(StringTools.CHARSET_UTF8);
			}

			private void addTr(StringBuffer buff, String name, String value) {
				buff.append("<tr>");
				buff.append("<th>");
				buff.append(name);
				buff.append("</th>");
				buff.append("<td>");
				buff.append(value);
				buff.append("</td>");
				buff.append("</tr>");
			}
		};

		hs.start();

		System.out.println("Press ENTER to stop the server.");
		System.in.read();

		hs.stop_B();

		System.out.println("Stopped the server.");
	}
}
