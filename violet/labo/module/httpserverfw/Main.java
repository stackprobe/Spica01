package violet.labo.module.httpserverfw;

public class Main {
	public static void main(String[] args) {
		try {
			System.out.println("Press any key to stop the server.");

			Config.main_args = args;
			new Server().perform();

			System.out.println("Stopped the server.");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
