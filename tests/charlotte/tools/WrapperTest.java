package tests.charlotte.tools;

import java.util.List;

import charlotte.tools.IArrays;
import charlotte.tools.ListTools;
import charlotte.tools.Wrapper;

public class WrapperTest {
	public static void main(String[] args) {
		try {
			//test01();
			test01b();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		// no-Wrapper
		{
			String[] arr = "1:2:3:4:5".split("[:]");

			String ret = String.join(
					":",
					ListTools.select(
							ListTools.where(
									ListTools.select(
											IArrays.asList(arr),
											value -> Integer.parseInt(value)
											),
									value -> value % 2 != 0
									),
							value -> "" + value
							)
					);

			System.out.println(ret);
		}

		// Wrapper
		{
			String[] arr = "1:2:3:4:5".split("[:]");

			String ret = Wrapper.create(arr)
			//String ret = new Wrapper<String[]>(arr)
					.change(v -> IArrays.asList(v))
					.change(v -> ListTools.select(v, value -> Integer.parseInt(value)))
					.change(v -> ListTools.where(v, value -> value % 2 != 0))
					.change(v -> ListTools.select(v, value -> "" + value))
					.change(v -> String.join(":", v))
					.get();

			System.out.println(ret);
		}

		// var
		{
			String[] arr = "1:2:3:4:5".split("[:]");

			List<String> list = IArrays.asList(arr);
			List<Integer> intList = ListTools.select(list, value -> Integer.parseInt(value));
			intList = ListTools.where(intList, value -> value % 2 != 0);
			list = ListTools.select(intList, value -> "" + value);
			String ret = String.join(":", list);

			System.out.println(ret);
		}
	}

	private static void test01b() {
		System.out.println(test01b_noWrapper());
		System.out.println(test01b_Wrapper());
		System.out.println(test01b_var());
	}

	private static String test01b_noWrapper() {
		return String.join(
				":",
				ListTools.select(
						ListTools.where(
								ListTools.select(
										IArrays.asList(
												"1:2:3:4:5".split("[:]")
												),
										value -> Integer.parseInt(value)
										),
								value -> value % 2 != 0
								),
						value -> "" + value
						)
				);
	}

	private static String test01b_Wrapper() {
		return Wrapper.create("1:2:3:4:5".split("[:]"))
		//return new Wrapper<String[]>("1:2:3:4:5".split("[:]"))
				.change(v -> IArrays.asList(v))
				.change(v -> ListTools.select(v, value -> Integer.parseInt(value)))
				.change(v -> ListTools.where(v, value -> value % 2 != 0))
				.change(v -> ListTools.select(v, value -> "" + value))
				.change(v -> String.join(":", v))
				.get();
	}

	private static String test01b_var() {
		String[] arr = "1:2:3:4:5".split("[:]");
		List<String> list = IArrays.asList(arr);
		List<Integer> intList = ListTools.select(list, value -> Integer.parseInt(value));
		intList = ListTools.where(intList, value -> value % 2 != 0);
		list = ListTools.select(intList, value -> "" + value);
		return String.join(":", list);
	}
}
