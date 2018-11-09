package charlotte.tools;

public class DateToDay {
	public static int ToDay(int date) {
		return (int)(DateTimeToSec.toSec(date * 1000000L) / 86400L);
	}

	public static int ToDate(int day) {
		return (int)(DateTimeToSec.toDateTime(day * 86400L) / 1000000L);
	}

	public static class Allow5To7Dig {
		public static int ToDay(int date) {
			return (int)(DateTimeToSec.Allow11To13Dig.toSec(date * 1000000L) / 86400L);
		}

		public static int ToDate(int day) {
			return (int)(DateTimeToSec.Allow11To13Dig.toDateTime(day * 86400L) / 1000000L);
		}
	}
}
