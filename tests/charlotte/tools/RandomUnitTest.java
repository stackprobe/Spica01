package tests.charlotte.tools;

import charlotte.tools.IntTools;
import charlotte.tools.SecurityTools;

public class RandomUnitTest {
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

	private static void test01() {
		int[] ns = IntTools.sequence(10);

		for(int n : ns) {
			System.out.print(" " + n);
		}
		System.out.println("");

		SecurityTools.cRandom.shuffle(IntTools.asList(ns));

		for(int n : ns) {
			System.out.print(" " + n);
		}
		System.out.println("");
	}
}
