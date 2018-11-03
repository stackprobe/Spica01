package charlotte.tools;

import java.io.File;

public class SpicaToolkit {
	private static final String SPICATOOLKIT_EXE_FILE = "C:/app/Kit/SpicaToolkit/SpicaToolkit.exe";

	public static Process exec(String args) throws Exception {
		if(new File(SPICATOOLKIT_EXE_FILE).exists() == false) {
			throw new RTError("SpicaToolkit.exe is not installed");
		}
		return Runtime.getRuntime().exec("\"" + SPICATOOLKIT_EXE_FILE + "\" " + args);
	}
}
