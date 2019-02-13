package charlotte.tools;

import java.util.List;

/**
 * sec     == second of A.D.
 * second  == second of minute
 * day     == day of month
 * dayOfAD == day of A.D.
 *
 */
public class DateTimeUnit {
	private int _y;
	private int _m;
	private int _d;
	private int _h;
	private int _i;
	private int _s;

	public static DateTimeUnit fromDayOfAD(int dayOfAD) {
		return fromSec(dayOfAD * 86400L);
	}

	public static DateTimeUnit fromDate(int date) {
		return fromDateTime(date * 1000000L);
	}

	public static DateTimeUnit now() {
		return fromDateTime(DateTimeToSec.Now.getDateTime());
	}

	public static DateTimeUnit fromSec(long sec) {
		return fromDateTime(DateTimeToSec.Allow11To13Dig.toDateTime(sec));
	}

	public static DateTimeUnit fromDateTime(long dateTime) {
		int s =(int)(dateTime % 100L);
		dateTime /= 100L;
		int i =(int)(dateTime % 100L);
		dateTime /= 100L;
		int h =(int)(dateTime % 100L);
		dateTime /= 100L;
		int d =(int)(dateTime % 100L);
		dateTime /= 100L;
		int m =(int)(dateTime % 100L);
		int y =(int)(dateTime / 100L);

		return new DateTimeUnit(y, m, d, h, i, s);
	}

	public DateTimeUnit(int y, int m, int d, int h, int i, int s) {
		_y = y;
		_m = m;
		_d = d;
		_h = h;
		_i = i;
		_s = s;
	}

	public int getYear() {
		return _y;
	}

	public DateTimeUnit changeYear(int y) {
		return new DateTimeUnit(y, _m, _d, _h, _i, _s);
	}

	public int getMonth() {
		return _m;
	}

	public DateTimeUnit changeMonth(int m) {
		return new DateTimeUnit(_y, m, _d, _h, _i, _s);
	}

	public int getDay() {
		return _d;
	}

	public DateTimeUnit changeDay(int d) {
		return new DateTimeUnit(_y, _m, d, _h, _i, _s);
	}

	public int getHour() {
		return _h;
	}

	public DateTimeUnit changeHour(int h) {
		return new DateTimeUnit(_y, _m, _d, h, _i, _s);
	}

	public int getMinute() {
		return _i;
	}

	public DateTimeUnit changeMinute(int i) {
		return new DateTimeUnit(_y, _m, _d, _h, i, _s);
	}

	public int getSecond() {
		return _s;
	}

	public DateTimeUnit changeSecond(int s) {
		return new DateTimeUnit(_y, _m, _d, _h, _i, s);
	}

	public long getDateTime() {
		return
				_y * 10000000000L +
				_m * 100000000L +
				_d * 1000000L +
				_h * 10000L +
				_i * 100L +
				_s;
	}

	public long getSec() {
		return DateTimeToSec.Allow11To13Dig.toSec(getDateTime());
	}

	public int getDate() {
		return (int)(getDateTime() / 1000000L);
	}

	public int getDayOfAD() {
		return (int)(getSec() / 86400L);
	}

	public int getWeekdayIndex() {
		return getDayOfAD() % 7;
	}

	public String getWeekday() {
		return new String(new char[] { "月火水木金土日".charAt(getWeekdayIndex()) });
	}

	@Override
	public String toString() {
		return toString("Y/M/D (W) h:m:s");
	}

	public String toString(String format) {
		String ret = format;

		ret = ret.replace("Y", String.format("%d", _y));
		ret = ret.replace("M", String.format("%02d", _m));
		ret = ret.replace("D", String.format("%02d", _d));
		ret = ret.replace("h", String.format("%02d", _h));
		ret = ret.replace("m", String.format("%02d", _i));
		ret = ret.replace("s", String.format("%02d", _s));
		ret = ret.replace("W", getWeekday());

		return ret;
	}

	public static DateTimeUnit fromString(String str) {
		List<String> tokens = StringTools.tokenize(str, StringTools.DECIMAL, true, true);

		if(tokens.size() == 1) {
			String token = tokens.get(0);

			if(token.length() == 8) {
				return fromDate(Integer.parseInt(token));
			}
			if(token.length() == 14) {
				return fromDateTime(Long.parseLong(token));
			}
		}
		else if(tokens.size() == 3) {
			int y = Integer.parseInt(tokens.get(0));
			int m = Integer.parseInt(tokens.get(1));
			int d = Integer.parseInt(tokens.get(2));

			return new DateTimeUnit(y, m, d, 0, 0, 0);
		}
		else if(tokens.size() == 6 ||
				tokens.size() == 7) { // ? oracle '2015-02-10 00:00:00.0'
			int y = Integer.parseInt(tokens.get(0));
			int m = Integer.parseInt(tokens.get(1));
			int d = Integer.parseInt(tokens.get(2));
			int h = Integer.parseInt(tokens.get(3));
			int i = Integer.parseInt(tokens.get(4));
			int s = Integer.parseInt(tokens.get(5));

			return new DateTimeUnit(y, m, d, h, i, s);
		}
		throw new IllegalArgumentException(str);
	}
}
