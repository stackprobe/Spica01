package charlotte.tools;

import java.io.File;

public class ExtraTools {
	private static String _easyBkDestDir = null;

	public static void easyBackupPath(String path) throws Exception {
		if(_easyBkDestDir == null) {
			_easyBkDestDir = makeFreeDir();
		}
		String destPath = FileTools.combine(_easyBkDestDir, FileTools.getFileName(path));
		destPath = toCreatablePath(destPath);

		if(new File(path).isFile()) {
			FileTools.copyFile(path, destPath);
		}
		else if(new File(path).isDirectory()) {
			FileTools.copyDir(path, destPath);
		}
	}

	public static String makeFreeDir() {
		for(int c = 1; ; c++) {
			String dir = "C:/" + c;

			if(new File(dir).exists() == false) {
				FileTools.createDir(dir);
				return dir;
			}
		}
	}

	public static String toCreatablePath(String path) {
		if(new File(path).exists()) {
			int extidx = FileTools.indexOfExtension(path);

			String prefix = path.substring(0, extidx);
			String suffix = path.substring(extidx);

			for(int c = 2; ; c++) {
				path = prefix + "~" + c + suffix;

				if(new File(path).exists() == false) {
					break;
				}
			}
		}
		return path;
	}
}
