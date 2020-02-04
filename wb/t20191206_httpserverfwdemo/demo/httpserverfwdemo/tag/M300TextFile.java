package wb.t20191206_httpserverfwdemo.demo.httpserverfwdemo.tag;

import java.io.File;
import java.io.Writer;
import java.util.function.Consumer;

import charlotte.tools.FileTools;
import charlotte.tools.RTError;
import charlotte.tools.SockChannel;
import charlotte.tools.StringTools;

public class M300TextFile extends DownloadTemporaryFile {
	@Override
	public void init() {
		// noop
	}

	@Override
	protected void writeContentTo(File f, Consumer<String> setContentType) {
		try {
			String line = StringTools.repeat("Georgia", 14) + "\n";

			try(Writer writer = FileTools.openWriter(f.getCanonicalPath(), StringTools.CHARSET_ASCII)) {
				for(int c = 0; c < 3000000; c++) {
					writer.append(line);

					if(c % 10000 == 0) {
						SockChannel.critical.contextSwitching();
					}
				}
			}
			setContentType.accept("text/plain; charset=US-ASCII");
		}
		catch(Throwable e) {
			throw RTError.re(e);
		}
	}
}
