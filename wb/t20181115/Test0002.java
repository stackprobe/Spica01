package wb.t20181115;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.DateToDay;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class Test0002 {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		List<String> lines = new ArrayList<String>();

		for(int date = 18000101; date <= 20991231; date = DateToDay.ToDate(DateToDay.ToDay(date) + 1)) {
			lines.add(date + " -> " + EraCalendar.i().getEraDate(date) + " -> " + EraCalendar.i().getEraDate(date).toDate());

			if(date != EraCalendar.i().getEraDate(date).toDate()) {
				throw null; // bugged !!!
			}
		}
		FileTools.writeAllLines("C:/temp/EraCalendar_test.txt", lines, StringTools.CHARSET_SJIS);
	}

	private static void test02() throws Exception {
		for(int date = 18000101; date <= 20991231; date = DateToDay.ToDate(DateToDay.ToDay(date) + 1)) {
			System.out.println("" + date); // test

			EraCalendar.EraDate eraDate = EraCalendar.i().getEraDate(date);

			String name = eraDate.toString("G");
			String nen = eraDate.toString("Y");
			int m = Integer.parseInt(eraDate.toString("M"));
			int d = Integer.parseInt(eraDate.toString("D"));

			eraDate = EraCalendar.i().getEraDate(name, nen, m, d);

			if(date != eraDate.toDate()) {
				throw null; // bugged !!!
			}
		}
	}
}
