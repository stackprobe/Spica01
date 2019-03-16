package wb.t20190316;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import charlotte.tools.ArrayTools;
import charlotte.tools.CsvFileReader;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.DoubleTools;
import charlotte.tools.FileTools;
import charlotte.tools.HandleDam;
import charlotte.tools.IQueues;
import charlotte.tools.ListTools;
import charlotte.tools.LongTools;
import charlotte.tools.MapTools;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;
import charlotte.tools.VariantTools;
import violet.labo.module.csvtable.utils.CsvFileSorter;

public class Test0001 {
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

	// ----

	private static final String R_DIR = "C:/var2/db-export";
	private static final String MID_DIR = "C:/temp/DBRelChk.tmp";
	private static final String FILES_DOLLAR_FILE = "C:/temp/DBRelChk_files_$";
	private static final String DEST_FILE = "C:/temp/DBRelChk.csv";

	private static final int OUT_MASKED_MAX = 6;

	private static void test01() throws Exception {
		FileTools.delete(MID_DIR);
		FileTools.createDir(MID_DIR);
		FileTools.delete(FILES_DOLLAR_FILE);
		FileTools.delete(DEST_FILE);

		{
			File[] fs = new File(R_DIR).listFiles();
			Arrays.sort(fs, (a, b) -> LongTools.comp.compare(a.length(), b.length())); // ファイルの小さい順
			int testCount = 0; // test
			for(File f : fs) {
				if(100 < ++testCount) { break; } // test
				if(f.isFile()) {
					String file = f.getCanonicalPath();

					System.out.println("file: " + file);

					try(CsvFileReader reader = new CsvFileReader(file)) {
						List<String> columns = reader.readRow();
						List<String> types = reader.readRow();

						String table = FileTools.getFileNameWithoutExtension(file);
						String tableDir = FileTools.combine(MID_DIR, table);

						FileTools.createDir(tableDir);

						List<CsvFileWriter> writers = new ArrayList<CsvFileWriter>();

						HandleDam.section(hDam -> {
							if(columns.size() < 1 || 1000 < columns.size()) {
								throw null; // souteigai !!!
							}
							for(int colidx = 0; colidx < columns.size(); colidx++) {
								String wFile = FileTools.combine(tableDir, String.format("%03d_%s_$", colidx, columns.get(colidx)));

								writers.add(hDam.add(new CsvFileWriter(
										wFile
										)));

								FileTools.writeAllText(wFile + "_t", types.get(colidx), StringTools.CHARSET_SJIS);
							}
							for(; ; ) {
								List<String> row = reader.readRow();

								if(row == null) {
									break;
								}
								for(int colidx = 0; colidx < columns.size(); colidx++) {
									writers.get(colidx).writeRow(new String[] { row.get(colidx) });
								}
							}
						});
					}
				}
			}
		}

		{
			File[] ds = new File(MID_DIR).listFiles();
			Arrays.sort(ds, (a, b) -> StringTools.comp.compare(a.getName(), b.getName()));
			for(File d : ds) {
				File[] fs = d.listFiles();
				fs = ArrayTools.where(fs, f -> f.getName().endsWith("_$")).toArray(new File[0]);
				Arrays.sort(fs, (a, b) -> StringTools.comp.compare(a.getName(), b.getName()));
				for(File f : fs) {
					String file = f.getCanonicalPath();

					System.out.println("file_m: " + file);

					Map<String, int[]> maskedCellCounts = MapTools.<int[]>create();

					maskedCellCounts.put("", new int[] { 0 }); // ""	を項目として必ず入れる。

					try(CsvFileReader reader = new CsvFileReader(file)) {
						for(; ; ) {
							List<String> row = reader.readRow();

							if(row == null) {
								break;
							}
							String cell = row.get(0);
							String maskedCell = maskCell(cell);

							increment(maskedCellCounts, maskedCell);
						}
					}
					String[] maskedCells = maskedCellCounts.keySet().toArray(new String[0]);
					Arrays.sort(maskedCells, (a, b) -> {
						int ret = VariantTools.comp(a, b, v -> v.isEmpty() ? 0 : 1); // ""	優先
						if(ret != 0) {
							return ret;
						}

						ret = maskedCellCounts.get(b)[0] - maskedCellCounts.get(a)[0]; // (b-a)多い順
						if(ret != 0) {
							return ret;
						}

						ret = StringTools.comp.compare(a, b);
						return ret;
					});

					try(Writer writer = FileTools.openWriter(file + "_m", StringTools.CHARSET_SJIS)) {
						for(int index = 0; index < maskedCells.length; index++) {
							writer.write(String.format("%d", maskedCellCounts.get(maskedCells[index])[0]));
							writer.write("\t");
							writer.write(maskedCells[index]);
							writer.write("\n");
						}
					}
				}
			}
		}

		{
			List<String> files = new ArrayList<String>();

			for(File d : new File(MID_DIR).listFiles()) {
				for(File f : d.listFiles()) {
					if(f.getName().endsWith("_$")) {
						files.add(f.getCanonicalPath());
					}
				}
			}
			files.sort(StringTools.comp);

			FileTools.writeAllLines(
					FILES_DOLLAR_FILE,
					ListTools.select(files, file -> FileTools.eraseRoot(file, MID_DIR)),
					StringTools.CHARSET_SJIS
					);

			for(String file : files) {
				System.out.println("file_sort: " + file);

				try(CsvFileSorter sorter = new CsvFileSorter(file)) {
					sorter.sort(_getRates_rowComp);
				}
			}
			for(String file : files) {
				System.out.println("file_r: " + file);

				long t = System.currentTimeMillis();
				try(CsvFileWriter writer = new CsvFileWriter(file + "_r")) {
					for(int ndx = 0; ndx < files.size(); ndx++) {
						double[] rates = getRates(file, files.get(ndx));

						writer.writeRow(new String[] {
								String.format("%.9f", rates[0]),
								String.format("%.9f", rates[1]),
						});
					}
				}
				System.out.println("t: " + (System.currentTimeMillis() - t));
			}
		}

		_tables = new ArrayList<TableInfo>();

		{
			File[] ds = new File(MID_DIR).listFiles();
			Arrays.sort(ds, (a, b) -> StringTools.comp.compare(a.getName(), b.getName()));
			for(File d : ds) {
				TableInfo table = new TableInfo();

				table.name = d.getName();
				table.comment = getTableComment(table.name);
				table.rowcnt = -1;

				File[] fs = d.listFiles();
				fs = ArrayTools.where(fs, f -> f.getName().endsWith("_$")).toArray(new File[0]);
				Arrays.sort(fs, (a, b) -> StringTools.comp.compare(a.getName(), b.getName()));
				for(File f : fs) {
					if(table.rowcnt == -1) {
						try(CsvFileReader reader = new CsvFileReader(f.getCanonicalPath())) {
							table.rowcnt = IQueues.counter(IQueues.wrap(() -> RTError.get(() -> reader.readRow())));
						}
					}

					ColumnInfo column = new ColumnInfo();

					column.parent = table;
					column.name = f.getName().substring(4, f.getName().length() - 2);
					column.type = FileTools.readAllText(f.getCanonicalPath() + "_t", StringTools.CHARSET_SJIS);
					column.comment = getColumnComment(table.name, column.name);

					try(Reader reader = FileTools.openReader(f.getCanonicalPath() + "_m", StringTools.CHARSET_SJIS)) {
						for(; ; ) {
							String line = FileTools.readLine(reader);

							if(line == null) {
								break;
							}
							String[] tokens = line.split("[\t]", 2);

							MaskedInfo masked = new MaskedInfo();

							masked.count = Integer.parseInt(tokens[0]);
							masked.pattern = tokens[1];

							column.maskeds.add(masked);

							if(OUT_MASKED_MAX <= column.maskeds.size()) {
								break;
							}
						}
					}
					try(CsvFileReader reader = new CsvFileReader(f.getCanonicalPath() + "_r")) {
						for(; ; ) {
							List<String> row = reader.readRow();

							if(row == null) {
								break;
							}
							RatePairInfo ratePair = new RatePairInfo();

							ratePair.anotherColumn = null; // 後で、
							ratePair.a = Double.parseDouble(row.get(0));
							ratePair.b = Double.parseDouble(row.get(1));

							column.ratePairs.add(ratePair);
						}
					}
					table.columns.add(column);
				}
				_tables.add(table);
			}
		}

		{
			List<String> files = FileTools.readAllLines(FILES_DOLLAR_FILE, StringTools.CHARSET_SJIS);

			for(TableInfo table : _tables) {
				for(ColumnInfo column : table.columns) {
					for(int index = 0; index < column.ratePairs.size(); index++) {
						String file = files.get(index);
						file = file.replace("\\", "/");
						String[] pTkns = file.split("[/]");
						String tableName = pTkns[0];
						String columnName = pTkns[1].substring(4, pTkns[1].length() - 2);

						ColumnInfo anotherColumn = getColumn(_tables, tableName, columnName);

						column.ratePairs.get(index).anotherColumn = anotherColumn;
					}

					// ----

					column.ratePairs.sort((a, b) -> {
						int ret = DoubleTools.comp.compare(b.a, a.a); // (b-a) .a レート高い順
						if(ret != 0) {
							return ret;
						}

						ret = StringTools.comp.compare(a.anotherColumn.parent.name, b.anotherColumn.parent.name);
						if(ret != 0) {
							return ret;
						}

						ret = StringTools.comp.compare(a.anotherColumn.name,  b.anotherColumn.name);
						return ret;
					});
				}
			}
		}

		try(CsvFileWriter writer = new CsvFileWriter(DEST_FILE)) {
			for(TableInfo table : _tables) {
				writer.writeRow(new String[] { table.name, "", "", table.comment, "" + table.rowcnt });

				for(ColumnInfo column : table.columns) {
					writer.writeCell("");
					writer.writeCell(column.name);
					writer.writeCell(column.type);
					writer.writeCell(column.comment);
					writer.writeCell("");

					// ---- masked ----

					writer.writeCell("" + column.maskeds.get(0).count);

					if(column.maskeds.get(0).pattern.isEmpty() == false) { // test
						throw null; // bugged !!!
					}

					for(int index = 1; index < column.maskeds.size(); index++) {
						MaskedInfo masked = column.maskeds.get(index);

						writer.writeCell("" + masked.count);
						writer.writeCell(masked.pattern);
					}
					for(int index = column.maskeds.size(); index < OUT_MASKED_MAX; index++) {
						writer.writeCell("");
						writer.writeCell("");
					}

					// ---- ratePair ----

					for(int index = 0; index < column.ratePairs.size(); index++) {
						RatePairInfo ratePair = column.ratePairs.get(index);

						if(ratePair.a < 0.95) {
							break;
						}
						writer.writeCell(String.format("%.3f", ratePair.a));
						writer.writeCell(String.format("%.3f", ratePair.b));
						writer.writeCell(ratePair.anotherColumn.parent.name);
						writer.writeCell(ratePair.anotherColumn.name);
					}

					// ----

					writer.endRow();
				}
			}
		}

		_tables = null;
	}

	private static String getTableComment(String name) {
		return "?";
	}

	private static String getColumnComment(String tableName, String columnName) {
		return "?";
	}

	private static ColumnInfo getColumn(List<TableInfo> tables, String tableName, String columnName) {
		for(TableInfo table : tables) {
			if(table.name.equals(tableName)) {
				for(ColumnInfo column : table.columns) {
					if(column.name.equals(columnName)) {
						return column;
					}
				}
			}
		}
		throw null; // bugged !!!
	}

	private static List<TableInfo> _tables;

	private static class TableInfo {
		public String name;
		public String comment;
		public int rowcnt;
		public List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
	}

	private static class ColumnInfo {
		public TableInfo parent;
		public String name;
		public String type;
		public String comment;
		public List<MaskedInfo> maskeds = new ArrayList<MaskedInfo>();
		public List<RatePairInfo> ratePairs = new ArrayList<RatePairInfo>();
	}

	private static class MaskedInfo {
		public int count;
		public String pattern;
	}

	private static class RatePairInfo {
		public ColumnInfo anotherColumn;
		public double a; // この列から相手の列へのヒット率
		public double b; // 相手の列からこの列へのヒット率
	}

	private static double[] getRates(String file, String file2) throws Exception {
		if(file == file2) {
			return new double[] { -2.0, -2.0 };
		}

		Counter only = new Counter();
		Counter only2 = new Counter();
		Counter both = new Counter();

		try(
				CsvFileReader reader = new CsvFileReader(file);
				CsvFileReader reader2 = new CsvFileReader(file2);
				) {
			IQueues.merge(
					IQueues.wrap(() -> RTError.get(() -> reader.readRow())),
					IQueues.wrap(() -> RTError.get(() -> reader2.readRow())),
					IQueues.wrap(row -> only.count++),
					IQueues.wrap(row -> both.count++),
					null,
					IQueues.wrap(row -> only2.count++),
					_getRates_rowComp
					);
		}

		if(both.count == 0) {
			if(only.count == 0 && only2.count == 0) {
				return new double[] { -1.0, -1.0 };
			}
			if(only.count == 0) {
				return new double[] { -1.0, 0.0 };
			}
			if(only2.count == 0) {
				return new double[] { 0.0, -1.0 };
			}
		}

		double rate = both.count * 1.0 / (both.count + only.count);
		double rate2 = both.count * 1.0 / (both.count + only2.count);

		return new double[] { rate, rate2 };
	}

	private static Comparator<List<String>> _getRates_rowComp = (a, b) -> StringTools.comp.compare(a.get(0), b.get(0));

	private static class Counter {
		public int count = 0;
	}

	private static <T> void increment(Map<T, int[]> counters, T key) {
		int[] counter = counters.get(key);

		if(counter == null) {
			counter = new int[] { 0 };
			counters.put(key, counter);
		}
		counter[0]++;
	}

	private static String maskCell(String cell) {
		StringBuffer buff = new StringBuffer();

		for(char chr : cell.toCharArray()) {
			if(30 <= buff.length()) {
				buff.append("(略)");
				break;
			}
			if(chr < 0x0020) {
				chr = 'C';
			}
			else if(chr == 0x0020) {
				chr = 'S';
			}
			else if(0x007e < chr) {
				chr = 'Z';
			}
			else if(StringTools.DECIMAL.indexOf(chr) != -1) {
				chr = 'D';
			}
			else if(StringTools.ALPHA.indexOf(chr) != -1) {
				chr = 'A';
			}
			else if(StringTools.alpha.indexOf(chr) != -1) {
				chr = 'a';
			}
			buff.append(chr);
		}
		return buff.toString();
	}
}
