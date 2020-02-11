package wb.t20181115;

import java.util.Map;

import charlotte.tools.FileTools;
import charlotte.tools.JsonTools;
import charlotte.tools.MapTools;
import charlotte.tools.ObjectList;
import charlotte.tools.RTError;

public class EraCalendar {
	private static EraCalendar _i = null;

	public static EraCalendar i() throws Exception {
		if(_i == null) {
			_i = new EraCalendar();
		}
		return _i;
	}

	public static class Era {
		public int firstDate;
		public String name;

		public Era(int firstDate, String name) {
			this.firstDate = firstDate;
			this.name = name;
		}
	}

	private Era[] _eras;
	private Map<String, Era> _name2Era;

	private EraCalendar() throws Exception {
		ObjectList root = (ObjectList)JsonTools.decode(FileTools.readToEnd(this.getClass().getResource("res/Era.json")));

		_eras = new Era[root.size()];

		for(int index = 0; index < root.size(); index++) {
			ObjectList ol = (ObjectList)root.get(index);

			_eras[index] = new Era(
					Integer.parseInt(((JsonTools.Word)ol.get(0)).value),
					(String)ol.get(1)
					);
		}
		_name2Era = MapTools.<Era>create();

		for(Era era : _eras) {
			_name2Era.put(era.name, era);
		}
	}

	private static final String J_GAN = "元"; // orig: private static final String J_GAN = "\u5143";

	public static class EraDate {
		public Era era;
		public int y;
		public int m;
		public int d;

		public int toDate() {
			return (era.firstDate / 10000 + y - 1) * 10000 + m * 100 + d;
		}

		private static final String J_NEN = "年"; // orig: private static final String J_NEN = "\u5e74";
		private static final String J_TSUKI = "月"; // orig: private static final String J_TSUKI = "\u6708";
		private static final String J_NICHI = "日"; // orig: private static final String J_NICHI = "\u65e5";

		@Override
		public String toString() {
			return toString("GY" + J_NEN + "M" + J_TSUKI + "D" + J_NICHI);
		}

		public String toString(String format) {
			String nen;

			if(y == 1) {
				nen = J_GAN;
			}
			else {
				nen = String.format("%02d", y);
			}
			String ret = format;

			ret = ret.replace("D", String.format("%02d", d));
			ret = ret.replace("M", String.format("%02d", m));
			ret = ret.replace("Y", nen);
			ret = ret.replace("G", era.name);

			return ret;
		}

		public void validator() throws Exception {
			if(toString().equals(EraCalendar.i().getEraDate(toDate()).toString()) == false) {
				throw new Exception("Bad date");
			}
		}
	}

	public EraDate getEraDate(int date) {
		EraDate eraDate = new EraDate();

		eraDate.era = getEra(date);
		eraDate.y = date / 10000 - eraDate.era.firstDate / 10000 + 1;
		eraDate.m = (date / 100) % 100;
		eraDate.d = date % 100;

		return eraDate;
	}

	public Era getEra(int date) {
		int l = 0;
		int r = _eras.length;

		while(l + 1 < r) {
			int m = (l + r) / 2;

			if(date < _eras[m].firstDate) {
				r = m;
			}
			else {
				l = m;
			}
		}
		return _eras[l];
	}

	public EraDate getEraDate(String name, String nen, int m, int d) {
		EraDate eraDate = new EraDate();

		eraDate.era = getEra(name);
		eraDate.y = nen.equals(J_GAN) ? 1 : Integer.parseInt(nen);
		eraDate.m = m;
		eraDate.d = d;

		return eraDate;
	}

	public Era getEra(String name) {
		Era era = _name2Era.get(name);

		if(era == null) {
			throw new RTError("Bad era name: " + name);
		}
		return era;
	}
}
