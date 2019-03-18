package tests.charlotte.tools;

import java.io.ByteArrayOutputStream;

import charlotte.tools.SockClient;
import charlotte.tools.StringTools;

public class SockClientTest {
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
		System.out.println("connect.1");
		try(SockClient client = new SockClient()) {
			System.out.println("connect.2");

			client.connect("www.example.com", 80);
			client.send("GET / HTTP/1.1\r\nHost: www.example.com\r\n\r\n".getBytes(StringTools.CHARSET_ASCII));
			String firstLine = recvLine(client);
			System.out.println("firstLine: " + firstLine);

			int contentLength = -1;

			for(; ; ) {
				String header = recvLine(client);

				if(header.isEmpty()) {
					break;
				}
				System.out.println("header: " + header);

				if(StringTools.startsWithIgnoreCase(header, "Content-Length:")) {
					contentLength = Integer.parseInt(header.substring(header.indexOf(':') + 1).trim());

					System.out.println("contentLength: " + contentLength);
				}
			}
			if(contentLength == -1) {
				throw new Exception("no Content-Length");
			}
			byte[] body = client.recv(contentLength);
			String sBody = new String(body, StringTools.CHARSET_ASCII);

			System.out.println("body: " + sBody);

			System.out.println("disconnect.1");
		}
		System.out.println("disconnect.2");
	}

	private static String recvLine(SockClient client) throws Exception {
		try(ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
			for(; ; ) {
				int chr = client.recv(1)[0] & 0xff;

				if(chr == 0x0d) { // CR
					continue;
				}
				if(chr == 0x0a) { // LF
					break;
				}
				writer.write(chr);
			}
			return new String(writer.toByteArray(), StringTools.CHARSET_ASCII);
		}
	}
}
