package wb.t20200130_GBCTCollectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

import charlotte.tools.BinTools;
import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import charlotte.tools.SecurityTools;
import charlotte.tools.SockChannel;
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
		//pushAll("C:/a");
		//pushAll("C:/bb");
		//pushAll("C:/ccc");

		// ----

		Runtime.getRuntime().exec("shutdown /t 300 /s");
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
		pushFile(d.getCanonicalPath() + ".clu", writer -> RTError.run(() -> Clusterizer.write(d, writer)));
	}

	private static void pushFile(File f) throws Exception {
		pushFile(f.getCanonicalPath(), writer -> RTError.run(() -> {
			try(InputStream reader = new FileInputStream(f)) {
				FileTools.readToEnd(reader, writer, new byte[64000]);
			}
		}
		));
	}

	private static PumpBinBuffer _recvBuff = null;
	private static PumpBinBuffer _chunkPartBuff = null;

	private static void pushFile(String path, Consumer<FileTools.IWriter> execWrite) throws Exception {
		Ground.connections.set(new Connection());
		_recvBuff = new PumpBinBuffer();
		_chunkPartBuff = new PumpBinBuffer();

		byte[] boundary = SecurityTools.makePassword().getBytes(StringTools.CHARSET_ASCII);

		pump("UPub\r\nPOST / HTTP/1.1\r\nTransfer-Encoding: chunked\r\n\r\n".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart("--".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart(boundary);
		pumpChunkPart("\r\nContent-Disposition: form-data; name=\"upload\"\r\n\r\nupload\r\n--".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart(boundary);
		pumpChunkPart("\r\nContent-Disposition: form-data; name=\"file\" filename=\"".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart(path.getBytes(StringTools.CHARSET_SJIS));
		pumpChunkPart("\"\r\n\r\n".getBytes(StringTools.CHARSET_ASCII));

		execWrite.accept((buff, offset, length) -> pumpChunkPart(BinTools.getSubBytes(buff, offset, offset + length)));

		pumpChunkPart("\r\n--".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPart(boundary);
		pumpChunkPart("--\r\n".getBytes(StringTools.CHARSET_ASCII));
		pumpChunkPartFlush();

		pump("0\r\n\r\n".getBytes(StringTools.CHARSET_ASCII)); // final chunk + chunked trailer part (empty) + CR-LF

		{
			int millis = 0;

			while(isResponseRecved() == false) {
				pump(BinTools.EMPTY);

				if(millis < 2000) {
					millis += 100;
				}
				Thread.sleep(millis);
			}
		}

		Ground.connections.get().disconnect = true;

		try {
			pump(BinTools.EMPTY); // Will disconnect
		}
		catch(Throwable e) {
			// noop
		}

		Ground.connections.set(null);
		_recvBuff = null;
		_chunkPartBuff = null;
	}

	private static void pumpChunkPart(byte[] data) throws Exception {
		_chunkPartBuff.write(data);

		if(10000 < _chunkPartBuff.size()) {
			pumpChunkPartFlush();
		}
	}

	private static void pumpChunkPartFlush() throws Exception {
		if(_chunkPartBuff.size() == 0) {
			return;
		}
		byte[] data = _chunkPartBuff.getBuffer();

		_chunkPartBuff.clear();

		pump(BinTools.join(
				new byte[][] {
					Integer.toHexString(data.length).getBytes(StringTools.CHARSET_ASCII),
					new byte[] { 0x0d, 0x0a }, // CR-LF
					data,
					new byte[] { 0x0d, 0x0a }, // CR-LF
				}
				));
	}

	private static void pump(byte[] data) throws Exception {
		//System.out.println("* " + data.length + ", " + _recvBuff.size()); // test
		SockChannel.critical.section_a(() -> _recvBuff.write(CipherPump.pump(data)));
	}

	private static boolean isResponseRecved() {
		// FIXME
		return Utils.contains(_recvBuff.getBuffer(), new byte[] { 0x0d, 0x0a, 0x0d, 0x0a }); // CR-LF-CR-LF
	}
}
