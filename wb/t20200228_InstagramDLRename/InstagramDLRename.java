package wb.t20200228_InstagramDLRename;

import java.io.File;

import charlotte.tools.ExtraTools;
import charlotte.tools.FileTools;
import charlotte.tools.IQueue;
import charlotte.tools.JsonTools;
import charlotte.tools.ObjectTree;
import charlotte.tools.QueueUnit;
import charlotte.tools.StringTools;
import charlotte.tools.WorkingDir;

public class InstagramDLRename {
	public static void main(String[] args) {
		try {
			new InstagramDLRename().main2();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private void main2() throws Exception {
		main3("C:/temp");
	}

	private String _outDir;

	private void main3(String rRootDir) throws Exception {
		_outDir = ExtraTools.makeFreeDir();

		IQueue<String> q = new QueueUnit<String>();

		q.enqueue(rRootDir);

		while(q.hasElements()) {
			String dir = q.dequeue();

			for(File f : new File(dir).listFiles()) {
				if(f.isDirectory()) {
					q.enqueue(f.getCanonicalPath());
				}
				else if(f.getName().equalsIgnoreCase("media.json")){
					ObjectTree media = new ObjectTree(JsonTools.decode(FileTools.readAllBytes(f)));
					ObjectTree photos = media.get("photos");

					for(ObjectTree photo : photos) {
						photo.get("caption").stringValue();
						String photoTakenAt = photo.get("taken_at").stringValue();
						String photoPath = photo.get("path").stringValue();

						{
							String fmt = photoTakenAt;

							fmt = StringTools.replaceChars(fmt, StringTools.DECIMAL, '9');

							if(fmt.equals("9999-99-99T99:99:99+99:99") == false) {
								throw null; // souteigai !!!
							}
						}

						String rFile = FileTools.combine(f.getParent(), photoPath);
						String wLocalFile = photoTakenAt;

						wLocalFile = wLocalFile.replace("-", "");
						wLocalFile = wLocalFile.replace("T", "");
						wLocalFile = wLocalFile.replace(":", "");
						wLocalFile = wLocalFile.replace("+", "");
						wLocalFile = wLocalFile + FileTools.getExtension(rFile);

						String wFile = FileTools.combine(_outDir, wLocalFile);

						System.out.println("< " + rFile);
						System.out.println("> " + wFile);

						if(new File(rFile).isFile()) {
							FileTools.moveFile(rFile, wFile);

							System.out.println("done");
						}
						else {
							System.out.println("no rFile");
						}
					}
				}
			}
		}

		try(WorkingDir wd = new WorkingDir()) {
			String batFile = wd.makePath() + ".bat";
			FileTools.writeAllText(batFile, "START " + _outDir, StringTools.CHARSET_SJIS);
			Runtime.getRuntime().exec(batFile).waitFor();
		}

		_outDir = null;
	}
}
