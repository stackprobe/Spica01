package violet.labo.module.httpserverfw;

import java.io.File;

import charlotte.tools.FileTools;
import charlotte.tools.IArrays;
import charlotte.tools.IntTools;
import charlotte.tools.ListTools;
import charlotte.tools.RTError;
import charlotte.tools.ReflectTools;
import charlotte.tools.ReflectTools.FieldUnit;
import charlotte.tools.StringTools;

public class Config {
	private static Config _i = null;

	public static Config i() {
		if(_i == null) {
			_i = RTError.get(() -> new Config());
		}
		return _i;
	}

	public static String[] main_args;

	public Config() throws Exception {
		for(String arg : main_args) {
			String[] tokens = arg.split("[=]", 2);

			if(tokens.length == 2) {
				String name = tokens[0];
				String value = tokens[1];

				setValue(name, value);
			}
		}

		if(new File(CONFIG_FILE).isFile()) {
			for(String line : FileTools.readAllLines(CONFIG_FILE, StringTools.CHARSET_UTF8)) {
				if(line.startsWith(";") == false) {
					String[] tokens = line.split("[=]", 2);

					if(tokens.length == 2) {
						String name = tokens[0];
						String value = tokens[1];

						setValue(name, value);
					}
				}
			}
		}
	}

	private void setValue(String name, String value) throws Exception {
		FieldUnit field = ReflectTools.getField(this.getClass(), name);

		if(field != null) {
			String typeName = field.inner.getType().getName();

			if("int".equals(typeName)) {
				field.setValue(this, Integer.parseInt(value));
			}
			else if("java.lang.String".equals(typeName)) {
				field.setValue(this, value);
			}
			else if("[I".equals(typeName)) { // int[]
				field.setValue(this, IntTools.toArray(
						ListTools.select(
								IArrays.asList(value.split("[:]")),
								v -> Integer.parseInt(v)
								)
						));
			}
			else if("[Ljava.lang.String;".equals(typeName)) { // String[]
				field.setValue(this, value.split("[:]", -1));
			}
			else {
				throw new Exception("Unknown property typeName: " + typeName);
			}
		}
	}

	public String CONFIG_FILE = "C:/var/httpserverfw/Config.properties";

	public int PORT_NO = 8080;
	public String DOR_ROOT_DIR = "C:/var/httpserverfw/DocRoot";
	public String MIME_TYPE_FILE = "C:/var/httpserverfw/MimeType.tsv";
	public String INDEX_PAGE_NAME = "index.html";
	//public String INDEX_PAGE_NAME = "index.htm";
	public String SERVICE_PAGE_SUFFIX = ".html";
	//public String SERVICE_PAGE_SUFFIX = ".htm";
	public String[] TAG_PACKAGES = new String[] { "violet.labo.module.httpserverfw.html.tag" };

	// test --->

	public int testInt = 123;
	public String testString = "abc";
	public int[] testInts = IntTools.sequence(1, 3);
	public String[] testStrings = "a:b:c".split("[:]");

	// <--- test
}
