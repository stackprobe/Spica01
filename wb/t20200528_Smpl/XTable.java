package wb.t20200528_Smpl;

import java.util.List;
import java.util.Map;

import charlotte.tools.ArrayTools;
import charlotte.tools.AutoTable;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.MapTools;
import charlotte.tools.StringTools;

public class XTable {
	private Map<String, Integer> _xName2idx = MapTools.<Integer>create();
	private Map<String, Integer> _yName2idx = MapTools.<Integer>create();

	private AutoTable<Long> _table = new AutoTable<Long>(1, 1);

	public void increment(String xName, String yName) {
		int x = find(_xName2idx, xName);
		int y = find(_yName2idx, yName);

		Long counter = _table.get(x, y);

		if(counter == null) {
			counter = 1L;
		}
		else {
			counter++;
		}
		_table.set(x, y, counter);
	}

	private int find(Map<String, Integer> names, String name) {
		Integer index = names.get(name);

		if(index == null) {
			index = names.size();
			names.put(name, index);
		}
		return index.intValue();
	}

	public void writeToCsvFile(String file) throws Exception {
		String[] xNames = getSortedNames(_xName2idx);
		String[] yNames = getSortedNames(_yName2idx);

		final int[] xNmIdxs = getNameIndexes(xNames, _xName2idx);
		final int[] yNmIdxs = getNameIndexes(yNames, _yName2idx);

		try(CsvFileWriter writer = new CsvFileWriter(file)) {
			writer.writeCell("");

			for(int i = 0; i < xNames.length; i++) {
				writer.writeCell(xNames[i]);
			}
			writer.endRow();

			for(int y = 0; y < yNames.length; y++) {
				writer.writeCell(yNames[y]);

				for(int x = 0; x < _xName2idx.size(); x++) {
					Long counter = _table.get(xNmIdxs[x], yNmIdxs[y]);

					if(counter == null) {
						counter = 0L;
					}
					writer.writeCell(counter.toString());
				}
				writer.endRow();
			}
		}
	}

	private static String[] getSortedNames(Map<String, Integer> name2idx) {
		List<String> names = ListTools.toList(name2idx.keySet());
		names.sort(StringTools.comp);
		return names.toArray(new String[names.size()]);
	}

	private int[] getNameIndexes(String[] names, Map<String, Integer> name2idx) {
		List<Integer> indexes = ArrayTools.select(names, name -> name2idx.get(name));
		return IntTools.toArray(indexes);
	}
}
