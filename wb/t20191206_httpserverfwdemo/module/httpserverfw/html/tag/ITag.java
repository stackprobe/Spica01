package wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag;

import java.util.Map;

import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;

/**
 *	呼び出される順序 // orig: *	\u547c\u3073\u51fa\u3055\u308c\u308b\u9806\u5e8f
 *		サーバー起動時 // orig: *		\u30b5\u30fc\u30d0\u30fc\u8d77\u52d5\u6642
 *			setAttributes()
 *			setParent()
 *			init()
 *			activate()
 *			activate2()
 *
 *		ページアクセス時 // orig: *		\u30da\u30fc\u30b8\u30a2\u30af\u30bb\u30b9\u6642
 *			access()
 *			access2()
 *			getHTML()
 *
 *	ページ内で呼び出される順序 // orig: *	\u30da\u30fc\u30b8\u5185\u3067\u547c\u3073\u51fa\u3055\u308c\u308b\u9806\u5e8f
 *		サーバー起動時 // orig: *		\u30b5\u30fc\u30d0\u30fc\u8d77\u52d5\u6642
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順) // orig: *			\u30da\u30fc\u30b8\u5185\u306e\u5168\u3066\u306e ITag \u306b\u3064\u3044\u3066\u3001\u53f3\u306e\u9806\u3067 -> (\u968e\u5c64\u304c\u6d45\u3044\u9806, \u540c\u3058\u968e\u5c64\u306e\u5834\u5408\u3001\u5148\u306b\u5b9a\u7fa9\u3055\u308c\u305f\u9806)
 *				setAttributes()
 *				setParent()
 *				init()
 *
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順) // orig: *			\u30da\u30fc\u30b8\u5185\u306e\u5168\u3066\u306e ITag \u306b\u3064\u3044\u3066\u3001\u53f3\u306e\u9806\u3067 -> (\u968e\u5c64\u304c\u6d45\u3044\u9806, \u540c\u3058\u968e\u5c64\u306e\u5834\u5408\u3001\u5148\u306b\u5b9a\u7fa9\u3055\u308c\u305f\u9806)
 *				activate()
 *
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)の逆順 // orig: *			\u30da\u30fc\u30b8\u5185\u306e\u5168\u3066\u306e ITag \u306b\u3064\u3044\u3066\u3001\u53f3\u306e\u9806\u3067 -> (\u968e\u5c64\u304c\u6d45\u3044\u9806, \u540c\u3058\u968e\u5c64\u306e\u5834\u5408\u3001\u5148\u306b\u5b9a\u7fa9\u3055\u308c\u305f\u9806)\u306e\u9006\u9806
 *				activate2()
 *
 *		ページアクセス時 // orig: *		\u30da\u30fc\u30b8\u30a2\u30af\u30bb\u30b9\u6642
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順) // orig: *			\u30da\u30fc\u30b8\u5185\u306e\u5168\u3066\u306e ITag \u306b\u3064\u3044\u3066\u3001\u53f3\u306e\u9806\u3067 -> (\u968e\u5c64\u304c\u6d45\u3044\u9806, \u540c\u3058\u968e\u5c64\u306e\u5834\u5408\u3001\u5148\u306b\u5b9a\u7fa9\u3055\u308c\u305f\u9806)
 *				access() [*A]
 *
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)の逆順 // orig: *			\u30da\u30fc\u30b8\u5185\u306e\u5168\u3066\u306e ITag \u306b\u3064\u3044\u3066\u3001\u53f3\u306e\u9806\u3067 -> (\u968e\u5c64\u304c\u6d45\u3044\u9806, \u540c\u3058\u968e\u5c64\u306e\u5834\u5408\u3001\u5148\u306b\u5b9a\u7fa9\u3055\u308c\u305f\u9806)\u306e\u9006\u9806
 *				access2() [*A]
 *
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)の逆順 // orig: *			\u30da\u30fc\u30b8\u5185\u306e\u5168\u3066\u306e ITag \u306b\u3064\u3044\u3066\u3001\u53f3\u306e\u9806\u3067 -> (\u968e\u5c64\u304c\u6d45\u3044\u9806, \u540c\u3058\u968e\u5c64\u306e\u5834\u5408\u3001\u5148\u306b\u5b9a\u7fa9\u3055\u308c\u305f\u9806)\u306e\u9006\u9806
 *				getHTML() [*A]
 *
 *	[*A] ... AnotherContent, AnotherHTML を throw することにより、別のコンテンツをレスポンス出来る。 // orig: *	[*A] ... AnotherContent, AnotherHTML \u3092 throw \u3059\u308b\u3053\u3068\u306b\u3088\u308a\u3001\u5225\u306e\u30b3\u30f3\u30c6\u30f3\u30c4\u3092\u30ec\u30b9\u30dd\u30f3\u30b9\u51fa\u6765\u308b\u3002
 *
 *	呼び出される順序から親子関係を判別出来ないので、以下を使用すること。 // orig: *	\u547c\u3073\u51fa\u3055\u308c\u308b\u9806\u5e8f\u304b\u3089\u89aa\u5b50\u95a2\u4fc2\u3092\u5224\u5225\u51fa\u6765\u306a\u3044\u306e\u3067\u3001\u4ee5\u4e0b\u3092\u4f7f\u7528\u3059\u308b\u3053\u3068\u3002
 *	親を取得するには -> TagBase.getParent (但し init 以降) // orig: *	\u89aa\u3092\u53d6\u5f97\u3059\u308b\u306b\u306f -> TagBase.getParent (\u4f46\u3057 init \u4ee5\u964d)
 *	子を取得するには -> TagBase.getChildren, TagBase.dive (但し activate 以降) // orig: *	\u5b50\u3092\u53d6\u5f97\u3059\u308b\u306b\u306f -> TagBase.getChildren, TagBase.dive (\u4f46\u3057 activate \u4ee5\u964d)
 *
 */
public interface ITag {
	void setAttributes(Map<String, String> attributes);
	void setParent(ITag parent);
	void init();
	default void activate() { }
	default void activate2() { }
	void access(ContextInfo context);
	default void access2(ContextInfo context) { }
	String getHTML(ContextInfo context, String innerHtml);
}
