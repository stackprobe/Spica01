package wb.t20200312;

public class GeneralSerializer {
	/**
	 * オブジェクトのタイプ情報も含めること。
	 * クラスのフィールドからは、格納されているオブジェクトがフィールドの型なのか、フィールドの型を継承した型なのは判別出来ない。
	 *
	 * @param src not null
	 * @return null == 未知のオブジェクトなので変換出来ない。
	 */
	public static String getString(Object src) {
		return null; // TODO
	}

	/**
	 *
	 * @param src getString の戻り値
	 * @return 必ず適切なオブジェクトに変換する。
	 */
	public static Object getObject(String src) {
		return new Object(); // TODO
	}
}
