package wb.t20190402;

import charlotte.tools.ListTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			test03();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void test01() {
		RouteSearch<Integer> rs = new RouteSearch<Integer>() {
			@Override
			protected Integer first() {
				if(route.size() == 3) {
					System.out.println("[" + String.join(", ", ListTools.select(route.iterator(), v -> "" + v)) + "]"); // goal
					return null;
				}
				return 0;
			}

			@Override
			protected Integer next(Integer value) {
				if(value == 2) {
					return null;
				}
				return value + 1;
			}
		};

		rs.perform();
	}

	/**
	 * https://github.com/stackprobe/Annex/blob/master/Violet/WolfSheep/Colorful.c
	 *
	 */
	public static void test02() {
		final int A_LEN = 10;
		final int RADIX = 3;

		RouteSearch<Integer> rs = new RouteSearch<Integer>() {
			private int _goalCount = 0;

			private void goal() {
				_goalCount++;
				System.out.println(_goalCount + " = [" + String.join(", ", ListTools.select(route.iterator(), v -> "" + v)) + "]");
			}

			private boolean check() {
				for(int index = 1; index < route.size() - 1; index++) {
					if(
							route.get(index - 1).intValue() == route.get(route.size() - 2).intValue() &&
							route.get(index - 0).intValue() == route.get(route.size() - 1).intValue()
							) {
						return false;
					}
				}
				return true;
			}

			@Override
			protected Integer first() {
				if(check() == false) {
					return null;
				}
				if(route.size() == A_LEN) {
					goal();
					return null;
				}
				return 0;
			}

			@Override
			protected Integer next(Integer value) {
				if(value == RADIX - 1) {
					return null;
				}
				return value + 1;
			}
		};

		rs.perform();
	}

	/**
	 * https://github.com/stackprobe/Annex/blob/master/Violet/WolfSheep/WolfSheep.c
	 *
	 */
	public static void test03() {
		final int W_NUM = 10;
		final int S_NUM = 10;

		final int W_SPAN = 4;
		final int W_MAX = 2;

		RouteSearch<String> rs = new RouteSearch<String>() {
			private int _wCount = 0;
			private int _sCount = 0;
			private int _goalCount = 0;

			private void goal() {
				_goalCount++;
				System.out.println(_goalCount + " = " + String.join("", route));
			}

			private boolean check() {
				if(W_NUM < _wCount) {
					return false;
				}
				if(S_NUM < _sCount) {
					return false;
				}
				if(W_SPAN <= route.size()) {
					int count = 0;

					for(int index = route.size() - W_SPAN; index < route.size(); index++) {
						if("W".equals(route.get(index))) {
							count++;
						}
					}
					if(W_MAX < count) {
						return false;
					}
				}
				return true;
			}

			@Override
			protected String first() {
				if(check() == false) {
					return null;
				}
				if(W_NUM + S_NUM <= route.size()) {
					goal();
					return null;
				}
				_wCount++;
				return "W";
			}

			@Override
			protected String next(String value) {
				if("W".equals(value)) {
					_wCount--;
					_sCount++;
					return "S";
				}
				_sCount--;
				return null;
			}
		};

		rs.perform();
	}
}
