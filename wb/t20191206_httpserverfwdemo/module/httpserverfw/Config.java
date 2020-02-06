package wb.t20191206_httpserverfwdemo.module.httpserverfw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

	public static String[] args;

	public Config() throws Exception {
		for(String arg : args) {
			processConfigLine(arg);
		}

		if(new File(CONFIG_FILE).isFile()) {
			processConfigFile(CONFIG_FILE);
		}

		// memo: IArrays.asList de wrap suruto Iterator seisei go demo tsuika dekiru
		for(String file : IArrays.asList(EXTENDED_CONFIG_FILE_LIST)) {
			processConfigFile(file);
		}
	}

	private void processConfigFile(String file) throws Exception {
		for(String line : FileTools.readAllLines(file, StringTools.CHARSET_UTF8)) {
			if(line.startsWith(";") == false) {
				processConfigLine(line);
			}
		}
	}

	private void processConfigLine(String line) throws Exception {
		String[] tokens = line.split("[=]", 2);

		if(tokens.length == 2) {
			String name = tokens[0];
			String value = tokens[1];

			setValue(name, value);
		}
	}

	private void setValue(String name, String value) throws Exception {
		FieldUnit field = ReflectTools.getField(this.getClass(), name);

		if(field == null) {
			throw new Exception("Unknown property name: " + name);
		}
		String typeName = field.inner.getGenericType().getTypeName();

		if("int".equals(typeName)) {
			field.setValue(this, Integer.parseInt(value));
		}
		else if("java.lang.String".equals(typeName)) {
			field.setValue(this, value);
		}
		else if("int[]".equals(typeName)) {
			field.setValue(this, IntTools.toArray(
					ListTools.select(
							IArrays.asList(value.split("[:]")),
							v -> Integer.parseInt(v)
							)
					));
		}
		else if("java.lang.String[]".equals(typeName)) {
			field.setValue(this, value.split("[:]", -1));
		}
		else if("java.util.List<java.lang.Integer>".equals(typeName)) {
			addToList(field, Integer.parseInt(value));
		}
		else if("java.util.List<java.lang.String>".equals(typeName)) {
			addToList(field, value);
		}
		else {
			throw new Exception("Unknown property typeName: " + typeName);
		}
	}

	private void addToList(FieldUnit field, Object value) throws Exception {
		ReflectTools.getMethod(Class.forName("java.util.List"), "add").invoke(
				field.getValue(this),
				new Object[] { value }
				);
	}

	public String CONFIG_FILE = "C:/wb2/20191205_httpserverfw/httpserverfw/Config.properties";
	public List<String> EXTENDED_CONFIG_FILE_LIST = new ArrayList<String>();

 	public int PORT_NO = 8080;
	public String DOC_ROOT_DIR = "C:/wb2/20191205_httpserverfw/httpserverfw/DocRoot";
	public String MIME_TYPE_FILE = "C:/wb2/20191205_httpserverfw/httpserverfw/MimeType.tsv";
	public String INDEX_PAGE_NAME = "index.html";
	public String[] SERVICE_PAGE_SUFFIXES = new String[] { ".html", ".htm", ".page" };
	public String[] TAG_PACKAGES = new String[] { "violet.labo.module.httpserverfw.html.tag" };
	public String SERVICE_PAGE_CHARSET = StringTools.CHARSET_UTF8;

	// test --->

	public int testInt = 123;
	public String testString = "abc";
	public int[] testInts = IntTools.sequence(1, 3);
	public String[] testStrings = "a:b:c".split("[:]");
	public List<Integer> testIntList = new ArrayList<Integer>();
	public List<String> testStringList = new ArrayList<String>();

	// <--- test
}
