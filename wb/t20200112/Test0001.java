package wb.t20200112;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.ThreadEx;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		String[] strs = new String[] {
				"AAA",
				"BBBB",
				"CCCCC",
				"DDDDDD",
				"EEEEEEE",
				"FFFFFFFF",
		};

		List<ThreadEx> ths = new ArrayList<ThreadEx>();
		int index = 0;

		for(String str : strs) {
			int tIndex = index++;
			String tmp = str;

			ThreadEx th = new ThreadEx(() -> {
				Thread.sleep(500);
				System.out.println(String.join(", ", "" + tIndex, str, tmp));
			});

			ths.add(th);
		}

		System.out.println("STARTED");

		for(ThreadEx th : ths) {
			th.waitToEnd();
		}

		System.out.println("ENDED");
	}
}
