package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo.tag;

import java.io.File;

import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.AnotherContent;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.MIMEType;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.ResBodyFile;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag.TagBase;

public class DownloadFile extends TagBase {
	private String _file;

	@Override
	public void init() {
		_file = getAttributes().get("file");

		if(new File(_file).isFile() == false) {
			throw new RTError("no file: " + _file);
		}
	}

	@Override
	public void access(ContextInfo context) {
		throw new AnotherContent() {
			@Override
			public String getContentType() {
				return MIMEType.i().getMIMEType(FileTools.getExtension(_file));
			}

			@Override
			public Iterable<byte[]> getResBody() {
				return new ResBodyFile(new File(_file));
			}
		};
	}

	@Override
	public String getHTML(ContextInfo context, String innerHtml) {
		throw null; // never
	}
}
