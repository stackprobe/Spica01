package wb.t20200229_CompImagesDir;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;
import charlotte.tools.RTError;
import charlotte.tools.StringTools;

public class Utils {
	public static void lssFs(File rootD, Consumer<File> reaction) {
		IQueue<File> q = new QueueUnit<File>();

		q.enqueue(rootD);

		while(q.hasElements()) {
			File d = q.dequeue();
			File[] sfs = d.listFiles();

			Arrays.sort(sfs, (a, b) -> RTError.get(() -> StringTools.comp.compare(a.getCanonicalPath(), b.getCanonicalPath())));

			for(File sf : sfs) {
				if(sf.isDirectory()) {
					q.enqueue(sf);
				}
				else {
					reaction.accept(sf);
				}
			}
		}
	}
}
