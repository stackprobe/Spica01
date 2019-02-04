package charlotte.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CipherTools {
	public interface IBlockCipher extends AutoCloseable {
		default void encryptBlock(byte[] src, byte[] dest) throws Exception {
			encryptBlock(src, 0, dest, 0);
		}

		default void decryptBlock(byte[] src, byte[] dest) throws Exception {
			decryptBlock(src, 0, dest, 0);
		}

		void encryptBlock(byte[] src, int srcOffset, byte[] dest, int destOffset) throws Exception;
		void decryptBlock(byte[] src, int srcOffset, byte[] dest, int destOffset) throws Exception;
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

		@Override
		public void encryptBlock(byte[] src, int srcOffset, byte[] dest, int destOffset) throws Exception {
			_encryptor.doFinal(src, srcOffset, 16, dest, destOffset);
		}

		@Override
		public void decryptBlock(byte[] src, int srcOffset, byte[] dest, int destOffset) throws Exception {
			_decryptor.doFinal(src, srcOffset, 16, dest, destOffset);
		}

		@Override
		public void close() throws Exception {
			// noop
		}
	}

	public static class Camellia implements IBlockCipher {
		private Object _encryptor;
		private Object _decryptor;
		private ReflectTools.MethodUnit _processBlock;

		public Camellia(byte[] rawKey) throws Exception {
			if(
					rawKey.length != 16 &&
					rawKey.length != 24 &&
					rawKey.length != 32
					) {
				throw new IllegalArgumentException();
			}
			_encryptor = ReflectTools.getConstructor(Class.forName("jp.co.ntt.isl.camellia.Camellia")).invoke(new Object[0]);
			_decryptor = ReflectTools.getConstructor(Class.forName("jp.co.ntt.isl.camellia.Camellia")).invoke(new Object[0]);

			ReflectTools.getMethod(Class.forName("jp.co.ntt.isl.camellia.Camellia"), "init").invoke(_encryptor, new Object[] { true, rawKey });
			ReflectTools.getMethod(Class.forName("jp.co.ntt.isl.camellia.Camellia"), "init").invoke(_decryptor, new Object[] { false, rawKey });

			_processBlock = ReflectTools.getMethod(Class.forName("jp.co.ntt.isl.camellia.Camellia"), "processBlock");
		}

		@Override
		public void encryptBlock(byte[] src, int srcOffset, byte[] dest, int destOffset) throws Exception {
			_processBlock.invoke(_encryptor, new Object[] { src, srcOffset, dest, destOffset });
		}

		@Override
		public void decryptBlock(byte[] src, int srcOffset, byte[] dest, int destOffset) throws Exception {
			_processBlock.invoke(_decryptor, new Object[] { src, srcOffset, dest, destOffset });
		}

		@Override
		public void close() throws Exception {
			// noop
		}
	}
}
