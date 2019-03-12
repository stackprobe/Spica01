package violet.labo.module.httpserverfw;

public class Main {
	public static void main(String[] args) {
		try {
			Config.main_args = args;

			System.out.println("Press any key to stop the server.");

			new Server().perform();

			System.out.println("Stopped the server.");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
