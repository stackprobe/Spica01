package charlotte.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CipherTools {
	public interface IBlockCipher extends AutoCloseable {
		void encryptBlock(byte[] src, byte[] dest) throws Exception;
		void decryptBlock(byte[] src, byte[] dest) throws Exception;
	}

	public static class AES implements IBlockCipher {
		private Cipher _encryptor;
		private Cipher _decryptor;

		public AES(byte[] rawKey) throws Exception {
			if(
					rawKey.length != 16 &&
					rawKey.length != 24 &&
					rawKey.length != 32
					) {
				throw new IllegalArgumentException();
			}
			_encryptor = Cipher.getInstance("AES/ECB/NoPadding");
			_decryptor = Cipher.getInstance("AES/ECB/NoPadding");

			_encryptor.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(rawKey, "AES"));
			_decryptor.init(Cipher.DECRYPT_MODE, new SecretKeySpec(rawKey, "AES"));
		}

		public void encryptBlock(byte[] src, byte[] dest) throws Exception {
			_encryptor.doFinal(src, 0, 16, dest, 0);
		}

		public void decryptBlock(byte[] src, byte[] dest) throws Exception {
			_decryptor.doFinal(src, 0, 16, dest, 0);
		}

		@Override
		public void close() throws Exception {
			// noop
		}
	}
}
