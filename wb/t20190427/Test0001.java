package deneb.wb.t20190427;

import charlotte.tools.Base64Unit;
import charlotte.tools.BinTools;

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
		//Base64Unit b64u = Base64Unit.createByC6364P("`?=");
		Base64Unit b64u = Base64Unit.createByC6364P("?`=");

		byte[] d = b64u.decode(
				///////////////////////////////////////////// $_git:secret
				);

		System.out.println(BinTools.Hex.toString(d));
		//System.out.println(new String(d, StringTools.CHARSET_SJIS));
	}
}
