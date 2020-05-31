package wb.t20200528_Smpl;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.AutoTable;
import charlotte.tools.CsvFileWriter;
import charlotte.tools.IntTools;
import charlotte.tools.StringTools;

public class XTable {
	private List<String> _xNames = new ArrayList<String>();
	private List<String> _yNames = new ArrayList<String>();

	private AutoTable<Long> _table = new AutoTable<Long>(1, 1);

	public void increment(String xName, String yName) {
		int x = find(_xNames, xName);
		int y = find(_yNames, yName);

		Long counter = _table.get(x, y);

		if(counter == null) {
			counter = 1L;
		}
		else {
			counter++;
		}
		_table.set(x, y, counter);
	}

	private int find(List<String> names, String name) {
		int i;

		search: {
			for(i = 0; i < names.size(); i++) {
				if(names.get(i).equals(name)) {
					break search;
				}
			}
			names.add(name);
		}
		return i;
	}

	public void writeToCsvFile(String file) throws Exception {
		int[] xNmIdxs = getSortedOrder(_xNames);
		int[] yNmIdxs = getSortedOrder(_yNames);

		try(CsvFileWriter writer = new CsvFileWriter(file)) {
			writer.writeCell("");

			for(int i = 0; i < _xNames.size(); i++) {
				writer.writeCell(_xNames.get(xNmIdxs[i]));
			}
			writer.endRow();

			for(int y = 0; y < _yNames.size(); y++) {
				writer.writeCell(_yNames.get(yNmIdxs[y]));

				for(int x = 0; x < _xNames.size(); x++) {
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

	private static int[] getSortedOrder(List<String> names) {
		List<Integer> indexList = IntTools.asList(IntTools.sequence(names.size()));
		indexList.sort((a, b) -> StringTools.comp.compare(names.get(a), names.get(b)));
		int[] indexes = IntTools.toArray(indexList);
		return indexes;
	}
}
