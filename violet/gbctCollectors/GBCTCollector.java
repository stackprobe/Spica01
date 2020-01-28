package violet.gbctCollectors;

import java.io.File;

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

	private static void main2() {
		perform("C:/temp/Collect");
	}

	private static void perform(String targDir) {
		for(File f : new File(targDir).listFiles()) {
			if(f.isDirectory()) {
				pushDir(f);
			}
			else {
				pushFile(f);
			}
		}
	}

	private static void pushDir(File d) {
		throw null; // TODO
	}

	private static void pushFile(File f) {
		throw null; // TODO
	}
}
