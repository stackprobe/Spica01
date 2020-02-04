package wb.t20200130_GBCTCollectors.utils;

import charlotte.tools.DateTimeToSec;

public class Utils {
	public static long getFileStampByTimeMillis(long millis) {
		return DateTimeToSec.toDateTime(DateTimeToSec.POSIX_ZERO + millis / 1000L) * 1000L;
	}
}
