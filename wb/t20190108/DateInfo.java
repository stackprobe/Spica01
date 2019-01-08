package wb.t20190108;

import java.util.List;

import charlotte.tools.DateToDay;
import charlotte.tools.StringTools;

public class DateInfo {
	private DateTimeInfo _inner;

	public static DateInfo now() {
		return fromDate(DateToDay.Now.getDate());
	}

	public static DateInfo fromDayOfAD(int day) {
		return fromDate(DateToDay.Allow5To7Dig.toDate(day));
	}

	public static DateInfo fromDate(int date) {
		int d =(int)(date % 100);
		date /= 100;
		int m =(int)(date % 100);
		int y =(int)(date / 100);

		return new DateInfo(y, m, d);
	}

	public DateInfo(int y, int m, int d) {
		_inner = new DateTimeInfo(y, m, d, 0, 0, 0);
	}

	public int getYear() {
		return _inner.getYear();
	}

	public DateInfo changeYear(int y) {
		return new DateInfo(y, getMonth(), getDay());
	}

	public int getMonth() {
		return _inner.getMonth();
	}

	public DateInfo changeMonth(int m) {
		return new DateInfo(getYear(), m, getDay());
	}

	public int getDay() {
		return _inner.getDay();
	}

	public DateInfo changeDay(int d) {
		return new DateInfo(getYear(), getMonth(), d);
	}

	public int getDate() {
		return (int)(_inner.getDateTime() / 1000000L);
	}

	public int getDayOfAD() {
		return (int)(_inner.getSec() / 86400L);
	}

	public int getWeekdayIndex() {
		return _inner.getWeekdayIndex();
	}

	public String getWeekday() {
		return _inner.getWeekday();
	}

	@Override
	public String toString() {
		return toString("Y/M/D (W)");
	}

	public String toString(String format) {
		return _inner.toString(format);
	}

	public static DateInfo fromString(String str) {
		List<String> tokens = StringTools.tokenize(str, StringTools.DECIMAL, true);

		if(tokens.size() != 3) {
			throw new IllegalArgumentException(str);
		}
		int y = Integer.parseInt(tokens.get(0));
		int m = Integer.parseInt(tokens.get(1));
		int d = Integer.parseInt(tokens.get(2));

		return new DateInfo(y, m, d);
	}
}
