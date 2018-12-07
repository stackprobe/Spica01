package charlotte.tools;

import java.util.List;

/**
 *	1000/1/1 から 9999/12/31 まで
 *
 */
public class DateToDay {
	public static int toDay(int date) {
		return (int)(DateTimeToSec.toSec(date * 1000000L) / 86400L);
	}

	public static int toDate(int day) {
		return (int)(DateTimeToSec.toDateTime(day * 86400L) / 1000000L);
	}

	/**
	 *	1/1/1 から 9999/12/31 まで
	 *
	 */
	public static class Allow5To7Dig {
		public static int toDay(int date) {
			return (int)(DateTimeToSec.Allow11To13Dig.toSec(date * 1000000L) / 86400L);
		}

		public static int toDate(int day) {
			return (int)(DateTimeToSec.Allow11To13Dig.toDateTime(day * 86400L) / 1000000L);
		}
	}

	public static class Now {
		public static long getDay() {
			return (int)(DateTimeToSec.Now.getSec() / 86400L);
		}

		public static long getDate() {
			return (int)(DateTimeToSec.Now.getDateTime() / 1000000L);
		}
	}

	public static String dateToString(int date) {
		return dateToString(date, "Y/M/D");
	}

	public static String dateToString(int date, String format) {
		int d = date % 100;
		date /= 100;
		int m = date % 100;
		int y = date / 100;

		String ret = format;

		ret = ret.replace("Y", String.format("%d", y));
		ret = ret.replace("M", String.format("%02d", m));
		ret = ret.replace("D", String.format("%02d", d));

		return ret;
	}

	public static int stringToDate(String str) {
		List<String> tokens = StringTools.tokenize(str, StringTools.DECIMAL, true);

		if(tokens.size() != 3) {
			throw new RTError();
		}
		int y = Integer.parseInt(tokens.get(0));
		int m = Integer.parseInt(tokens.get(1));
		int d = Integer.parseInt(tokens.get(2));

		return y * 10000 + m * 100 + d;
	}
}
