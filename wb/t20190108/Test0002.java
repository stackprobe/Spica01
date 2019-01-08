package wb.t20190108;

import java.util.Calendar;
import java.util.TimeZone;

import charlotte.tools.DateTimeToSec;

public class Test0002 {
	public static void main(String[] args) {
		try {
			dateTimeToCalendar();
			calendarToDateTime();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void dateTimeToCalendar() {
		Calendar cal = Calendar.getInstance();

		{
			long dateTime = DateTimeToSec.Now.getDateTime();

			int s = (int)(dateTime % 100);
			dateTime /= 100;
			int i = (int)(dateTime % 100);
			dateTime /= 100;
			int h = (int)(dateTime % 100);
			dateTime /= 100;
			int d = (int)(dateTime % 100);
			dateTime /= 100;
			int m = (int)(dateTime % 100);
			int y = (int)(dateTime / 100);

			cal.set(y, m, d, h, i, s);
		}

		System.out.println(cal.toString().replace(",", ",\r\n\t"));
	}

	private static void calendarToDateTime() {
		Calendar cal = Calendar.getInstance();

		long millis = cal.getTimeInMillis();
		long dateTime = DateTimeToSec.toDateTime((millis + TimeZone.getDefault().getRawOffset()) / 1000L + DateTimeToSec.POSIX_ZERO);

		System.out.println(dateTime);
		System.out.println(DateTimeToSec.Now.getDateTime());
	}
}
