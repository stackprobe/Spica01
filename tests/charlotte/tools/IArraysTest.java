package tests.charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IArrays;

public class IArraysTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		List<String> lines = new ArrayList<String>();

		lines.add("AAAA");
		lines.add("BBBB");
		lines.add("CCCC");
		lines.add("DDDD");
		lines.add("EEEE");

		// memo: IArrays.asList でラップすると Iterator 生成後でも追加出来る。
		for(String line : IArrays.asList(lines)) {

			if(line.charAt(0) == 'B') {
				lines.add("FFFF");
			}
			else if(line.charAt(0) == 'D') {
				lines.add("GGGG");
			}
			else if(line.charAt(0) == 'F') {
				lines.add("HHHH");
			}
			else if(line.charAt(0) == 'H') {
				lines.add("IIII");
			}

			System.out.println("line: " + line);
		}
	}
}
