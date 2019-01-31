package wb.t20181226;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 99/99 99:99 99:99
 * 99/99 99:99 99:99
 * 99/99 99:99 99:99
 * ...
 *
 */
public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		List<String> lines = new ArrayList<String>();
		List<String> cppLines = new ArrayList<String>(); // コピペ用lines

		try(Scanner scanner = new Scanner(System.in)) {
			for(; ; ) {
				String line = scanner.nextLine();

				// line as "MM/DD hh:mm hh:mm" or ""

				if(line.isEmpty()) {
					break;
				}
				lines.add(line);
			}
		}

		int mm = 0;
		int currDD = 0;

		for(String line : lines) {
			String[] tokens = line.split("[ ]");

			if(tokens.length != 3) {
				throw null;
			}
			String[] dTkns = tokens[0].split("[/]");
			String[] sTkns = tokens[1].split("[:]");
			String[] eTkns = tokens[2].split("[:]");

			if(dTkns.length != 2 || sTkns.length != 2 || eTkns.length != 2) {
				throw null;
			}
			int dm = Integer.parseInt(dTkns[0]);
			int dd = Integer.parseInt(dTkns[1]);
			int sh = Integer.parseInt(sTkns[0]);
			int sm = Integer.parseInt(sTkns[1]);
			int eh = Integer.parseInt(eTkns[0]);
			int em = Integer.parseInt(eTkns[1]);

			if(
					dm < 1 || 12 < dm ||
					dd < 1 || 31 < dd ||
					sh < 0 || 23 < sh ||
					sm < 0 || 59 < sm ||
					eh < 0 || 23 < eh ||
					em < 0 || 59 < em ||
					(eh * 100 + em) < (sh * 100 + sm)
					) {
				throw null;
			}

			while(++currDD < dd) {
				cppLines.add("");

				System.out.println(String.format("%d/%02d", dm, currDD));
			}

			int m = (eh * 60 + em) - (sh * 60 + sm);
			int k = 0;

			if(sh <= 12 && 13 <= eh) {
				k = 45;
			}
			m -= k;

			cppLines.add(sh + "\t\t" + sm + "\t" + eh + "\t\t" + em + "\t" + k);

			{
				int dayH = m / 60;
				int dayM = m % 60;

				System.out.println(String.format("%s --> (%02d) %d:%02d == %.3f", line, k, dayH, dayM, dayH + dayM / 60.0));
			}

			mm += m;
		}

		System.out.println("CPP>");
		for(String line : cppLines) {
			System.out.println(line);
		}
		System.out.println("<CPP");

		{
			int h = mm / 60;
			int m = mm % 60;

			System.out.println(String.format("%d:%02d", h, m));
		}
	}
}
