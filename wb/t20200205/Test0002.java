package wb.t20200205;

import java.io.File;

import charlotte.tools.CharTools;
import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;

public class Test0002 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		search("C:/pleiades/workspace/Spica01/src");
	}

	private static void search(String targDir) throws Exception {
		for(File f : new File(targDir).listFiles()) {
			if(f.isDirectory()) {
				search(f.getCanonicalPath());
			}
			else {
				foundFile(f.getCanonicalPath());
			}
		}
	}

	private static void foundFile(String file) throws Exception {
		if(StringTools.compIgnoreCase.compare(FileTools.getExtension(file), ".java") == 0) {
			String text = FileTools.readAllText(file, StringTools.CHARSET_UTF8);

			if(ListTools.any(CharTools.asList(text.toCharArray()), chr -> 0x7f < chr)) { // ? has non ascii
				System.out.println(file);
			}
		}
	}
}
