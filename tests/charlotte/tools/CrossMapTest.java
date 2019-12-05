package tests.charlotte.tools;

import charlotte.tools.CrossMap;
import charlotte.tools.DebugTools;
import charlotte.tools.MapTools;

public class CrossMapTest {
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
		CrossMap<String, String> cm = MapTools.createCross();

		cm.add("A", "123");
		cm.add("B", "456");
		cm.add("C", "789");

		DebugTools.mustThrow(() -> cm.add("B", "999"));
		DebugTools.mustThrow(() -> cm.add("Z", "456"));

		for(String k : cm.keys()) {
			System.out.println("k: " + k);
		}
		for(String v : cm.values()) {
			System.out.println("v: " + v);
		}
		for(String k : cm.keys()) {
			System.out.println("kv: " + k + " ---> " + cm.get(k));
		}
		for(String v : cm.values()) {
			System.out.println("vk: " + v + " ---> " + cm.getKey(v));
		}
	}
}
