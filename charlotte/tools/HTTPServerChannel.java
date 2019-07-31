package charlotte.tools;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HTTPServerChannel {
	private SockChannel _channel;

	public HTTPServerChannel(SockChannel channel) {
		_channel = channel;
	}

	public HandleDam hDam;

	public static int requestTimeoutMillis = -1; // -1 == INFINITE
	public static int responseTimeoutMillis = -1; // -1 == INFINITE
	public static int firstLineTimeoutMillis = 2000; // -1 == INFINITE
	public static int idleTimeoutMillis = 180000; // 3 min // -1 == INFINITE

	public void recvRequest() throws Exception {
		_channel.sessionTimeoutTime = timeoutMillisToTime(requestTimeoutMillis);
		_channel.idleTimeoutMillis = firstLineTimeoutMillis;

		firstLine = recvLine();

		{
			String[] tokens = firstLine.split("[ ]");

			method = tokens[0];
			path = decodeURL(tokens[1]);
			httpVersion = tokens[2];
		}

		_channel.idleTimeoutMillis = idleTimeoutMillis;

		recvHeader();
		checkHeader();

		if(expect100Continue) {
			sendLine("HTTP/1.1 100 Continue");
			sendLine("");
		}
		recvBody();
	}

	private static long timeoutMillisToTime(int millis) {
		if(millis == -1) {
			return -1L;
		}
		return System.currentTimeMillis() + (long)millis;
	}

	private String decodeURL(String path) throws Exception {
		byte[] src = path.getBytes(StringTools.CHARSET_ASCII);

		try(ByteArrayOutputStream dest = new ByteArrayOutputStream()) {
			for(int index = 0; index < src.length; index++) {
				if(src[index] == 0x25) { // ? '%'
					dest.write((byte)Integer.parseInt(new String(BinTools.getSubBytes(src, index + 1, index + 3), StringTools.CHARSET_ASCII), 16));
					index += 2;
				}
				else if(src[index] == 0x2b) { // ? '+'
					dest.write(0x20); // ' '
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

	private static final byte CR = 0x0d;
	private static final byte LF = 0x0a;

	private String recvLine() throws Exception {
		try(ByteArrayOutputStream buff = new ByteArrayOutputStream()) {
			for(; ; ) {
				byte chr = _channel.recv(1)[0];

				if(chr == CR) {
					continue;
				}
				if(chr == LF) {
					break;
				}
				if(512000 < buff.size()) {
					throw new RTError("Overflow");
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
				throw new RTError("Overflow");
			}
			if(line.charAt(0) <= ' ') {
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

	public static int bodySizeMax = 300000000; // 300 MB
	public static int buffSize = 3000000; // 3 MB

	private void recvBody() throws Exception {
		try(HTTPBodyOutputStream buff = new HTTPBodyOutputStream()) {
			if(chunked) {
				for(; ; ) {
					String line = recvLine();

					// erase chunk-extension
					{
						int i = line.indexOf(';');

						if(i != -1) {
							line = line.substring(0, i);
						}
					}

					int size = Integer.parseInt(line.trim(), 16);

					if(size == 0) {
						break;
					}
					if(size < 0) {
						throw new RTError("size: " + size);
					}
					if(bodySizeMax - buff.size() < size) {
						throw new RTError("buff.size(), size: " + buff.size() + ", " + size);
					}
					int chunkEnd = buff.size() + size;

					while(buff.size() < chunkEnd) {
						buff.write(_channel.recv(Math.min(buffSize, chunkEnd - buff.size())));
					}
					_channel.recv(CRLF.length);
				}
				while(recvLine().isEmpty() == false) { // RFC 7230 4.1.2 Chunked Trailer Part
					// noop
				}
			}
			else {
				if(contentLength < 0) {
					throw new RTError("contentLength: " + contentLength);
				}
				if(bodySizeMax < contentLength) {
					throw new RTError("contentLength, bodySizeMax: " + contentLength + ", " + bodySizeMax);
				}
				while(buff.size() < contentLength) {
					buff.write(_channel.recv(Math.min(buffSize, contentLength - buff.size())));
				}
			}
			body = buff.toByteArray();
		}
	}

	public void sendResponse() throws Exception {
		body = null;
		_channel.sessionTimeoutTime = timeoutMillisToTime(responseTimeoutMillis);

		sendLine("HTTP/1.1 " + resStatus + " Chocolate Cake");

		if(resServer != null) {
			sendLine("Server: " + resServer);
		}
		if(resContentType != null) {
			sendLine("Content-Type: " + resContentType);
		}
		for(String[] pair : resHeaderPairs) {
			sendLine(pair[0] + ": " + pair[1]);
		}
		if(resBody == null) {
			endHeader();
		}
		else {
			Iterator<byte[]> resBodyIte = resBody.iterator();

			if(resBodyIte.hasNext()) {
				byte[] first = resBodyIte.next();

				if(resBodyIte.hasNext()) {
					sendLine("Transfer-Encoding: chunked");
					endHeader();
					sendChunk(first);

					do {
						sendChunk(resBodyIte.next());
					}
					while(resBodyIte.hasNext());

					sendLine("0");
					_channel.send(CRLF);
				}
				else {
					sendLine("Content-Length: " + first.length);
					endHeader();
					_channel.send(first);
				}
			}
			else {
				sendLine("Content-Length: 0");
				endHeader();
			}
		}
	}

	private void endHeader() throws Exception {
		sendLine("Connection: close");
		_channel.send(CRLF);
	}

	private void sendChunk(byte[] chunk) throws Exception {
		if(1 <= chunk.length) {
			sendLine(String.format("%x", chunk.length));
			_channel.send(chunk);
			_channel.send(CRLF);
		}
	}

	public int resStatus = 200;
	public String resServer = null;
	public String resContentType = null;
	public List<String[]> resHeaderPairs = new ArrayList<String[]>();
	public Iterable<byte[]> resBody = null;

	private static final byte[] CRLF = new byte[] { CR, LF };

	private void sendLine(String line) throws Exception {
		_channel.send(line.getBytes(StringTools.CHARSET_ASCII));
		_channel.send(CRLF);
	}
}
