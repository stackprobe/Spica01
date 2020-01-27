package violet.gbcTunnels.utils.camellia;

import charlotte.tools.RTError;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class CamelliaRingCipherUtils {
	private static final int X_EXP_MIN = 20;
	private static final int X_EXP_MAX = 50;

	private static final int X_PTN_SIZE = 1024 * 1024;
	private static final int X_PTN_EXP = 20;

	public static byte[] generateRawKey(String passphrase) throws Exception {
		byte[] bPassphrase = passphrase.getBytes(StringTools.CHARSET_SJIS);

		if(
				5 <= bPassphrase.length &&
				bPassphrase[bPassphrase.length - 1] == 0x5d && // ']'
				0x30 <= bPassphrase[bPassphrase.length - 2] && bPassphrase[bPassphrase.length - 2] <= 0x39 && // [0-9]
				0x30 <= bPassphrase[bPassphrase.length - 3] && bPassphrase[bPassphrase.length - 3] <= 0x39 && // [0-9]
				//bPassphrase[bPassphrase.length - 4] // any char
				bPassphrase[bPassphrase.length - 5] == 0x5b // '['
				) {
			byte xChr = bPassphrase[bPassphrase.length - 4];
			int xExp =
					((bPassphrase[bPassphrase.length - 3] & 0xff) - 0x30) * 10 +
					((bPassphrase[bPassphrase.length - 2] & 0xff) - 0x30);

			if(xExp < X_EXP_MIN || X_EXP_MAX < xExp) {
				throw new IllegalArgumentException();
			}
			byte[] xPtn = new byte[X_PTN_SIZE];

			for(int index = 0; index < X_PTN_SIZE; index++) {
				xPtn[index] = xChr;
			}
			int xNum = 1 << (xExp - X_PTN_EXP);

			return SecurityTools.getSHA512(writer -> RTError.run(() -> {
				writer.write(bPassphrase, 0, bPassphrase.length - 5);

				for(int count = 0; count < xNum; count++) {
					writer.write(xPtn, 0, X_PTN_SIZE);
				}
			}
			));
		}
		else {
			return SecurityTools.getSHA512(bPassphrase);
		}
	}
}
