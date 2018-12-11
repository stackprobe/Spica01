package tests.charlotte.tools;

import charlotte.tools.JsonTools;
import charlotte.tools.ObjectTree;

public class ObjectTreeTest {
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
		{
			ObjectTree ot = new ObjectTree(JsonTools.decode("{ 1: { 2: { 3: 777 }}}"));

			System.out.println("777 == " + ot.get("1/2/3").asString());

			System.out.println("ot: " + ot);
		}

		{
			ObjectTree ot = new ObjectTree(JsonTools.decode("[ 1, [ 1, 2, [ 1, 2, 3, 777 ]]]"));

			System.out.println("777 == " + ot.get("1/2/3").asString());

			System.out.println("ot: " + ot);
		}
	}
}
