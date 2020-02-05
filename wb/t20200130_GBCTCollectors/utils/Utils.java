package wb.t20200130_GBCTCollectors.utils;

import charlotte.tools.DateTimeToSec;

public class Utils {
	public static long getFileStampByTimeMillis(long millis) {
		return DateTimeToSec.toDateTime(utcSecToJstSec(DateTimeToSec.POSIX_ZERO + millis / 1000L)) * 1000L;
	}

	private static long utcSecToJstSec(long sec) {
		return sec + 9 * 3600L;
	}
}
