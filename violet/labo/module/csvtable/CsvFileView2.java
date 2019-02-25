package violet.labo.module.csvtable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import charlotte.tools.CsvFileReader;
import charlotte.tools.FileTools;
import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.StringTools;
import violet.labo.module.csvtable.utils.CsvFileSorter;

public class CsvFileView2 {
	private String _file;
	private String _cacheDir;

	public CsvFileView2(String file) throws Exception {
		if(StringTools.isNullOrEmpty(file)) {
			throw new IllegalArgumentException();
		}
		if(new File(file).isFile() == false) {
			throw new IllegalArgumentException(file);
		}

		_file = file;
		_cacheDir = file + "_cache.dir";

		if(isOldCacheDir()) {
			FileTools.delete(_cacheDir);
			FileTools.createDir(_cacheDir);
		}
		else {
			if(new File(_cacheDir).isDirectory() == false) {
				FileTools.createDir(_cacheDir);
			}
		}
	}

	private boolean isOldCacheDir() {
		File f = new File(_file);
		File d = new File(_cacheDir);

		return d.isDirectory() && d.lastModified() < f.lastModified();
	}

	private String _sortedFile = null;

	public void sort(int colidx, int direction) throws Exception {
		if(colidx < 0 || IntTools.IMAX < colidx || (direction != 1 && direction != -1)) {
			throw new IllegalArgumentException();
		}
		_sortedFile = FileTools.combine(_cacheDir, colidx + "_" + direction);

		if(new File(_sortedFile).isFile() == false) {
			try(CsvFileSorter sorter = new CsvFileSorter(_file, _sortedFile)) {
				sorter.sort((a, b) -> {
					int ret = StringTools.comp.compare(a.get(colidx), b.get(colidx)) * direction;

					if(ret == 0) {
						ret = ListTools.comp(a, b, StringTools.comp);
					}
					return ret;
				});
			}
		}
	}

	public List<String[]> filter(Predicate<List<String>> match, int start, int count) throws Exception {
		if(match == null || start < 0 || IntTools.IMAX < start || count < 1 || IntTools.IMAX < count) {
			throw new IllegalArgumentException();
		}
		if(_sortedFile == null) {
			sort(0, 1);
		}
		List<String[]> dest = new ArrayList<String[]>();

		try(CsvFileReader reader = new CsvFileReader(_sortedFile)) {
			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				if(match.test(row)) {
					if(1 <= start) {
						start--;
					}
					else {
						dest.add(row.toArray(new String[row.size()]));

						if(count <= 1) {
							break;
						}
						count--;
					}
				}
			}
		}
		return dest;
	}
}
