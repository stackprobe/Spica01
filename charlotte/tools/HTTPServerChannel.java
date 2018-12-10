package charlotte.tools;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HTTPServerChannel {
	private SockChannel _channel;

	public HTTPServerChannel(SockChannel channel) {
		_channel = channel;
	}

	public void recvRequest() throws Exception {
		_channel.recvTimeoutMillis = 2000;

		firstLine = recvLine();

		{
			String[] tokens = firstLine.split("[ ]", -1);

			method = tokens[0];
			path = decodeURL(tokens[1]);
			httpVersion = tokens[2];
		}

		_channel.recvTimeoutMillis = 180000; // 3 min

		recvHeader();
		checkHeader();

		throw null; // TODO
	}

	private String decodeURL(String path) throws Exception {
		byte[] src = path.getBytes(StringTools.CHARSET_ASCII);

		try(ByteArrayOutputStream dest = new ByteArrayOutputStream()) {
			for(int index = 0; index < src.length; index++) {
				if(src[index] == 0x25) { // ? '%'
					dest.write((byte)Integer.parseInt(new String(BinTools.getSubBytes(src, index + 1, index + 3), StringTools.CHARSET_ASCII), 16));
					index += 2;
				}
				else {
					dest.write(src[index]);
				}
			}
			return dest.toString(StringTools.CHARSET_UTF8);
		}
	}

	public String firstLine;
	public String method;
	public String path;
	public String httpVersion;
	public String schema;
	public List<String[]> headerPairs = new ArrayList<String[]>();
	public byte[] body;

	private String recvLine() throws Exception {
		try(ByteArrayOutputStream buff = new ByteArrayOutputStream()) {
			for(; ; ) {
				byte chr = _channel.recv(1)[0];

				if(chr == 0x0d) { // CR
					continue;
				}
				if(chr == 0x0a) { // LF
					break;
				}
				if(512000 < buff.size()) {
					throw new Exception("Overflow");
				}
				buff.write(chr & 0xff);
			}
			return buff.toString(StringTools.CHARSET_ASCII);
		}
	}

	private void recvHeader() throws Exception {
		int headerRoughLength = 0;

		for(; ; ) {
			String line = recvLine();

			if(line.isEmpty()) {
				break;
			}
			headerRoughLength += line.length() + 10;

			if(512000 < headerRoughLength) {
				throw new Exception("Overflow");
			}
			if((line.charAt(0) & 0xffff) <= 0x20) {
				headerPairs.get(headerPairs.size() - 1)[1] += " " + line.trim();
			}
			else {
				int colon = line.indexOf(':');

				headerPairs.add(new String[] {
						line.substring(0, colon).trim(),
						line.substring(colon + 1).trim(),
						});
			}
		}
	}

	public int contentLength = 0;
	public boolean chunked = false;
	public String contentType = null;
	public boolean expect100Continue = false;

	private void checkHeader() {
		for(String[] pair : headerPairs) {
			String key = pair[0];
			String value = pair[1];

			if(key.equalsIgnoreCase("Content-Length")) {
				contentLength = Integer.parseInt(value);
			}
			else if(key.equalsIgnoreCase("Transfer-Encoding") && value.equalsIgnoreCase("chunked")) {
				chunked = true;
			}
			else if(key.equalsIgnoreCase("Content-Type")) {
				contentType = value;
			}
			else if(key.equalsIgnoreCase("Expect") && value.equalsIgnoreCase("100-continue")) {
				expect100Continue = true;
			}
		}
	}

	public void sendResponse() {
		throw null; // TODO
	}
}
