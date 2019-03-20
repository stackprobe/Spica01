package violet.labo.demo.httpserverfwdemo;

import violet.labo.module.httpserverfw.Config;
import violet.labo.module.httpserverfw.Server;

public class Main {
	public static void main(String[] args) {
		try {
			Config.args = new String[] { "CONFIG_FILE=C:/var2/httpserverfwdemo/Config.properties" };

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
