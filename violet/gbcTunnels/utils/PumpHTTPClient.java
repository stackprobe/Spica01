package violet.gbcTunnels.utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.SockChannel;
import charlotte.tools.SockClient;
import charlotte.tools.StringTools;

public class PumpHTTPClient {
	public String proxyDomain = null;
	public int proxyPortNo = -1;

	// <---- prm

	public byte[] resBody;

	// <---- res

	public void get(String url) throws Exception {
		String domainPath = url.substring(7);
		String domain;
		String path;
		int portNo;

		{
			int index = domainPath.indexOf('/');

			if(index == -1) {
				domain = domainPath;
				path = "/";
			}
			else {
				domain = domainPath.substring(0, index);
				path = domainPath.substring(index);
			}

			index = domain.lastIndexOf(':');

			if(index == -1) {
				portNo = 80;
			}
			else {
				portNo = Integer.parseInt(domain.substring(index + 1));
				domain = domain.substring(0, index);
			}
		}

		try(
				AutoCloseable unsection_client = SockChannel.critical.unsection();
				SockClient client = new SockClient();
				) {
			if(this.proxyDomain == null) {
				client.connect(domain, portNo);
			}
			else {
				client.connect(this.proxyDomain, this.proxyPortNo);
			}

			client.send("GET ".getBytes(StringTools.CHARSET_ASCII));

			if(this.proxyDomain == null) {
				client.send(path.getBytes(StringTools.CHARSET_ASCII));
			}
			else if(portNo == 80){
				client.send(String.format("http://%s%s", domain, path).getBytes(StringTools.CHARSET_ASCII));
			}
			else {
				client.send(String.format("http://%s:%d%s", domain, portNo, path).getBytes(StringTools.CHARSET_ASCII));
			}

			client.send(" HTTP/1.1\r\n".getBytes(StringTools.CHARSET_ASCII));

			if(portNo == 80) {
				client.send(String.format("Host: %s\r\n", domain).getBytes(StringTools.CHARSET_ASCII));
			}
			else {
				client.send(String.format("Host: %s:%d\r\n", domain, portNo).getBytes(StringTools.CHARSET_ASCII));
			}

			client.send("Connection: close\r\n\r\n".getBytes(StringTools.CHARSET_ASCII));

			{
				String line = readLine(client);
				String[] tokens = line.split("[ ]");
				int status = Integer.parseInt(tokens[1]);

				if(status != 200) {
					throw new Exception("Bad status: " + status);
				}
			}

			int contentLength = -1;
			boolean chunked = false;

			for(; ; ) {
				String line = readLine(client);

				if(line.isEmpty()) {
					break;
				}

				String name;
				String value;

				{
					int index = line.indexOf(':');

					if(index == -1) {
						continue;
					}
					name = line.substring(0, index).trim().toLowerCase();
					value = line.substring(index + 1).trim();

					// dont care for header-content folding
				}

				if(name.equals("content-length")) {
					contentLength = Integer.parseInt(value);
				}
				else if(name.equals("transfer-encoding")) {
					chunked = value.toLowerCase().equals("chunked");
				}
			}

			if(chunked) {
				List<byte[]> parts = new ArrayList<byte[]>();

				for(; ; ) {
					String line = readLine(client);

					{
						int extPos = line.indexOf(';');

						if(extPos != -1) {
							line = line.substring(0, extPos); // erase chunked-extension
						}
					}

					line = line.trim();

					int partSize = Integer.parseInt(line, 16);

					if(partSize == 0) {
						break;
					}
					byte[] part = client.recv(partSize);

					parts.add(part);

					client.recv(2); // CR-LF
				}
				for(; ; ) { // chunked-footer
					String line = readLine(client);

					if(line.isEmpty()) {
						break;
					}
				}
				this.resBody = BinTools.join(parts);
			}
			else {
				if(contentLength < 0) {
					throw new Exception("Bad contentLength: " + contentLength);
				}
				this.resBody = client.recv(contentLength);
			}
		}
	}

	private static String readLine(SockClient client) throws Exception {
		try(ByteArrayOutputStream buff = new ByteArrayOutputStream()) {
			for(; ; ) {
				int chr = client.recv(1)[0] & 0xff;

				if(chr == 0x0d) { // CR
					continue;
				}
				if(chr == 0x0a) { // LF
					break;
				}
				buff.write(chr);
			}
			return new String(buff.toByteArray(), StringTools.CHARSET_ASCII);
		}
	}
}
