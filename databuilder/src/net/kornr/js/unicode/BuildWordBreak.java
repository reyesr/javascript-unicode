package net.kornr.js.unicode;

import java.io.File;
import java.io.IOException;

public class BuildWordBreak {
	
	public static void main(String[] args) throws IOException {
		WordBreakData wbd = new WordBreakData().open(new File(args[0]));
		Misc.createJSFile(new File("wordbreaks.js"), "wordbreaks_data", wbd.toJavascript(120));
		System.out.println(wbd.toJavascript(120));
	}
	
}
