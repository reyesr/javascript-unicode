package net.kornr.util.table;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class FileUtil {

	static public PrintStream openWritePrintStream(File f) throws IOException {
		PrintStream p = new PrintStream(f, "utf-8");
		return p;
	}
	
}
