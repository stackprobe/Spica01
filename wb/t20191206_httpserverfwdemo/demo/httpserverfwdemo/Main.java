package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo;

import wb.t20191206_httpserverfwdemo.module.httpserverfw.Config;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.Server;

public class Main {
	public static void main(String[] args) {
		try {
			Config.args = new String[] { "CONFIG_FILE=C:/wb2/20191205_httpserverfw/httpserverfwdemo/Config.properties" };

			System.out.println("[httpserverfwdemo] Press any key to stop the server.");

			new Server().perform();

			System.out.println("[httpserverfwdemo] Stopped the server.");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
