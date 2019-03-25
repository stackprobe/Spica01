package violet.labo.module.httpserverfw.html.tag;

import java.util.Map;

import violet.labo.module.httpserverfw.ContextInfo;

/**
 *	呼び出される順序
 *		サーバー起動時
 *			setAttributes()
 *			setParent()
 *			init()
 *			activate()
 *			activate2()
 *
 *		ページアクセス時
 *			access()
 *			access2()
 *			getHTML()
 *
 *	ページ内で呼び出される順序
 *		サーバー起動時
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)
 *				setAttributes()
 *				setParent()
 *				init()
 *
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)
 *				activate()
 *
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)の逆順
 *				activate2()
 *
 *		ページアクセス時
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)
 *				access() [*A]
 *
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)の逆順
 *				access2() [*A]
 *
 *			ページ内の全ての ITag について、右の順で -> (階層が浅い順, 同じ階層の場合、先に定義された順)の逆順
 *				getHTML() [*A]
 *
 *	[*A] ... AnotherContent, AnotherHTML を throw することにより、別のコンテンツをレスポンス出来る。
 *
 *	呼び出される順序から親子関係を判別出来ないので、以下を使用すること。
 *	親を取得するには -> TagBase.getParent (但し init 以降)
 *	子を取得するには -> TagBase.getChildren, TagBase.dive (但し activate 以降)
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
