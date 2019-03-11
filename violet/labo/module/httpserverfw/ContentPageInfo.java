package violet.labo.module.httpserverfw;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;

import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
import charlotte.tools.MapTools;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;

public class ContentPageInfo {
	private Map<String, ContentPageInfo> _children = null;
	private HTMLDesigner _designer = null;
	private String _contentType;
	private File _f = null;
	private long _fLength = -1L;

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
				_fLength = f.length();
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
			return ListTools.one(_designer.getHTML(context).getBytes(StringTools.CHARSET_UTF8));
		}
		else {
			context.hsChannel.resContentType = _contentType;
			return () -> new Iterator<byte[]>() {
				private long _currPos = 0L;

				@Override
				public boolean hasNext() {
					return _currPos < _fLength;
				}

				@Override
				public byte[] next() {
					try(FileInputStream reader = new FileInputStream(_f)) {
						reader.skip(_currPos);
						int readSize = (int)Math.min(4L * 1024 * 1024, _fLength - _currPos);
						byte[] buff = new byte[readSize];

						if(reader.read(buff) != readSize) {
							throw new RTError("Bad read size: " + readSize);
						}
						_currPos += readSize;
						return buff;
					}
					catch(Throwable e) {
						throw RTError.re(e);
					}
				}
			};
		}
	}
}
