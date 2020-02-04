package wb.t20200130_GBCTCollectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import wb.t20200125_GBCTunnels.Connection;
import wb.t20200125_GBCTunnels.GBCTunnel;
import wb.t20200125_GBCTunnels.Ground;
import wb.t20200125_GBCTunnels.pumps.CipherPump;
import wb.t20200125_GBCTunnels.utils.PumpBinBuffer;
import wb.t20200130_GBCTCollectors.utils.Clusterizer;

public class GBCTCollector {
	public static void main(String[] args) {
		try {
			main2();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void main2() throws Exception {
		GBCTunnel.init();

		// ----

		pushAll("C:/temp/Collect");

		// ----
	}

	private static void pushAll(String targDir) throws Exception {
		for(File f : new File(targDir).listFiles()) {
			if(f.isDirectory()) {
				pushDir(f);
			}
			else {
				pushFile(f);
			}
		}
	}

	private static void pushDir(File d) throws Exception {
		pushFile(d, writer -> RTError.run(() -> Clusterizer.write(d, writer)));
	}

	private static void pushFile(File f) throws Exception {
		pushFile(f, writer -> RTError.run(() -> {
			try(InputStream reader = new FileInputStream(f)) {
				FileTools.readToEnd(reader, writer, new byte[64000]);
			}
		}
		));
	}

	private static PumpBinBuffer _recvBuff = null;

	private static void pushFile(File f, Consumer<FileTools.IWriter> execWrite) throws Exception {
		Ground.connections.set(new Connection());
		_recvBuff = new PumpBinBuffer();

		byte[] boundary = SecurityTools.makePassword().getBytes(StringTools.CHARSET_ASCII);

		pump("POST / HTTP/1.1\r\nTransfer-Encoding: chunked\r\n\r\n".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart("--".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart(boundary);
		pumpChunkPart("\r\nContent-Disposition: form-data; name=\"upload\"\r\n\r\nupload\r\n--".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart(boundary);
		pumpChunkPart("\r\nContent-Disposition: form-data; name=\"file\" filename=\"".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart(f.getCanonicalPath().getBytes(StringTools.CHARSET_SJIS));
		pumpChunkPart("\"\r\n\r\n".getBytes(StringTools.CHARSET_ASCII));

		execWrite.accept((buff, offset, length) -> pumpChunkPart(BinTools.getSubBytes(buff, offset, offset + length)));

		pumpChunkPart("\r\n--".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart(boundary);
		pumpChunkPart("--\r\n".getBytes(StringTools.CHARSET_ASCII));

		pump("0\r\n\r\n".getBytes(StringTools.CHARSET_ASCII)); // final chunk + chunked trailer part

		while(isResponseRecved() == false) {
			pump(BinTools.EMPTY);
		}

		Ground.connections.get().disconnect = true;

		try {
			pump(BinTools.EMPTY);
		}
		catch(Throwable e) {
			// noop
		}

		Ground.connections.set(null);
		_recvBuff = null;
	}

	private static void pumpChunkPart(byte[] data) throws Exception {
		if(data.length == 0) {
			return;
		}

		pump(BinTools.join(
				new byte[][] {
					String.format("%x", data.length).getBytes(StringTools.CHARSET_ASCII),
					new byte[] { 0x0d, 0x0a }, // CR-LF
					data,
					new byte[] { 0x0d, 0x0a }, // CR-LF
				}
				));
	}

	private static void pump(byte[] data) throws Exception {
		_recvBuff.write(CipherPump.pump(data));
	}

	private static boolean isResponseRecved() {
		// FIXME
		return Utils.contains(_recvBuff.getBuffer(), new byte[] { 0x0d, 0x0a, 0x0d, 0x0a }); // CR-LF-CR-LF
	}
}
