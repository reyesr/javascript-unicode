package net.kornr.util.table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;


public class CsvFileWriter {

	private File outputFile;
	private PrintStream out = null;
	public CsvFileWriter(File output) {
		outputFile = output;
	}

	public void open() throws FileNotFoundException {
		if (out == null) {
			out = new PrintStream(outputFile);
		}
	}
	
	public void close() {
		if (out != null) {
			out.close();
		}
	}
	
	public void dump(DataTable table, boolean printHeaders) throws IOException {
		
		open();

		if (printHeaders) {
			for (Object h: table.getHeaders()) {
				out.print(h.toString() + ";");
			}
			out.println("");
		}
		
		table.iterateRows(new DataTable.RowProcessor() {
			@Override
			public void process(Object[] row) {
				for (Object o: row) {
					if (o != null) {
						out.print(o.toString());
					}
					out.print(";");
				}
				out.println("");
			}
		});
	}
	
}
