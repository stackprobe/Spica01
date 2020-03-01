package wb.t20200229_CompImagesDir;

import charlotte.tools.WorkingDir;

public class Ground {
	public static WorkingDir wd;

	public static void init() throws Exception {
		wd = new WorkingDir();
	}

	public static void fnlz() throws Exception {
		wd.close();
		wd = null;
	}
}
