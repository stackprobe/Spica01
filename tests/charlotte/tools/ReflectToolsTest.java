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
		// 親クラスの実装インターフェイスは拾ってこない。
		for(Class<?> interfaceObj : Class01.class.getInterfaces()) {
			System.out.println("" + interfaceObj);
		}

		System.out.println("----");

		// 実装インターフェイスの実装インターフェイスは拾ってこない。
		for(Class<?> interfaceObj : Class02.class.getInterfaces()) {
			System.out.println("" + interfaceObj);
		}

		System.out.println("----");

		for(Class<?> interfaceObj : Interface01.class.getInterfaces()) {
			System.out.println("" + interfaceObj);
		}
	}
}
