package wb.t20181226;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

		for(String line : lines) {
			String[] tokens = line.split("[ ]");

			if(tokens.length != 3) {
				throw null;
			}
			String[] sTkns = tokens[1].split("[:]");
			String[] eTkns = tokens[2].split("[:]");

			if(sTkns.length != 2 || eTkns.length != 2) {
				throw null;
			}
			int sh = Integer.parseInt(sTkns[0]);
			int sm = Integer.parseInt(sTkns[1]);
			int eh = Integer.parseInt(eTkns[0]);
			int em = Integer.parseInt(eTkns[1]);

			if(
					sh < 0 || 23 < sh ||
					sm < 0 || 59 < sm ||
					eh < 0 || 23 < eh ||
					em < 0 || 59 < em ||
					(eh * 100 + em) < (sh * 100 + sm)
					) {
				throw null;
			}
			int m = (eh * 60 + em) - (sh * 60 + sm);

			if(sh <= 12 && 13 <= eh) {
				m -= 45;
			}

			{
				int dh = m / 60;
				int dm = m % 60;

				System.out.println(String.format("%s ---> %d:%02d", line, dh, dm));
			}

			mm += m;
		}

		{
			int h = mm / 60;
			int m = mm % 60;

			System.out.println(String.format("%d:%02d", h, m));
		}
	}
}
