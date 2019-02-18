package violet.labo.module.httpserverfw;

import java.io.File;
import java.util.Map;

import charlotte.tools.FileTools;
import charlotte.tools.MapTools;
import charlotte.tools.StringTools;

public class ContentPageInfo {
	private Map<String, ContentPageInfo> _children = null;
	private HTMLDesigner _designer = null;
	private String _contentType;
	private File _f = null;

	public ContentPageInfo(File f) {
		if(f.isDirectory()) {
			_children = MapTools.<ContentPageInfo>createIgnoreCase();

			for(File subF : f.listFiles()) {
				_children.put(subF.getName(), new ContentPageInfo(subF));
			}
		}
		else {
			String path = f.getName();

			if(StringTools.endsWithIgnoreCase(path, Config.i().SERVICE_PAGE_SUFFIX)) {
				_designer = new HTMLDesigner(f);
			}
			else {
				_contentType = MIMEType.i().getMIMEType(FileTools.getExtension(f.getName()));
				_f = f;
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

	public byte[] getBody(ContextInfo context) throws Exception {
		if(_designer != null) {
			context.hsChannel.resContentType = "text/html; charset=UTF-8";
			return _designer.getHTML(context).getBytes(StringTools.CHARSET_UTF8);
		}
		else {
			context.hsChannel.resContentType = _contentType;
			return FileTools.readAllBytes(_f);
		}
	}
}
