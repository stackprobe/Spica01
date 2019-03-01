package wb.t20190301;

import charlotte.tools.FileTools;
import charlotte.tools.KernelTools;
import charlotte.tools.StringTools;

public class Test0001 {
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
		FileTools.appendAllLines("C:/temp/JavaPIDLog.txt", new String[] { "" + KernelTools.PID }, StringTools.CHARSET_SJIS);
	}
}
