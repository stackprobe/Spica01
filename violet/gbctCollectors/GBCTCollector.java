package violet.gbctCollectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import violet.gbcTunnels.Connection;
import violet.gbcTunnels.Ground;
import violet.gbcTunnels.pumps.CipherPump;
import violet.gbcTunnels.utils.PumpBinBuffer;
import violet.gbctCollectors.utils.Clusterizer;

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
		pushAll("C:/temp/Collect");
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
		pushFile(writer -> Clusterizer.write(d, writer));
	}

	private static void pushFile(File f) throws Exception {
		pushFile(writer -> RTError.run(() -> {
			try(InputStream reader = new FileInputStream(f)) {
				FileTools.readToEnd(reader, writer);
			}
		}
		));
	}

	private static PumpBinBuffer _recvBuff = null;

	private static void pushFile(Consumer<FileTools.IWriter> setWriter) throws Exception {
		Ground.connections.set(new Connection());
		_recvBuff = new PumpBinBuffer();

		// TODO
		setWriter.accept((buff, offset, length) -> {
			// TODO
		});
		// TODO

		Ground.connections.set(null);
		_recvBuff = null;
	}

	private static void pump(byte[] data) throws Exception {
		_recvBuff.write(CipherPump.pump(data));
	}
}
