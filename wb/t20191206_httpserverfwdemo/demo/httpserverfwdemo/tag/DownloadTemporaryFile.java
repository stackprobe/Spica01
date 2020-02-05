package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo.tag;

import java.io.File;
import java.util.function.Consumer;

import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.AnotherContent;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.ContextInfo;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.MIMEType;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.ResBodyFile;
import wb.t20191206_httpserverfwdemo.module.httpserverfw.html.tag.TagBase;

public abstract class DownloadTemporaryFile extends TagBase {
	protected abstract void writeContentTo(File f, Consumer<String> setContentType);

	@Override
	public void access(ContextInfo context) {
		throw new AnotherContent() {
			private String _file = FileTools.combine("C:/wb2/20191205_httpserverfw/httpserverfwdemo/temp", SecurityTools.makePassword_9a() + ".tmp");
			private String _contentType = MIMEType.DEFAULT_MIME_TYPE;

			{
				context.hsChannel.hDam.add(() -> FileTools.delete(_file));
				writeContentTo(new File(_file), value -> _contentType = value);
			}

			@Override
			public String getContentType() {
				return _contentType;
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
