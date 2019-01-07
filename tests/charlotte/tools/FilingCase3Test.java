package tests.charlotte.tools;

import charlotte.tools.FilingCase3;
import charlotte.tools.StringTools;

public class FilingCase3Test {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		try(FilingCase3 client = new FilingCase3()) {
			client.delete("test-root");

			for(String file : client.list("test-root")) {
				System.out.println(file);
			}
			client.post("test-root/test-file", "test-data".getBytes(StringTools.CHARSET_ASCII));

			for(String file : client.list("test-root")) {
				System.out.println(file);
			}
			System.out.println(new String(client.get("test-root/test-file"), StringTools.CHARSET_ASCII));
		}
	}

	private static void test02() throws Exception {
		try(FilingCase3 client = new FilingCase3()) {
			client.delete("test-root");

			Thread.sleep(30000);

			client.delete("test-root");
		}
	}
}
