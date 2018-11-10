package charlotte.tools;

/**
 *	1000/1/1 から 9999/12/31 まで
 *
 */
public class DateToDay {
	public static int ToDay(int date) {
		return (int)(DateTimeToSec.toSec(date * 1000000L) / 86400L);
	}

	public static int ToDate(int day) {
		return (int)(DateTimeToSec.toDateTime(day * 86400L) / 1000000L);
	}

	/**
	 *	1/1/1 から 9999/12/31 まで
	 *
	 */
	public static class Allow5To7Dig {
		public static int ToDay(int date) {
			return (int)(DateTimeToSec.Allow11To13Dig.toSec(date * 1000000L) / 86400L);
		}

		public static int ToDate(int day) {
			return (int)(DateTimeToSec.Allow11To13Dig.toDateTime(day * 86400L) / 1000000L);
		}
	}
}
