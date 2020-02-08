package wb.t20200207_JavaSrcJpToUxxx;

import java.io.File;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class JavaSrcJpToUxxx {
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
			StringBuffer buff = new StringBuffer();

			for(int chr : text.toCharArray()) {
				if(("\t\r\n " + StringTools.ASCII).indexOf(chr) == -1) {
					buff.append(String.format("\\u%04x", chr));
				}
				else {
					buff.append((char)chr);
				}
			}
			FileTools.writeAllText(file, buff.toString(), StringTools.CHARSET_UTF8);
		}
	}
}
