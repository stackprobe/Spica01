package charlotte.tools;

import java.io.File;

public class SpicaToolkit {
	private static final String SPICATOOLKIT_EXE_FILE = "C:/app/Kit/SpicaToolkit/SpicaToolkit.exe";

	public static Process exec(String args) {
		try {
			if(new File(SPICATOOLKIT_EXE_FILE).exists() == false) {
				throw new RTError();
			}
			return Runtime.getRuntime().exec("\"" + SPICATOOLKIT_EXE_FILE + "\" " + args);
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}
}
