package charlotte.tools;

import java.util.TimeZone;

/**
 *	1000/1/1 から 9999/12/31 まで
 *
 */
public class DateTimeToSec {
	public static long toSec(long dateTime) {
		if(dateTime < 10000101000000L) {
			return 0L;
		}
		return Allow11To13Dig.toSec(dateTime);
	}

	public static long toDateTime(long sec) {
		return Math.max(10000101000000L, Allow11To13Dig.toDateTime(sec));
	}

	/**
	 *	1/1/1 から 9999/12/31 まで
	 *
	 */
	public static class Allow11To13Dig {
		public static long toSec(long dateTime) {
			if(dateTime < 10101000000L || 99991231235959L < dateTime) {
				return 0L;
			}

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

			if(
					y < 1 || 9999 < y ||
					m < 1 || 12 < m ||
					d < 1 || 31 < d ||
					h < 0 || 23 < h ||
					i < 0 || 59 < i ||
					s < 0 || 59 < s
					) {
				return 0L;
			}

			if(m <= 2) {
				y--;
			}

			long ret = y / 400;
			ret *= 365 * 400 + 97;

			y %= 400;

			ret += y * 365;
			ret += y / 4;
			ret -= y / 100;

			if(2 < m) {
				ret -= 31 * 10 - 4;
				m -= 3;
				ret +=(m / 5) *(31 * 5 - 2);
				m %= 5;
				ret +=(m / 2) *(31 * 2 - 1);
				m %= 2;
				ret += m * 31;
			}
			else {
				ret +=(m - 1) * 31;
			}

			ret += d - 1;
			ret *= 24;
			ret += h;
			ret *= 60;
			ret += i;
			ret *= 60;
			ret += s;

			return ret;
		}

		public static long toDateTime(long sec) {
			if(sec < 0L) {
				return 10101000000L;
			}

			int s =(int)(sec % 60L);
			sec /= 60L;
			int i =(int)(sec % 60L);
			sec /= 60L;
			int h =(int)(sec % 24L);
			sec /= 24L;

			if((long)Integer.MAX_VALUE < sec) {
				return 99991231235959L;
			}

			int day =(int)sec;
			int y =(day / 146097) * 400 + 1;
			int m = 1;
			int d;
			day %= 146097;

			day += Math.min((day + 306) / 36524, 3);
			y +=(day / 1461) * 4;
			day %= 1461;

			day += Math.min((day + 306) / 365, 3);
			y += day / 366;
			day %= 366;

			if(60 <= day) {
				m += 2;
				day -= 60;
				m +=(day / 153) * 5;
				day %= 153;
				m +=(day / 61) * 2;
				day %= 61;
			}
			m += day / 31;
			day %= 31;
			d = day + 1;

			if(y < 1) {
				return 10101000000L;
			}
			if(9999 < y) {
				return 99991231235959L;
			}

			if(
				//y < 1 || 9999 < y ||
				m < 1 || 12 < m ||
				d < 1 || 31 < d ||
				h < 0 || 23 < h ||
				m < 0 || 59 < m ||
				s < 0 || 59 < s
				)
				throw null; // never

			return
				y * 10000000000L +
				m * 100000000L +
				d * 1000000L +
				h * 10000L +
				i * 100L +
				s;
		}
	}

	public static class Now {
		public static long getSec() {
			return (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) / 1000 + POSIX_ZERO;
		}

		public static long getDateTime() {
			return toDateTime(getSec());
		}
	}

	public static final long POSIX_ZERO = toSec(19700101000000L);
}
