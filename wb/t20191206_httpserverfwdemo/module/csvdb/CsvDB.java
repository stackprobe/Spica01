package wb.t20191206_httpserverfwdemo.module.csvdb;

import java.io.File;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.IArrays;
import charlotte.tools.ListTools;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;

public class CsvDB {
	private String _dir;

	public CsvDB(String dir) throws Exception {
		if(StringTools.isNullOrEmpty(dir)) {
			throw new IllegalArgumentException(dir);
		}
		if(new File(dir).isDirectory() == false) {
			FileTools.createDir(dir);
		}
		_dir = dir;
	}

	public CsvDBFile getFile(String name) throws Exception {
		if(StringTools.isNullOrEmpty(name)) {
			throw new IllegalArgumentException(name);
		}
		return new CsvDBFile(FileTools.combine(_dir, name));
	}

	public List<String> getNames() {
		return IArrays.asList(new File(_dir).list());
	}

	public List<CsvDBFile> getFiles() {
		return getFiles(getNames());
	}

	public List<CsvDBFile> getFiles(List<String> names) {
		return ListTools.select(names, name -> RTError.get(() -> getFile(name)));
	}
}
