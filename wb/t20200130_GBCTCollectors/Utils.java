package wb.t20200130_GBCTCollectors;

public class Utils {
	public static boolean contains(byte[] data, byte[] ptn) {
		for(int index = 0; index + ptn.length <= data.length; index++) {
			if(containsPos(data, index, ptn)) {
				return true;
			}
		}
		return false;
	}

	private static boolean containsPos(byte[] data, int dataOffset, byte[] ptn) {
		for(int index = 0; index < ptn.length; index++) {
			if(data[dataOffset + index] != ptn[index]) {
				return false;
			}
		}
		return true;
	}
}
