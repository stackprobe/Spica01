package wb.t20191206_httpserverfwdemo.module.csvtable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import charlotte.tools.BinTools;
import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.IQueue;
import charlotte.tools.IQueues;
import charlotte.tools.QueueUnit;
import charlotte.tools.RTError;
import charlotte.tools.WorkingDir;

public class CsvFileView implements AutoCloseable {
	private String _file;
	private WorkingDir _wd;
	private String _filteredFile;
	private String _sortedFile;
	private String _rangedFile;

	public CsvFileView(String file) throws Exception {
		_file = file;
		_wd = new WorkingDir();
		_filteredFile = _wd.makePath();
		_sortedFile = _wd.makePath();
		_rangedFile = _wd.makePath();
	}

	public void filter(Predicate<List<String>> match) throws Exception {
		try(
				CsvFileReader reader = new CsvFileReader(_file);
				CsvFileWriter writer = new CsvFileWriter(_filteredFile);
				) {
			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				if(match.test(row)) {
					writer.writeRow(row);
				}
			}
		}
	}

	public void sort(Comparator<String[]> comp) throws Exception {
		QueueUnit<String> partFiles = new QueueUnit<String>();

		try(CsvFileReader reader = new CsvFileReader(_filteredFile)) {
			List<String[]> rows = new ArrayList<String[]>();
			int loadedChrCount = 0;

			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				rows.add(row.toArray(new String[row.size()]));

				for(String cell : row) {
					loadedChrCount += cell.length();
					loadedChrCount += 10;
				}
				loadedChrCount += 10;

				if(50000000 < loadedChrCount) { // ? 50 M char <
					writeToPartFile(rows, comp, partFiles);
					rows.clear();
					loadedChrCount = 0;
				}
			}
			if(1 <= rows.size()) {
				writeToPartFile(rows, comp, partFiles);
			}
		}

		System.out.println("partFiles.size() == " + partFiles.size()); // test

		if(partFiles.size() == 0) {
			FileTools.writeAllBytes(_sortedFile, BinTools.EMPTY);
		}
		else if(partFiles.size() == 1) {
			FileTools.delete(_sortedFile);
			FileTools.moveFile(partFiles.dequeue(), _sortedFile);
		}
		else {
			while(2 <= partFiles.size()) {
				String partFile = partFiles.dequeue();
				String partFile2 = partFiles.dequeue();
				String partFileNew;

				if(partFiles.size() == 0) {
					partFileNew = _sortedFile;
				}
				else {
					partFileNew = _wd.makePath();
					partFiles.enqueue(partFileNew);
				}
				mergePartFile(partFile, partFile2, partFileNew, comp);

				FileTools.delete(partFile);
				FileTools.delete(partFile2);
			}
		}
	}

	private void writeToPartFile(List<String[]> rows, Comparator<String[]> comp, IQueue<String> partFiles) throws Exception {
		rows.sort(comp);

		String partFile = _wd.makePath();

		try(CsvFileWriter writer = new CsvFileWriter(partFile)) {
			writer.writeRows(rows);
		}
		partFiles.enqueue(partFile);
	}

	private void mergePartFile(String partFile, String partFile2, String partFileNew, Comparator<String[]> comp) throws Exception {
		try(
				CsvFileReader reader = new CsvFileReader(partFile);
				CsvFileReader reader2 = new CsvFileReader(partFile2);
				CsvFileWriter writer = new CsvFileWriter(partFileNew);
				) {
			List<String> row = reader.readRow();
			List<String> row2 = reader2.readRow();

			while(row != null && row2 != null) {
				int ret = comp.compare(
						row.toArray(new String[row.size()]),
						row2.toArray(new String[row2.size()])
						);

				if(ret <= 0) {
					writer.writeRow(row);
					row = reader.readRow();
				}
				if(0 <= ret) {
					writer.writeRow(row2);
					row2 = reader2.readRow();
				}
			}
			while(row != null) {
				writer.writeRow(row);
				row = reader.readRow();
			}
			while(row2 != null) {
				writer.writeRow(row2);
				row2 = reader2.readRow();
			}
		}
	}

	public void range(int start, int count) throws Exception {
		try(
				CsvFileReader reader = new CsvFileReader(_sortedFile);
				CsvFileWriter writer = new CsvFileWriter(_rangedFile);
				) {
			for(int index = 0; index < start; index++) {
				List<String> row = reader.readRow();

				if(row == null) {
					return;
				}
				// skip row
			}
			for(int index = 0; index < count; index++) {
				List<String> row = reader.readRow();

				if(row == null) {
					return;
				}
				writer.writeRow(row);
			}
		}
	}

	public List<String[]> get() throws Exception {
		try(CsvFileReader reader = new CsvFileReader(_rangedFile)) {
			return reader.readToEnd();
		}
	}

	public int size() throws Exception {
		try(CsvFileReader reader = new CsvFileReader(_file)) {
			return IQueues.counter(IQueues.wrap(() -> RTError.get(() -> reader.readRow())));
		}
	}

	@Override
	public void close() throws Exception {
		if(_wd != null) {
			_wd.close();
			_wd = null;
		}
	}
}
