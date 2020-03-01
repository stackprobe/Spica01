package wb.t20200229_CompImagesDir;

import java.io.File;
import java.util.function.Consumer;

import charlotte.tools.IQueue;
import charlotte.tools.QueueUnit;

public class Utils {
	public static void lssFs(File rootD, Consumer<File> reaction) {
		IQueue<File> q = new QueueUnit<File>();

		q.enqueue(rootD);

		while(q.hasElements()) {
			File d = q.dequeue();

			for(File sf : d.listFiles()) {
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
