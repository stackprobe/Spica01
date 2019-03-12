package violet.labo.module.httpserverfw;

import java.io.File;
import java.util.Map;

import charlotte.tools.ArrayTools;
import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.MapTools;
import charlotte.tools.StringTools;

public class ContentPageInfo {
	private Map<String, ContentPageInfo> _children = null;
	private HTMLDesigner _designer = null;
	private String _contentType;
	private ResBodyFile _f = null;

	public ContentPageInfo(File f) {
		if(f.isDirectory()) {
			_children = MapTools.<ContentPageInfo>createIgnoreCase();

			for(File subF : f.listFiles()) {
				_children.put(subF.getName(), new ContentPageInfo(subF));
			}
		}
		else {
			String path = f.getName();

			if(ArrayTools.any(Config.i().SERVICE_PAGE_SUFFIXES, suffix -> StringTools.endsWithIgnoreCase(path, suffix))) {
				_designer = new HTMLDesigner(f);
			}
			else {
				_contentType = MIMEType.i().getMIMEType(FileTools.getExtension(f.getName()));
				_f = new ResBodyFile(f);
			}
		}
	}

	public ContentPageInfo get(String name) {
		return _children.get(name);
	}

	public ContentPageInfo getIndexPage() {
		return get(Config.i().INDEX_PAGE_NAME);
	}

	public boolean isDirectory() {
		return _children != null;
	}

	public Iterable<byte[]> getBody(ContextInfo context) throws Exception {
		if(_designer != null) {
			context.hsChannel.resContentType = "text/html; charset=UTF-8";
			try {
				return ListTools.one(_designer.getHTML(context).getBytes(StringTools.CHARSET_UTF8));
			}
			catch(AnotherContent e) {
				context.hsChannel.resContentType = e.getContentType();
				return e.getResBody();
			}
		}
		else {
			context.hsChannel.resContentType = _contentType;
			return _f;
		}
	}
}
