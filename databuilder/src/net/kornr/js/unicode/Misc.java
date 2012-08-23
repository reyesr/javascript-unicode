package net.kornr.js.unicode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.kornr.util.table.CsvFileReader;
import net.kornr.util.table.DataTable;

public class Misc {

	static public void createJSFile(File file, String varname, String data) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write("var net = net||{};net.kornr = net.kornr||{};net.kornr.normalizer=net.kornr.normalizer||{};\n");
		writer.write("net.kornr.normalizer."+varname+"=");
		writer.write(data);
		writer.write(";\n");
		writer.close();
	}

	
	static public DataTable read(String arg) throws IOException {
		DataTable result = null;
		CsvFileReader reader = new CsvFileReader(new File(arg), ";", "UTF-8");
		if (reader.open()) {
			result = reader.readFull();
		} else {
			throw new IOException("File " + arg + " not found");
		}
		return result;
	}

}
