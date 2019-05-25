package wb.t20190525;

import charlotte.tools.StringTools;

public class Test0001 {
	public static void main(String[] args) {
		search("2152", 6, "");
	}

	private static void search(String curr, int rem, String route) {
		if(curr.equals("13")) {
			System.out.println("found: " + route);
			return;
		}

		if(rem <= 0) {
			return;
		}
		rem--;

		search(curr.replace("25", "12"), rem, route + "(25_12),");
		search(curr.replace("21", "3"), rem, route + "(21_3),");
		search(curr.replace("12", "5"), rem, route + "(12_5),");
		search(StringTools.reverse(curr), rem, route + "(rev),");
		search(curr.substring(curr.length() - 1) + curr.substring(0, curr.length() - 1), rem, route + "(sh>),");
	}
}
