package tests.charlotte.tools;

import charlotte.tools.KernelTools;
import charlotte.tools.SpicaToolkit;

public class SpicaToolkitTest {
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

	private static void test01() throws Exception {
		Process p = SpicaToolkit.exec("/NAMED-EVENT AAA EEE TTT " + KernelTools.PID);

		Thread.sleep(100); // XXX
		if(p.isAlive() == false) throw null;

		Process q = SpicaToolkit.exec("/NAMED-EVENT-WAIT AAA -1 " + KernelTools.PID);

		Thread.sleep(100); // XXX
		if(p.isAlive() == false) throw null;
		if(q.isAlive() == false) throw null;

		Process r = SpicaToolkit.exec("/NAMED-EVENT-SET AAA");
		r.waitFor();

		Thread.sleep(100); // XXX
		if(p.isAlive() == false) throw null;
		if(q.isAlive()) throw null;
		if(r.isAlive()) throw null;

		Process s = SpicaToolkit.exec("/NAMED-EVENT-SET TTT");

		Thread.sleep(100); // XXX
		if(p.isAlive()) throw null;
		if(s.isAlive()) throw null;
	}
}
