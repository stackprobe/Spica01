package violet;

import java.util.Base64;
import java.util.Scanner;

import charlotte.tools.StringTools;

public class BasicAuthEnc {
	public static void main(String[] args) {
		try {
			main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void main2() throws Exception {
		String user;
		String password;

		try(Scanner scanner = new Scanner(System.in)) {
			user = scanner.nextLine();
			password = scanner.nextLine();
		}

		System.out.println("user: " + user);
		System.out.println("password: " + password);

		String plain = user + ":" + password;
		String enc = Base64.getEncoder().encodeToString(plain.getBytes(StringTools.CHARSET_UTF8));

		System.out.println("Authorization: Basic " + enc);
	}
}
