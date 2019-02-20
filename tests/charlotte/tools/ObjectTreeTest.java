package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.JsonTools;
import charlotte.tools.ObjectTree;
import charlotte.tools.StringTools;

public class ObjectTreeTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		{
			ObjectTree ot = new ObjectTree(JsonTools.decode("{ 1: { 2: { 3: 777 }}}"));

			System.out.println("777 == " + ot.get("1/2/3").stringValue());

			System.out.println("ot: " + ot);
		}

		{
			ObjectTree ot = new ObjectTree(JsonTools.decode("[ 1, [ 1, 2, [ 1, 2, 3, 777 ]]]"));

			System.out.println("777 == " + ot.get("1/2/3").stringValue());

			System.out.println("ot: " + ot);
		}
	}

	private static void test02() throws Exception {
		{
			ObjectTree tree = ObjectTree.convert("ABC");

			System.out.println("tree: " + tree);
		}

		{
			ObjectTree tree = ObjectTree.convert("ABC".getBytes(StringTools.CHARSET_ASCII));

			System.out.println("tree: " + tree);
		}

		{
			ObjectTree tree = ObjectTree.convert("A:B:C".split("[:]"));

			System.out.println("tree: " + tree);
		}

		{
			List<String[]> rows = new ArrayList<String[]>();

			rows.add(new String[] { "A", "BB", "CCC" });
			rows.add(new String[] { "a", "bb", "ccc" });
			rows.add(new String[] { "1", "22", "333" });

			ObjectTree tree = ObjectTree.convert(rows);

			System.out.println("tree: " + tree);
		}
	}
}
