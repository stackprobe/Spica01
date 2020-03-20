package wb.t20200312;

public class GeneralSerializer {
	/**
	 * オブジェクトのタイプ情報も含めること。 // orig: * \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30bf\u30a4\u30d7\u60c5\u5831\u3082\u542b\u3081\u308b\u3053\u3068\u3002
	 * クラスのフィールドからは、格納されているオブジェクトがフィールドの型なのか、フィールドの型を継承した型なのは判別出来ない。 // orig: * \u30af\u30e9\u30b9\u306e\u30d5\u30a3\u30fc\u30eb\u30c9\u304b\u3089\u306f\u3001\u683c\u7d0d\u3055\u308c\u3066\u3044\u308b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u304c\u30d5\u30a3\u30fc\u30eb\u30c9\u306e\u578b\u306a\u306e\u304b\u3001\u30d5\u30a3\u30fc\u30eb\u30c9\u306e\u578b\u3092\u7d99\u627f\u3057\u305f\u578b\u306a\u306e\u306f\u5224\u5225\u51fa\u6765\u306a\u3044\u3002
	 *
	 * @param src not null
	 * @return null == 未知のオブジェクトなので変換出来ない。 // orig: * @return null == \u672a\u77e5\u306e\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306a\u306e\u3067\u5909\u63db\u51fa\u6765\u306a\u3044\u3002
	 */
	public static String getString(Object src) {
		return null; // TODO
	}

	/**
	 *
	 * @param src getString の戻り値 // orig: * @param src getString \u306e\u623b\u308a\u5024
	 * @return 必ず適切なオブジェクトに変換する。 // orig: * @return \u5fc5\u305a\u9069\u5207\u306a\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306b\u5909\u63db\u3059\u308b\u3002
	 */
	public static Object getObject(String src) {
		return new Object(); // TODO
	}
}
