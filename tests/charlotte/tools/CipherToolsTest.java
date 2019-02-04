package tests.charlotte.tools;

import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.CipherTools;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class CipherToolsTest {
	public static void main(String[] args) {
		try {
			AESTest.test01();
			CamelliaTest.test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static class AESTest {
		private static void test01() throws Exception {
			List<String> lines = FileTools.readAllLines("C:/Factory/Labo/utest/auto/OpenSource/aes128/testvector/t_aes128.txt", StringTools.CHARSET_ASCII);
			byte[] rawKey = null;

			for(int index = 0; index < lines.size(); index++) {
				String line = lines.get(index);

				if(line.startsWith("K")) {
					rawKey = test01_getBlock(line);
				}
				else if(line.startsWith("P")) {
					byte[] plain = test01_getBlock(line);
					byte[] cipher = test01_getBlock(lines.get(++index));

					test01_encDecTest(rawKey, plain, cipher);
				}
			}
		}

		private static byte[] test01_getBlock(String line) {
			//System.out.println("line: " + line); // test
			return BinTools.Hex.toBytes(line.substring(line.indexOf(':') + 1).replace(" ", ""));
		}

		private static void test01_encDecTest(byte[] rawKey, byte[] plain, byte[] cipher) throws Exception {
			System.out.println("rawKey: " + BinTools.Hex.toString(rawKey)); // test
			System.out.println("plain: " + BinTools.Hex.toString(plain)); // test
			System.out.println("cipher: " + BinTools.Hex.toString(cipher)); // test

			if(rawKey.length != 16) throw null; // bugged !!! // only exists 128-bits now. @ 2019.2.4
			if(plain.length != 16) throw null; // bugged !!!
			if(cipher.length != 16) throw null; // bugged !!!

			try(CipherTools.AES aes = new CipherTools.AES(rawKey)) {
				byte[] encans = new byte[16];
				byte[] decans = new byte[16];

				aes.encryptBlock(plain, encans);
				aes.decryptBlock(cipher, decans);

				System.out.println("encans: " + BinTools.Hex.toString(encans)); // test
				System.out.println("decans: " + BinTools.Hex.toString(decans)); // test

				if(BinTools.comp_array.compare(encans, cipher) != 0) throw null; // bugged !!!
				if(BinTools.comp_array.compare(decans, plain) != 0) throw null; // bugged !!!
			}

			System.out.println("ok"); // test
		}
	}

	private static class CamelliaTest {
		private static void test01() throws Exception {
			List<String> lines = FileTools.readAllLines("C:/Factory/Labo/utest/auto/OpenSource/camellia/testvector/t_camellia.txt", StringTools.CHARSET_ASCII);
			byte[] rawKey = null;

			for(int index = 0; index < lines.size(); index++) {
				String line = lines.get(index);

				if(line.startsWith("K")) {
					rawKey = test01_getBlock(line);
				}
				else if(line.startsWith("P")) {
					byte[] plain = test01_getBlock(line);
					byte[] cipher = test01_getBlock(lines.get(++index));

					test01_encDecTest(rawKey, plain, cipher);
				}
			}
		}

		private static byte[] test01_getBlock(String line) {
			//System.out.println("line: " + line); // test
			return BinTools.Hex.toBytes(line.substring(line.indexOf(':') + 1).replace(" ", ""));
		}

		private static void test01_encDecTest(byte[] rawKey, byte[] plain, byte[] cipher) throws Exception {
			System.out.println("rawKey: " + BinTools.Hex.toString(rawKey)); // test
			System.out.println("plain: " + BinTools.Hex.toString(plain)); // test
			System.out.println("cipher: " + BinTools.Hex.toString(cipher)); // test

			if(rawKey.length != 16 && rawKey.length != 24 && rawKey.length != 32) throw null; // bugged !!!
			if(plain.length != 16) throw null; // bugged !!!
			if(cipher.length != 16) throw null; // bugged !!!

			try(CipherTools.Camellia camellia = new CipherTools.Camellia(rawKey)) {
				byte[] encans = new byte[16];
				byte[] decans = new byte[16];

				camellia.encryptBlock(plain, encans);
				camellia.decryptBlock(cipher, decans);

				System.out.println("encans: " + BinTools.Hex.toString(encans)); // test
				System.out.println("decans: " + BinTools.Hex.toString(decans)); // test

				if(BinTools.comp_array.compare(encans, cipher) != 0) throw null; // bugged !!!
				if(BinTools.comp_array.compare(decans, plain) != 0) throw null; // bugged !!!
			}

			System.out.println("ok"); // test
		}
	}
}
