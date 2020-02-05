package charlotte.tools;

/**
 * from 1000/1/1 to 9999/12/31
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
	 * from 1/1/1 to 9999/12/31
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
		public static int getDay() {
			return (int)(DateTimeToSec.Now.getSec() / 86400L);
		}

		public static int getDate() {
			return (int)(DateTimeToSec.Now.getDateTime() / 1000000L);
		}
	}
}
