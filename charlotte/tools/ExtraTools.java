package charlotte.tools;

import java.io.File;

public class ExtraTools {
	private static String _semiRemoveDestDir = null;

	public static void semiRemovePath(String path) throws Exception {
		semiRemovePath(path, false);
	}

	public static void semiRemovePath(String path, boolean keepSrcPath) throws Exception {
		if(_semiRemoveDestDir == null) {
			_semiRemoveDestDir = makeFreeDir();
		}
		String destPath = FileTools.combine(_semiRemoveDestDir, FileTools.getFileName(path));
		destPath = toCreatablePath(destPath);

		if(new File(path).isFile()) {
			if(keepSrcPath) {
				FileTools.copyFile(path, destPath);
			}
			else {
				FileTools.moveFile(path, destPath);
			}
		}
		else if(new File(path).isDirectory()) {
			if(keepSrcPath) {
				FileTools.copyDir(path, destPath);
			}
			else {
				FileTools.moveDir(path, destPath);
			}
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
