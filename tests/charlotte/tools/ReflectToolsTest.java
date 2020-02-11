package tests.charlotte.tools;

import tests.charlotte.tools.t0001.Class01;
import tests.charlotte.tools.t0001.Class02;
import tests.charlotte.tools.t0001.Interface01;

public class ReflectToolsTest {
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
		// 親クラスの実装インターフェイスは拾ってこない。 // orig: // \u89aa\u30af\u30e9\u30b9\u306e\u5b9f\u88c5\u30a4\u30f3\u30bf\u30fc\u30d5\u30a7\u30a4\u30b9\u306f\u62fe\u3063\u3066\u3053\u306a\u3044\u3002
		for(Class<?> interfaceObj : Class01.class.getInterfaces()) {
			System.out.println("" + interfaceObj);
		}

		System.out.println("----");

		// 実装インターフェイスの実装インターフェイスは拾ってこない。 // orig: // \u5b9f\u88c5\u30a4\u30f3\u30bf\u30fc\u30d5\u30a7\u30a4\u30b9\u306e\u5b9f\u88c5\u30a4\u30f3\u30bf\u30fc\u30d5\u30a7\u30a4\u30b9\u306f\u62fe\u3063\u3066\u3053\u306a\u3044\u3002
		for(Class<?> interfaceObj : Class02.class.getInterfaces()) {
			System.out.println("" + interfaceObj);
		}

		System.out.println("----");

		for(Class<?> interfaceObj : Interface01.class.getInterfaces()) {
			System.out.println("" + interfaceObj);
		}
	}
}
