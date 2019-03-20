package violet.labo.demo.httpserverfwdemo.tag;

import java.io.File;
import java.util.function.Consumer;

import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import violet.labo.module.httpserverfw.AnotherContent;
import violet.labo.module.httpserverfw.ContextInfo;
import violet.labo.module.httpserverfw.MIMEType;
import violet.labo.module.httpserverfw.ResBodyFile;
import violet.labo.module.httpserverfw.html.tag.TagBase;

public abstract class DownloadTemporaryFile extends TagBase {
	protected abstract void writeContentTo(File f, Consumer<String> setContentType);

	@Override
	public void access(ContextInfo context) {
		throw new AnotherContent() {
			private String _file = FileTools.combine("C:/var2/httpserverfwdemo/temp", SecurityTools.makePassword_9a() + ".tmp");
			private String _contentType = MIMEType.DEFAULT_MIME_TYPE;

			public AnotherContent init() {
				context.hsChannel.hDam.add(() -> FileTools.delete(_file));
				writeContentTo(new File(_file), value -> _contentType = value);
				return this;
			}

			@Override
			public String getContentType() {
				return _contentType;
			}

			@Override
			public Iterable<byte[]> getResBody() {
				return new ResBodyFile(new File(_file));
			}
		}
		.init();
	}

	@Override
	public String getHTML(ContextInfo context, String innerHtml) {
		throw null; // never
	}
}