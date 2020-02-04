package wb.t20191206_httpserverfwdemo.module.csvdb;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import charlotte.tools.CsvFileReader;
import charlotte.tools.FileTools;
import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;
import charlotte.tools.VariantTools;
import wb.t20191206_httpserverfwdemo.module.csvtable.utils.CsvFileSorter;

public class CsvFileView {
	private String _file;
	private String _cacheDir;

	public CsvFileView(String file, String cacheDir) throws Exception {
		if(StringTools.isNullOrEmpty(file)) {
			throw new IllegalArgumentException(file);
		}
		if(StringTools.isNullOrEmpty(cacheDir)) {
			throw new IllegalArgumentException(cacheDir);
		}
		if(new File(file).isFile() == false) {
			throw new IllegalArgumentException(file);
		}
		if(new File(file).isDirectory() == false) {
			FileTools.createDir(cacheDir);
		}
		_file = file;
		_cacheDir = cacheDir;
	}

	private static int indexOfOldestFile(String file, String file2) {
		File f = new File(file);
		File f2 = new File(file2);

		return f.lastModified() < f2.lastModified() ? 0 : 1;
	}

	private static int toInt(boolean value) {
		return value ? 1 : 0;
	}

	public void view(int sortColumnIndex, int sortDirection, boolean ignoreCase, boolean numericalMode, Predicate<List<String>> match, int offsetRowIndex, int rowCount, Consumer<List<String>> routine) throws Exception {
		if(
				sortColumnIndex < 0 ||
				sortColumnIndex > IntTools.IMAX ||
				(sortDirection != 1 && sortDirection != -1) ||
				//ignoreCase
				//numericalMode
				match == null ||
				offsetRowIndex < 0 ||
				offsetRowIndex > IntTools.IMAX ||
				rowCount < 0 ||
				rowCount > IntTools.IMAX
				) {
			throw new IllegalArgumentException();
		}
		if(rowCount <= 0) {
			return;
		}
		String sortedFile = FileTools.combine(_cacheDir, sortColumnIndex + "_" + ((sortDirection + 1) / 2) + "_" + toInt(ignoreCase) + "_" + toInt(numericalMode));

		if(new File(sortedFile).isFile() == false || indexOfOldestFile(_file, sortedFile) == 1) {
			Comparator<String> compString;
			Comparator<String> comp;

			if(ignoreCase) {
				compString = StringTools.compIgnoreCase;
			}
			else {
				compString = StringTools.comp;
			}
			if(numericalMode) {
				comp = (a, b) -> {
					int ret = VariantTools.comp(a, b, v -> v.startsWith("-") ? -1 : 1);
					if(ret != 0) {
						return ret;
					}

					int sign = a.startsWith("-") ? -1 : 1;

					ret = VariantTools.comp(a, b, v -> {
						int i = v.indexOf('.');

						if(i == -1) {
							i = v.length();
						}
						return i;
					});
					if(ret != 0) {
						return ret * sign;
					}

					ret = compString.compare(a, b);
					return ret * sign;
				};

				// old
				/*
				comp = (a, b) -> {
					int ret = a.length() - b.length();

					if(ret == 0) {
						ret = compString.compare(a, b);
					}
					return ret;
				};
				*/
			}
			else {
				comp = compString;
			}

			try(CsvFileSorter sorter = new CsvFileSorter(_file, sortedFile)) {
				sorter.sort((a, b) -> {
					int ret = comp.compare(a.get(sortColumnIndex), b.get(sortColumnIndex)) * sortDirection;

					if(ret == 0) {
						ret = ListTools.comp(a, b, StringTools.comp);
					}
					return ret;
				});
			}
			catch(Throwable e) {
				FileTools.delete(sortedFile);
				throw RTError.re(e);
			}
		}

		try(CsvFileReader reader = new CsvFileReader(sortedFile)) {
			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				if(match.test(row)) {
					if(1 <= offsetRowIndex) {
						offsetRowIndex--;
					}
					else {
						routine.accept(row);

						if(rowCount <= 1) {
							break;
						}
						rowCount--;
					}
				}
			}
		}
	}
}
