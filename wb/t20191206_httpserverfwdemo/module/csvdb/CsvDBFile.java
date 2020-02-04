package wb.t20191206_httpserverfwdemo.module.csvdb;

import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import charlotte.tools.BinTools;
import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.IArrays;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;

public class CsvDBFile {
	private String _file;

	public CsvDBFile(String file) throws Exception {
		if(StringTools.isNullOrEmpty(file)) {
			throw new IllegalArgumentException(file);
		}
		if(new File(file).isFile() == false) {
			FileTools.writeAllBytes(file, BinTools.EMPTY);
		}
		_file = file;
	}

	public CsvFileView select() throws Exception {
		return new CsvFileView(_file, _file + "$v");
	}

	public void add(String[] row) throws Exception {
		add(IArrays.asList(row));
	}

	public void add(Iterable<String> row) throws Exception {
		try(CsvFileWriter writer = new CsvFileWriter(_file, true)) {
			writer.writeRow(row.iterator());
		}
	}

	public void delete(Predicate<List<String>> match) throws Exception {
		modify(match, row -> row);
	}

	public void update(Function<List<String>, List<String>> modifier) throws Exception {
		modify(row -> false, modifier);
	}

	public void modify(Predicate<List<String>> match, Function<List<String>, List<String>> modifier) throws Exception {
		String wFile = _file + "$w";
		String xFile = _file + "$x";

		if(new File(wFile).exists() || new File(xFile).exists()) {
			throw new RTError("Already exists wFile or xFile.");
		}
		try {
			try(
					CsvFileReader reader = new CsvFileReader(_file);
					CsvFileWriter writer = new CsvFileWriter(wFile);
					) {
				for(; ; ) {
					List<String> row = reader.readRow();

					if(row == null) {
						break;
					}
					if(match.test(row) == false) {
						writer.writeRow(modifier.apply(row));
					}
				}
			}
			FileTools.moveFile(_file, xFile);
			FileTools.moveFile(wFile, _file);
			FileTools.moveFile(xFile, wFile);
		}
		finally {
			FileTools.delete(wFile);
		}
	}
}
