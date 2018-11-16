package wb.t20181115;

public class EraCalendar {
	private static EraCalendar _i = null;

	public static EraCalendar i() {
		if(_i == null) {
			_i = new EraCalendar();
		}
		return _i;
	}

	public static class Era {
		public String name;
		public int firstDate;

		public Era(String name, int firstDate) {
			this.name = name;
			this.firstDate = firstDate;
		}
	}

	private Era[] _eras;

	private EraCalendar() {
		_eras = new Era[] {
				new Era("明治", 18680125),
				new Era("大正", 19120730),
				new Era("昭和", 19261225),
				new Era("平成", 19890108),
				//new Era("新元号", 20190501),
		};
	}

	public static class EraDate {
		public Era era;
		public int y;
		public int m;
		public int d;

		public int toDate() {
			return (era.firstDate / 10000 + y - 1) * 10000 + m * 100 + d;
		}

		@Override
		public String toString() {
			return toString("GY年M月D日");
		}

		public String toString(String format) {
			String nen;

			if(y == 1) {
				nen = "元";
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
				throw new Exception("不正な日付です。");
			}
		}
	}

	public EraDate getEraDate(int date) throws Exception {
		EraDate eraDate = new EraDate();

		eraDate.era = getEra(date);
		eraDate.y = date / 10000 - eraDate.era.firstDate / 10000 + 1;
		eraDate.m = (date / 100) % 100;
		eraDate.d = date % 100;

		return eraDate;
	}

	public Era getEra(int date) throws Exception {
		if(date < _eras[0].firstDate) {
			throw new Exception(String.format("%sより前の日付は扱えません。", _eras[0].name));
		}
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
		eraDate.y = nen.equals("元") ? 1 : Integer.parseInt(nen);
		eraDate.m = m;
		eraDate.d = d;

		return eraDate;
	}

	public Era getEra(String name) {
		for(int index = _eras.length - 1; 0 <= index; index--) {
			if(_eras[index].name.equals(name)) {
				return _eras[index];
			}
		}
		return null;
	}
}
