package charlotte.tools;

import java.util.List;

public class DateTimeUnit {
	private int _y;
	private int _m;
	private int _d;
	private int _h;
	private int _i;
	private int _s;

	public static DateTimeUnit now() {
		return fromDateTime(DateTimeToSec.Now.getDateTime());
	}

	/**
	 *
	 * @param sec second of A.D.
	 * @return
	 */
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
		int y = IntTools.range(_y, 0, 9999);
		int m = IntTools.range(_m, 0, 99);
		int d = IntTools.range(_d, 0, 99);
		int h = IntTools.range(_h, 0, 99);
		int i = IntTools.range(_i, 0, 99);
		int s = IntTools.range(_s, 0, 99);

		return
				y * 10000000000L +
				m * 100000000L +
				d * 1000000L +
				h * 10000L +
				i * 100L +
				s;
	}

	/**
	 *
	 * @return second of A.D.
	 */
	public long getSec() {
		return DateTimeToSec.Allow11To13Dig.toSec(getDateTime());
	}

	public int getWeekdayIndex() {
		return (int)((getSec() / 86400L) % 7L);
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
		List<String> tokens = StringTools.tokenize(str, StringTools.DECIMAL, true);

		if(tokens.size() != 6) {
			throw new IllegalArgumentException(str);
		}
		int y = Integer.parseInt(tokens.get(0));
		int m = Integer.parseInt(tokens.get(1));
		int d = Integer.parseInt(tokens.get(2));
		int h = Integer.parseInt(tokens.get(3));
		int i = Integer.parseInt(tokens.get(4));
		int s = Integer.parseInt(tokens.get(5));

		return new DateTimeUnit(y, m, d, h, i, s);
	}
}
